package com.example.maipetsfct.popups;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PopPet extends Activity {

    // Parámetros de conexión a Firebase
    private FirebaseAuth mAuth ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private StorageReference mStorage;

    // Definimos los elementos de la actividad
    private ImageView imgPet;
    private EditText nombre, especie, raza, color, fecha;
    private Button guardar;
    private String ruta,codigo;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_pet);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inicializamos FireBase
        FirebaseApp.initializeApp(this);

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de Storage y Database
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Obtenemos el UID del usuario logueado
        String uid = mAuth.getCurrentUser().getUid();

        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.85),(int)(height*.76));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);

        // Visualizado datos de mascota

        // Instanciamos los elementos
        nombre = findViewById(R.id.name2Pet);
        especie = findViewById(R.id.specie2Pet);
        raza = findViewById(R.id.race2Pet);
        color = findViewById(R.id.color2Pet);
        fecha = findViewById(R.id.date2Pet);
        imgPet = findViewById(R.id.imgPet);
        guardar = findViewById(R.id.saved);

        imgPet.setImageResource(R.drawable.petscard);

        // Recogemos los datos que nos llegan
        Bundle datos = getIntent().getExtras();
        String nombreB = datos.getString("nombre");
        String especieB = datos.getString("especie");
        String razaB = datos.getString("raza");
        String colorB = datos.getString("color");
        String fechaB = datos.getString("fecha");
        codigo = datos.getString("codigo");

        // Verificamos si ya existe la imagen del perfil
        if (mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_"+codigo+".jpg") != null) {

            StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_"+codigo+".jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri downloadUrl = uri;
                    ruta = downloadUrl.toString();
                    Picasso.get().load(ruta).into(imgPet);
                }
            });
        } else {
            imgPet.setImageResource(R.drawable.petscard);
        }

        // Los asociamos a cada campo de edición
        nombre.setText(nombreB);
        especie.setText(especieB);
        raza.setText(razaB);
        color.setText(colorB);
        fecha.setText(fechaB);

        //Lanzamos el popup de selección para la imagen de la mascota
        imgPet.setOnClickListener(v ->
        {
            Intent select = new Intent(this, PopUpSelect.class);
            select.putExtra("code",2);
            select.putExtra("codigo",codigo);
            startActivity(select);
        });

        // Boton de guardar los cambios realizados
        guardar.setOnClickListener(v -> {
            Map<String, Object> datosAct = new HashMap<>();

            // Recogemos los datos actualizados
            final String nombreAct =  nombre.getText().toString().trim();
            final String especieAct =  especie.getText().toString().trim();
            final String razaAct =  raza.getText().toString().trim();
            final String colorAct =  color.getText().toString().trim();
            final String fechaAct =  fecha.getText().toString().trim();

            // Los agregamos a nuestro HashMap
            datosAct.put("nombre",nombreAct);
            datosAct.put("tipo",especieAct);
            datosAct.put("raza",razaAct);
            datosAct.put("color",colorAct);
            datosAct.put("fechaNac",fechaAct);

            // Actualizamos en Firebase
            databaseReference.child("mascotas").child(uid).child(codigo).updateChildren(datosAct)
                    .addOnSuccessListener((OnSuccessListener)(aVoid) ->
                    {
                        Toast.makeText(this,R.string.save_ok,Toast.LENGTH_LONG).show();
                        finish();
                    }).addOnFailureListener((e -> {
                Toast.makeText(this,R.string.nosave,Toast.LENGTH_LONG).show();
            }));
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_"+codigo+".jpg") != null) {

            StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_"+codigo+".jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri downloadUrl = uri;
                    ruta = downloadUrl.toString();
                    Picasso.get().load(ruta).into(imgPet);
                }
            });
        }
    }
}
