package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.maipetsfct.models.Usuario;
import com.example.maipetsfct.services.initService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button letsGo;

    private FirebaseAuth mAuth ;
    private FirebaseDatabase fbdatabase ;

    Intent intent;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        // Inicializamos Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;

        // Instancio el boton y configuro su listener
        letsGo = findViewById(R.id.go);

        letsGo.setOnClickListener(v -> {
            Intent start = new Intent(MainActivity.this, PostMainActivity.class);
            goMusic();
            startActivity(start);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String userID = sessionManagement.getSession();
        String mail = sessionManagement.getMail();
        String pass = sessionManagement.getPass();

        if (userID != "Usuario") {
            // El usuario ya está logeado
            logIn(mail,pass);
        } else {
            // No hacemos nada
        }
    }

    private void logIn(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        // Error en el Login
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), R.string.log_nok, Toast.LENGTH_LONG).show();
                        } else
                        {
                            // Login se realiza correctamente
                            // Toast de prueba para si logea bien
                            Toast.makeText(getApplicationContext(), R.string.log_exist, Toast.LENGTH_LONG).show();

                            // 1. Obtenemos el UID del usuario logueado
                            String uid = mAuth.getCurrentUser().getUid();

                            // 2. Obtenemos instancia de FirebaseDatabase
                            fbdatabase = FirebaseDatabase.getInstance();

                            // 3. Obtenemos referencia al usuario logueado
                            DatabaseReference userRef = fbdatabase.getReference().child("usuarios/"+uid);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    // comprobar que existe el hijo que busco
                                    if (dataSnapshot.hasChildren())
                                    {
                                        // Obtenemos el codigo del usuario para seleccionar activity
                                        String codify = dataSnapshot.child("codigo").getValue().toString();

                                        // Obtenemos los datos del usuario logeado
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class) ;

                                        // creamos un diccionario para poner los datos del usuario
                                        Bundle bundle = new Bundle() ;
                                        bundle.putSerializable("_usuario", usuario) ;

                                        //Guardamos sesion de usuario
                                        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                                        sessionManagement.saveSession(usuario);


                                        // Entramos a la app
                                        // Creamos el intent de usuario segun cual sea
                                        switch (codify){
                                            case "fam":
                                                intent = new Intent(MainActivity.this, UsersActivity.class);
                                                break;
                                            case "ser":
                                                intent = new Intent(MainActivity.this, VetActivity.class) ;
                                                break;
                                        }
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
    }


    private void goMusic() {
        Intent ring = new Intent(MainActivity.this, initService.class);
        startService(ring);
    }
}
