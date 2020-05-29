package com.example.maipetsfct.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.maipetsfct.models.Usuario;

import com.example.maipetsfct.R;
import com.example.maipetsfct.adapters.UsuarioAdapter;
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

    // Colección de servicios de usuarios
    ArrayList<Usuario> usuarios;
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
                    if (u.getActividad() != null) {
                        usuarios.add(u);
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

        return  view;
    }
}
