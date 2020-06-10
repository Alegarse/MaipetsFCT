package com.example.maipetsfct.popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.maipetsfct.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PopServ extends Activity {

    // Parámetros de conexión a Firebase
    private FirebaseAuth mAuth ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // Definimos los elementos de la actividad
    private ImageView image;
    private EditText nombre, desc;
    private Button guardar;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_serv);

        // Inicializamos FireBase
        FirebaseApp.initializeApp(this);

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Obtenemos el UID del usuario logueado
        String uid = mAuth.getCurrentUser().getUid();

        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);

        // Instanciamos los elementos
        image = findViewById(R.id.imgService);
        image.setImageResource(R.drawable.imgserv);
        nombre = findViewById(R.id.name2Serv);
        desc = findViewById(R.id.desc2Serv);
        guardar = findViewById(R.id.saved);

        // Recogemos los datos que nos llegan
        Bundle datos = getIntent().getExtras();
        String nombreS = datos.getString("nombre");
        String descS = datos.getString("desc");
        codigo = datos.getString("codigo");

        // Los asociamos a cada campo de edición
        nombre.setText(nombreS);
        desc.setText(descS);

        // Boton de guardar los cambios realizados
        guardar.setOnClickListener(v -> {
            Map<String, Object> datosAct = new HashMap<>();

            // Recogemos los datos actualizados
            final String nombreAct =  nombre.getText().toString().trim();
            final String descAct =  desc.getText().toString().trim();

            // Los agregamos a nuestro HashMap
            datosAct.put("nombre",nombreAct);
            datosAct.put("desc",descAct);

            // Actualizamos en Firebase
            databaseReference.child("servicios").child(codigo).updateChildren(datosAct)
                    .addOnSuccessListener((OnSuccessListener)(aVoid) ->
                    {
                        Toast.makeText(this,R.string.save_ok,Toast.LENGTH_LONG).show();
                        finish();
                    }).addOnFailureListener((e -> {
                Toast.makeText(this,R.string.nosave,Toast.LENGTH_LONG).show();
            }));
        });
    }
}