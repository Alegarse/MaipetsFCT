package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maipetsfct.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private FirebaseAuth mAuth ;
    private FirebaseDatabase fbdatabase ;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        // Datos para logeo de prueba temporal
        email.setText("aleboy80@gmail.com");
        password.setText("123456");

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
                Intent reset = new Intent(LoginActivity.this,PopReset.class);
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

                // Realizamos el logeo
                mAuth.signInWithEmailAndPassword(mail,passw)
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
                                        Bundle bundle = new Bundle() ;
                                        bundle.putSerializable("_usuario", usuario) ;

                                        // Creamos el intent de usuario segun cual sea
                                        switch (codify){
                                            case "fam":
                                                intent = new Intent(LoginActivity.this, UsersActivity.class);
                                                break;
                                            case "ser":
                                                intent = new Intent(LoginActivity.this, VetActivity.class) ;
                                                break;
                                        }

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
        });
    }
}
