package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maipetsfct.models.servicio;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddServActivity extends AppCompatActivity {

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase ;

    // Botones y elementos
    private Button cancAdd, okAdd;
    private EditText nombre;
    private EditText descripcion;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_serv);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;

        String uid = fbauth.getCurrentUser().getUid();

        // Inicializamoslos elementos
        cancAdd = findViewById(R.id.cancAddBtn);
        okAdd = findViewById(R.id.addBtn);
        nombre= findViewById(R.id.addNameS);
        descripcion = findViewById(R.id.addDescS);

        // Escuchador para el botón Cancelar
        cancAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelamos y volvemos al principal
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        });

        //Escuchador para el boton añadir
        okAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el valor de los campos
                final String uUid = uid.trim();
                final String sNombre = nombre.getText().toString().trim();
                final String sDescripcion = descripcion.getText().toString().trim();

                String sUid = UUID.randomUUID().toString();

                //Verificamos que los campos contienen información
                if (sNombre.isEmpty() || sDescripcion.isEmpty()) {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                } else {
                    servicio serv = new servicio(sUid,uUid,sNombre,sDescripcion);

                    DatabaseReference dbref = fbdatabase.getReference("servicios");

                    dbref.child(sUid).setValue(serv);

                    //fbauth.signOut();
                    setResult(RESULT_OK);
                    finish();
                    return;
                }
            }
        });
    }
}
