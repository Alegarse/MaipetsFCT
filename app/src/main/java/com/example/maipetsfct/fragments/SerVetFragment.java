package com.example.maipetsfct.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.maipetsfct.AddServActivity;
import com.example.maipetsfct.popups.PopServ;
import com.example.maipetsfct.R;
import com.example.maipetsfct.adapters.ServicioAdapter;
import com.example.maipetsfct.models.servicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class SerVetFragment extends Fragment {

    private Button btnAdd;
    public final int COD_REGISTRO=000;

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference,ref;
    private StorageReference mStorageRef;

    // Colección de servicios
    ArrayList<servicio> servicios;
    RecyclerView recyclerView;
    ServicioAdapter servicioAdapter;

    public SerVetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ser_vet, container, false);

        btnAdd = view.findViewById(R.id.addServ);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance();

        String uid = fbauth.getCurrentUser().getUid();

        Activity activity = getActivity();

        // ZONA PARA EL CARDVIEW                      ######################################

        recyclerView = view.findViewById(R.id.servShows);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        servicios = new ArrayList<servicio>();

        ref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("servicios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                servicios.clear();
                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                    servicio s = data1.getValue(servicio.class);
                    servicios.add(s);
                }
                servicioAdapter = new ServicioAdapter(activity,servicios);
                servicioAdapter.setServicios(servicios);
                recyclerView.setAdapter(servicioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity, R.string.s_noadd, Toast.LENGTH_LONG).show();
            }
        });

        registerForContextMenu(recyclerView);

        // Defino escuchador para el botón AÑADIR
        btnAdd.setOnClickListener(viewAdd -> {

            // Intencion para proceder a añadir mascota
            Intent add = new Intent(activity, AddServActivity.class);

            // Empezar la intención
            startActivityForResult(add, COD_REGISTRO);
        });

        return view;
    }


    // Menú contextual para las tarjetas de las mascotas
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.contextual, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ctxEdd:

                servicio serv = servicios.get(servicioAdapter.getIndex());

                Intent irAEditar = new Intent(getActivity().getApplicationContext(), PopServ.class);
                irAEditar.putExtra("nombre",serv.getNombre());
                irAEditar.putExtra("desc",serv.getDesc());
                irAEditar.putExtra("codigo",serv.getsUid());
                startActivity(irAEditar);
                break;

            case R.id.ctxDel:
                servicio ser = servicios.get(servicioAdapter.getIndex());
                String UUID = ser.getsUid();
                ref.child("servicios").child(UUID).removeValue();
                Toast.makeText(getActivity().getApplicationContext(),R.string.ficDel, Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
