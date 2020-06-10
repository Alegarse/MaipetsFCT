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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maipetsfct.adapters.CitasAdapter;
import com.example.maipetsfct.models.Cita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CitasActivity extends AppCompatActivity {

    private Button citar;
    private TextView nombreM;

    private String titulo,mascota;

    RecyclerView recyclerView;
    ArrayList<Cita> citas;
    CitasAdapter citaAdapter;

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference,ref;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        this.setTitle(R.string.datMan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle datosCitas = getIntent().getExtras();
        String nameM = datosCitas.getString("nombre");

        // Instanciamos
        citar = findViewById(R.id.sacDate);
        nombreM = findViewById(R.id.citaNomPU);
        nombreM.setText(nameM);

        //Obtenemos conexiones Firebase
        fbauth = FirebaseAuth.getInstance() ;
        fbdatabase =  FirebaseDatabase.getInstance();
        String uid = fbauth.getCurrentUser().getUid();

        // Escuchador para el boton citar
        citar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent programarC = new Intent(CitasActivity.this, PopSelectAct.class);
                programarC.putExtra("nombreMasc",nameM);
                startActivity(programarC);
            }
        });

        // ZONA PARA LOS CARDVIEW DE CITAS                  ######################################
        recyclerView = findViewById(R.id.citasList);
        recyclerView.setLayoutManager(new LinearLayoutManager(CitasActivity.this));

        citas = new ArrayList<Cita>();
        reference = FirebaseDatabase.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference().child("citas");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                citas.clear();
                for (DataSnapshot dataCita: dataSnapshot.getChildren()) {
                    Cita c = dataCita.getValue(Cita.class);
                    if (c.getIdUsuario().equals(uid) && (c.getNombreMascota().equals(nameM))) {
                        citas.add(c);
                    }
                }
                citaAdapter = new CitasAdapter(CitasActivity.this,citas);
                citaAdapter.setCitas(citas);
                recyclerView.setAdapter(citaAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        registerForContextMenu(recyclerView);
    }


    // Menú contextual para las tarjetas de las citas
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.citas_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.ctxDelC:

                AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
                myBuild.setTitle(R.string.cDel);
                myBuild.setMessage(R.string.delCita);
                myBuild.setPositiveButton(R.string.afirmative, (dialogInterface, i) -> {

                    Cita cita = citas.get(citaAdapter.getIndex());
                    String UUID = cita.getIdCita();
                    ref.child(UUID).removeValue();
                    Toast.makeText(this,R.string.ficDel, Toast.LENGTH_LONG).show();

                });
                myBuild.setNegativeButton("No", (dialogInterface, i) ->
                        dialogInterface.cancel());
                AlertDialog dialog = myBuild.create();
                dialog.show();
        }
        return super.onContextItemSelected(item);
    }
}
