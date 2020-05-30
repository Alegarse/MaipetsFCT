package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

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
                        if(repes.contains(u.getActividad())) {
                        } else {
                            usuarios.add(u);
                            repes.add(u.getActividad());
                        }
                    }
                }
                actividadAdapter = new ActividadAdapter(getApplicationContext(),usuarios);
                actividadAdapter.setUsuarios(usuarios);
                recyclerView.setAdapter(actividadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        registerForContextMenu(recyclerView);

    }
    // Menú contextual para las tarjetas de las mascotas
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.servicesmenu, menu);

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ctxDatos:

                AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
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
