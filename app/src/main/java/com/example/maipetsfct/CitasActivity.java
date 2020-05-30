package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        citas = new ArrayList<Cita>();
        ref = FirebaseDatabase.getInstance().getReference().child("citas");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                citas.clear();

                for (DataSnapshot dataCita: dataSnapshot.getChildren()) {
                    Cita c = dataCita.getValue(Cita.class);
                    if (c.getIdUsuario().equals(uid) || (c.getNombreMascota().equals(nameM))) {
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




    }
}
