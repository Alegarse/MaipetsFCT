package com.example.maipetsfct.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import com.example.maipetsfct.MainActivity;
import com.example.maipetsfct.PopUpSelect;
import com.example.maipetsfct.R;
import com.example.maipetsfct.models.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfVetFragment extends Fragment {

    private Button delPerf, editPerf;
    private EditText bussname, direction, telefono, email, contra;
    private ImageView imgPerfil;
    private String ruta;

    // Elementos para Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference;
    private StorageReference mStorageRef;
    FirebaseUser usuario;

    public ProfVetFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg") != null) {

            StorageReference profileImgPath = mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri downloadUrl = uri;
                    ruta = downloadUrl.toString();
                    Picasso.get().load(ruta).into(imgPerfil);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prof, container, false);

        Activity activity = getActivity();

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase y Storage
        fbdatabase =  FirebaseDatabase.getInstance() ;
        String uid = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        usuario = mAuth.getInstance().getCurrentUser();

        // Instanciamos
        delPerf = view.findViewById(R.id.delProf);
        editPerf= view.findViewById(R.id.edProf);
        bussname = view.findViewById(R.id.nameB);
        direction = view.findViewById(R.id.dirB);
        telefono = view.findViewById(R.id.telB);
        email = view.findViewById(R.id.emaB);
        contra = view.findViewById(R.id.passB);
        imgPerfil = view.findViewById(R.id.imgPerfil);


        // Verificamos si ya existe la imagen del perfil
        if (mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg") != null) {

            StorageReference profileImgPath = mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri downloadUrl = uri;
                    ruta = downloadUrl.toString();
                    Picasso.get().load(ruta).into(imgPerfil);
                }
            });
        } else {
            imgPerfil.setImageResource(R.drawable.profile);
        }


        //Lanzamos el popup de selección para la imagen de perfil
        imgPerfil.setOnClickListener(v ->
        {
            Intent select = new Intent(activity, PopUpSelect.class);
            select.putExtra("code",1);
            startActivity(select);
        });


        // Datos del perfil del usuario
        // Mostramos los datos en los campos de datos
        reference.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String bussnameFb = dataSnapshot.child("razon").getValue().toString();
                    String dirFb = dataSnapshot.child("direccion").getValue().toString();
                    String telFb = dataSnapshot.child("telefono").getValue().toString();
                    String emaFb = dataSnapshot.child("email").getValue().toString();
                    String passFb = dataSnapshot.child("contrasena").getValue().toString();
                    bussname.setText(bussnameFb);
                    direction.setText(dirFb);
                    telefono.setText(telFb);
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
                usuario.delete();
                reference.child("usuarios").child(uid).removeValue();
                reference.child("servicios").child(uid).removeValue();
                mAuth.signOut();
                Intent salir = new Intent(activity, MainActivity.class);
                startActivity(salir);
            }
        });

        // Botón guardar

        editPerf.setOnClickListener(new View.OnClickListener() {
            private View v;
            @Override
            public void onClick(View v) {
                final String bnom = getField(bussname);
                final String dir = getField(direction);
                final String tel = getField(telefono);
                final String ema = getField(email);
                final String pwd = getField(contra);

                // Verificamos que se han introducido todos los campos
                if (ema.isEmpty() || bnom.isEmpty() || dir.isEmpty() || tel.isEmpty() ||
                        pwd.isEmpty())
                {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                }
                String uid = mAuth.getUid();

                Usuario usuario = new Usuario(bnom,dir,tel,ema,pwd);

                DatabaseReference dbref = fbdatabase.getReference("usuarios");

                dbref.child(uid).setValue(usuario) ;
                Snackbar.make(v, getResources().getText(R.string.save_ok), Snackbar.LENGTH_LONG).show();
                return;
            }
        });

        return view;
    }



    // Método para obtener texto de los campos
    private String getField(EditText edit)
    {
        return edit.getText().toString().trim() ;
    }

}
