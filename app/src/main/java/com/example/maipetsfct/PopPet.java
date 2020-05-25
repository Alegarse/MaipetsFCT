package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PopPet extends Activity {

    // Parámetros de conexión a Firebase
    private FirebaseAuth mAuth ;
    private StorageReference mStorage;

    // Definimos los elementos de la actividad
    private ImageView imgPet;
    private EditText nombre, especie, raza, color, fecha;
    private Button guardar;
    private String ruta;


    @Override
    public void onResume() {
        super.onResume();
        if (mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg.jpg") != null) {

            StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg.jpg");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_pet);

        // Inicializamos FireBase
        FirebaseApp.initializeApp(this);

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de Storage
        mStorage = FirebaseStorage.getInstance().getReference();

        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.9));

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

        imgPet.setImageResource(R.drawable.petscard);

        // Verificamos si ya existe la imagen del perfil
        if (mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg.jpg") != null) {

            StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg.jpg");
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

        // Recogemos los datos que nos llegan
        Bundle datos = getIntent().getExtras();
        String nombreB = datos.getString("nombre");
        String especieB = datos.getString("especie");
        String razaB = datos.getString("raza");
        String colorB = datos.getString("color");
        String fechaB = datos.getString("fecha");

        // Los asociamos a cada campo de edición
        nombre.setText(nombreB);
        especie.setText(especieB);
        raza.setText(razaB);
        color.setText(colorB);
        fecha.setText(fechaB);

        //Lanzamos el popup de selección para la imagen de la mascota
        imgPet.setOnClickListener(v ->
        {
            Intent select = new Intent(this,PopUpSelect.class);
            select.putExtra("code",2);
            startActivity(select);
        });

    }
}
