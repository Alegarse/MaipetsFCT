package com.example.maipetsfct.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.example.maipetsfct.models.Usuario;

import com.example.maipetsfct.R;
import com.example.maipetsfct.adapters.UsuarioAdapter;
import com.example.maipetsfct.popups.PopPet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference,ref;
    private StorageReference mStorageRef;
    int test = 0;

    // Colección de servicios de usuarios
    ArrayList<Usuario> usuarios;
    ArrayList<String> repes = new ArrayList<>();
    RecyclerView recyclerView;
    UsuarioAdapter usuarioAdapter;


    private Button prueba;


    public ServicesFragment() {
        // Constructor vacio requerido
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        Activity activity = getActivity();

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance();

        String uid = fbauth.getCurrentUser().getUid();

        // ZONA PARA LOS CARDVIEW                      ######################################
        recyclerView = view.findViewById(R.id.servsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        usuarios = new ArrayList<Usuario>();

        ref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarios.clear();
                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                    Usuario u = data1.getValue(Usuario.class);

                    // Para que no muestre servicios repetidos
                        if (u.getActividad() != null) {
                            if(repes.contains(u.getActividad())) {
                            } else {
                                usuarios.add(u);
                                repes.add(u.getActividad());
                            }
                        }
                }

                usuarioAdapter = new UsuarioAdapter(activity,usuarios);
                usuarioAdapter.setUsuarios(usuarios);
                recyclerView.setAdapter(usuarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity, R.string.s_noadd, Toast.LENGTH_LONG).show();
            }
        });
        registerForContextMenu(recyclerView);

        return  view;
    }

    // Menú contextual para las tarjetas de las mascotas
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.servicesmenu, menu);

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ctxEdd:

                Usuario usu = usuarios.get(usuarioAdapter.getIndex());
/*
                Intent irAEditar = new Intent(getActivity().getApplicationContext(), PopPet.class);
                irAEditar.putExtra("codigo",usu.getCodigo());
                irAEditar.putExtra("nombre",usu.getNombre());
                irAEditar.putExtra("especie",usu.getTipo());
                irAEditar.putExtra("raza",usu.getRaza());
                irAEditar.putExtra("color",usu.getColor());
                irAEditar.putExtra("fecha",usu.getFechaNac());
                startActivity(irAEditar);

 */
                break;

            case R.id.ctxDel:

                AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
                myBuild.setTitle(R.string.cDel);
                myBuild.setMessage(R.string.delPet);
                myBuild.setPositiveButton(R.string.afirmative, (dialogInterface, i) -> {
/*
                    Usuario usua = usuarios.get(usuarioAdapter.getIndex());
                    String UUID = usua.getCodigo();
                    String uid = fbauth.getCurrentUser().getUid();

                    ref.child("usuarios").child(uid).child(UUID).removeValue();
                    Toast.makeText(getActivity().getApplicationContext(),R.string.ficDel, Toast.LENGTH_LONG).show();
 */
                });


                myBuild.setNegativeButton("No", (dialogInterface, i) ->
                        dialogInterface.cancel());

                AlertDialog dialog = myBuild.create();
                dialog.show();

        }
        return super.onContextItemSelected(item);
    }
}
