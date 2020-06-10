package com.maipetsfct.popups;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maipetsfct.R;
import com.maipetsfct.adapters.ServicioAdapter2;
import com.maipetsfct.adapters.sgeAdapter;
import com.maipetsfct.models.Usuario;
import com.maipetsfct.models.servicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Pop_spr extends Activity {

    private ImageView imgUsuario;
    private TextView nombreRUsu, tipoRUsu;
    private String uidSel;

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference,ref;

    // Colección de servicios de usuarios
    ArrayList<Usuario> usuarios;
    ArrayList<servicio> servicios;
    RecyclerView recyclerView,recyclerView2;
    sgeAdapter usuarioAdapter;
    ServicioAdapter2 servicioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_spr);

        imgUsuario = findViewById(R.id.imgUsu);
        nombreRUsu = findViewById(R.id.nombreRUsu);
        tipoRUsu = findViewById(R.id.tipoRUsu);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance();

        // LISTADO DE USUARIOS TIPO SERVICIO
        recyclerView = findViewById(R.id.listUsusAct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuarios = new ArrayList<Usuario>();

        reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarios.clear();
                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                    Usuario u = data1.getValue(Usuario.class);
                    if (u.getCodigo().equals("ser")){
                        usuarios.add(u);
                    }
                }
                usuarioAdapter = new sgeAdapter(Pop_spr.this,usuarios);
                usuarioAdapter.setUsuarios(usuarios);
                usuarioAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));
                        uidSel = user.getUid();
                            if (user.getUrlImage().equals("empty")) {
                                imgUsuario.setImageResource(R.drawable.profile);
                            } else {
                                Picasso.get().load(user.getUrlImage()).into(imgUsuario);
                            }
                            nombreRUsu.setText(user.getRazon());
                            tipoRUsu.setText(user.getActividad());

                        // ALIMENTO EL SEGUNDO RECYCLERVIEW
                        recyclerView2 = findViewById(R.id.listServsAct);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        servicios = new ArrayList<servicio>();

                        ref = FirebaseDatabase.getInstance().getReference().child("servicios");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                servicios.clear();
                                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                                    servicio s = data1.getValue(servicio.class);
                                    if (s.getUid().equals(uidSel)){
                                        servicios.add(s);
                                    }
                                }
                                servicioAdapter = new ServicioAdapter2(Pop_spr.this,servicios);
                                servicioAdapter.setServicios(servicios);
                                servicioAdapter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        servicio serv = servicios.get(recyclerView.getChildAdapterPosition(v));
                                        String desc = serv.getDesc();
                                        String nombre = serv.getNombre();
                                        Intent vermasInfo = new Intent(Pop_spr.this,Pop_spr_det.class);
                                        vermasInfo.putExtra("titulo",nombre);
                                        vermasInfo.putExtra("desc",desc);
                                        startActivity(vermasInfo);
                                    }
                                });
                                recyclerView2.setAdapter(servicioAdapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
                recyclerView.setAdapter(usuarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.s_noadd, Toast.LENGTH_LONG).show();
            }
        });
    }
}
