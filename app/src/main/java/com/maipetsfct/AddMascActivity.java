package com.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maipetsfct.R;
import com.maipetsfct.fragments.MyDatePicker;
import com.maipetsfct.models.mascota;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class AddMascActivity extends AppCompatActivity {

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase ;

    // Botones y elementos
    private Button cancAdd, okAdd;
    private String ruta = "empty";
    private EditText nombre;
    private EditText tipo;
    private EditText raza;
    private EditText color;
    private static EditText fecha;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masc);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;

        String uid = fbauth.getCurrentUser().getUid();

        // Inicializamos los botones y campos
        cancAdd = findViewById(R.id.cancAddBtn);
        okAdd = findViewById(R.id.addBtn);
        nombre = findViewById(R.id.addName);
        tipo = findViewById(R.id.addType);
        raza = findViewById(R.id.addRace);
        color = findViewById(R.id.addCol);
        fecha = findViewById(R.id.addFec);

        fecha.setKeyListener(null);

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

        // Escuchador fecha
        fecha.setOnClickListener(v -> {
            DialogFragment fragment = new MyDatePicker();
            fragment.show(getSupportFragmentManager(),"Date Picker");
        });

        //Escuchador para el boton añadir
        okAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Sacamos el valor de los campos
                final String uUid = uid.trim();
                final String mUid = UUID.randomUUID().toString();
                final String aNombre = nombre.getText().toString().trim();
                final String aTipo = tipo.getText().toString().trim();
                final String aRaza = raza.getText().toString().trim();
                final String aColor = color.getText().toString().trim();
                final String aFecha = fecha.getText().toString().trim();

                //Verificamos que los campos contienen información
                if (aNombre.isEmpty() || aTipo.isEmpty() || aRaza.isEmpty() || aColor.isEmpty() || aFecha.isEmpty())
                {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                } else {

                    mascota masc = new mascota(ruta,mUid,aNombre,aTipo,aRaza,aColor,aFecha);

                    DatabaseReference dbref = fbdatabase.getReference("mascotas/"+uUid);

                    dbref.child(mUid).setValue(masc) ;

                    //fbauth.signOut();
                    setResult(RESULT_OK);
                    finish();
                    return;
                }
            }
        });
    }

    public static void populateSetDateText (int day, int month, int year) {
        fecha.setText(day + "/" + month + "/" + year);
    }
}