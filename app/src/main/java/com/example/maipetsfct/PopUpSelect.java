package com.example.maipetsfct;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;


public class PopUpSelect extends Activity {

    private Button gallery, camera;
    private Bitmap bitMap;
    private String ruta;
    private int option;

    // Elementos para la cámara
    static MagicalPermissions magicalPermissions;
    static MagicalCamera magicalCamera;
    static final int resol = 50;

    // Elementos para Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_select);

        gallery = findViewById(R.id.galleryBtn);
        camera = findViewById(R.id.cameraBtn);

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase y Storage
        fbdatabase =  FirebaseDatabase.getInstance() ;
        mStorage = FirebaseStorage.getInstance().getReference();


        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);

        // Permisos para el uso de la camara
        String[] permisos = new String[]
                {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

        //Instanciamos la cámara
        magicalPermissions = new MagicalPermissions(this, permisos);
        magicalCamera = new MagicalCamera(PopUpSelect.this,resol,magicalPermissions);

        // Si pulso el boton galería
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.selectedPicture("ProfileImg");
                option = 1;
            }
        });

        // Si pulso el boton camara
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takePhoto();
                option = 2;
            }
        });
    }

    // Para colocar la imagen sacada en el perfil
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        magicalCamera.resultPhoto(requestCode, resultCode, data);
        if (option == 2) {
            magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_90);
        }

         // Seteamos a variable bitmap la imagen tomada
        bitMap = magicalCamera.getPhoto();

        // Guardamos la imagen en el dispositivo
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"ProfileImg","Maipets", MagicalCamera.JPEG, true);

        if(path != null){
            Toast.makeText(PopUpSelect.this, getString(R.string.imgSave) + path, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(PopUpSelect.this, getString(R.string.imgNoSave), Toast.LENGTH_SHORT).show();
        }

        //Subimos la foto a Firebase
        Uri file = Uri.fromFile(new File(path));
        StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg");

        profileImgPath.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Para obtener la URL de la imagen subida a Firebase del perfil
                        profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                ruta = downloadUrl.toString();

                                //Paso el dato
                                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Url",ruta);
                                editor.commit();

                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
/*
        Intent pasarUrl = new Intent (this, ProfileFragment.class);
        pasarUrl.putExtra("Url",ruta);
        startActivity(pasarUrl);
         */

        }


}
