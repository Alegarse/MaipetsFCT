package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.maipetsfct.adapters.ActividadAdapter;
import com.example.maipetsfct.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopSelectAct extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Usuario> usuarios;
    ArrayList<String> repes = new ArrayList<>();
    ActividadAdapter actividadAdapter;

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference ref;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_select);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle datosCitaServ = getIntent().getExtras();
        String nombreMasc = datosCitaServ.getString("nombreMasc");

        //Obtenemos conexiones Firebase
        fbauth = FirebaseAuth.getInstance() ;
        fbdatabase =  FirebaseDatabase.getInstance();
        String uid = fbauth.getCurrentUser().getUid();

        // ZONA DEL CARDVIEW DE SERVICIOS DIPONIBLES CLICKABLES
        recyclerView = findViewById(R.id.servDispCit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuarios = new ArrayList<Usuario>();

        ref = FirebaseDatabase.getInstance().getReference().child("usuarios");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    Usuario u = datos.getValue(Usuario.class);

                    // Para que no muestre actividades repetidas
                    if (u.getActividad() != null) {
                        if(repes.contains(u.getServCode())) {
                        } else {
                            usuarios.add(u);
                            repes.add(u.getServCode());
                        }
                    }
                }
                actividadAdapter = new ActividadAdapter(getApplicationContext(),usuarios);
                actividadAdapter.setUsuarios(usuarios);

                actividadAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));
                        Toast.makeText(getApplicationContext(),"Selected: "+ usuarios.get(recyclerView.getChildAdapterPosition(v)).getActividad(), Toast.LENGTH_SHORT).show();

                        Intent irASelServ = new Intent(PopSelectAct.this,ServDispActivity.class);
                        irASelServ.putExtra("nombreMasc",nombreMasc);
                        irASelServ.putExtra("actividad", user.getActividad());
                        irASelServ.putExtra("mail",user.getEmail());
                        irASelServ.putExtra("servCode",user.getServCode());
                        startActivity(irASelServ);
                        }
                });

                recyclerView.setAdapter(actividadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        registerForContextMenu(recyclerView);

    }

}
