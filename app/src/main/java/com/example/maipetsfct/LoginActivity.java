package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maipetsfct.models.Usuario;
import com.example.maipetsfct.popups.PopReset;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // Inicializamos variables u objetos que intervienen en la actividad
    private EditText email, password;
    private Button login, cancel;
    private TextView resetPass;
    String codify;
    Bundle bundle;

    private FirebaseAuth mAuth ;
    private FirebaseDatabase fbdatabase ;

    private Intent intent;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        // Inicializamos Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;

        // Referenciamos los campos y botones
        email = findViewById(R.id.userEmailLogin);
        password = findViewById(R.id.userPassLogin);
        login = findViewById(R.id.btnLogin);
        cancel = findViewById(R.id.btnCancel);
        resetPass = findViewById(R.id.passLoose);

        // Escuchador para el botón Cancelar
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelamos y volvemos al principal
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        });

        // Escuchador del boton recuperar contraseña
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(LoginActivity.this, PopReset.class);
                startActivity(reset);
            }
        });

        // ################################   LOGIN    #############################################

        // Escuchador para el botón de Login
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Guardamos las variables introducidas
                String mail = email.getText().toString();
                String passw = password.getText().toString();

                if (mail.isEmpty() || passw.isEmpty() ){
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                }

                // Realizamos el logeo

                logIn(mail,passw);

            }
        });
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
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
                            Toast.makeText(getApplicationContext(), R.string.log_ok, Toast.LENGTH_LONG).show();

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
                                        codify = dataSnapshot.child("codigo").getValue().toString();

                                        // Obtenemos los datos del usuario logeado
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class) ;

                                        // creamos un diccionario para poner los datos del usuario
                                        bundle = new Bundle() ;
                                        bundle.putSerializable("_usuario", usuario) ;

                                        //Guardamos sesion de usuario
                                        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                        sessionManagement.saveSession(usuario);


                                        // Entramos a la app
                                        // Creamos el intent de usuario segun cual sea
                                        switch (codify){
                                            case "fam":
                                                intent = new Intent(LoginActivity.this, UsersActivity.class);
                                                break;
                                            case "ser":
                                                intent = new Intent(LoginActivity.this, VetActivity.class) ;
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

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        // Verificamos si el usuario está ya logeado
        // Si así fuese entramos directamente a la app

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
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
}
