package com.example.maipetsfct;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.maipetsfct.models.Usuario;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private Button delPerf, editPerf;
    private EditText nombre, apellidos, email, contra;
    private ImageView imgPerfil;

    // Elementos para Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference;

    // Elementos para la cámara
    static MagicalPermissions magicalPermissions;
    static MagicalCamera magicalCamera;
    static final int resol = 50;


    public ProfileFragment() {
        // Constructor vacio requerido
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Activity activity = getActivity();

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;
        String uid = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        // Instanciamos
        delPerf = view.findViewById(R.id.delProf);
        editPerf= view.findViewById(R.id.edProf);
        nombre = view.findViewById(R.id.nameP);
        apellidos = view.findViewById(R.id.apeP);
        email = view.findViewById(R.id.emaP);
        contra = view.findViewById(R.id.passP);
        imgPerfil = view.findViewById(R.id.imgPerfil);

        // Opción cambiar foto del perfil

        // Permisos para el uso de la camara
        String[] permisos = new String[]
                {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

        //Instanciamos la cámara
        magicalPermissions = new MagicalPermissions(this, permisos);
        magicalCamera = new MagicalCamera(activity,resol,magicalPermissions);

        //Lanzamos la camara para sacar la fotografía
        imgPerfil.setOnClickListener(v ->
        {
            magicalCamera.takeFragmentPhoto(ProfileFragment.this);
        });





        // Mostramos los datos en los campos de datos
        reference.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String nameFb = dataSnapshot.child("nombre").getValue().toString();
                    String apeFb = dataSnapshot.child("apellidos").getValue().toString();
                    String emaFb = dataSnapshot.child("email").getValue().toString();
                    String passFb = dataSnapshot.child("contrasena").getValue().toString();
                    nombre.setText(nameFb);
                    apellidos.setText(apeFb);
                    email.setText(emaFb);
                    contra.setText(passFb);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Boton borrar

        delPerf.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                reference.child("usuarios").child(uid).removeValue();
                reference.child("mascotas").child(uid).removeValue();
                mAuth.signOut();
                Intent salir = new Intent(activity,MainActivity.class);
                startActivity(salir);
            }
        });

        // Botón guardar

        editPerf.setOnClickListener(new View.OnClickListener() {
            private View v;
            @Override
            public void onClick(View v) {
                final String nom = getField(nombre);
                final String ape = getField(apellidos);
                final String ema = getField(email);
                final String pwd = getField(contra);

                // Verificamos que se han introducido todos los campos
                if (ema.isEmpty() || nom.isEmpty() || ape.isEmpty() ||
                        pwd.isEmpty())
                {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                }
                String uid = mAuth.getUid();

                Usuario usuario = new Usuario(nom,ape,ema,pwd);

                DatabaseReference dbref = fbdatabase.getReference("usuarios");

                dbref.child(uid).setValue(usuario) ;
                Snackbar.make(v, getResources().getText(R.string.save_ok), Snackbar.LENGTH_LONG).show();
                return;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        magicalCamera.resultPhoto(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_90);

        // Seteamos el bitmap a la imagen de perfil
        imgPerfil.setImageBitmap(magicalCamera.getPhoto());

    }

    // Método para obtener texto de los campos
    private String getField(EditText edit)
    {
        return edit.getText().toString().trim() ;
    }


}
