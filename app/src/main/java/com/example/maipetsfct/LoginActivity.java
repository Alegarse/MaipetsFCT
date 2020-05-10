package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private FirebaseAuth mAuth ;
    private FirebaseDatabase fbdatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance() ;

        // Referenciamos los campos y botones
        email = findViewById(R.id.userEmailLogin);
        password = findViewById(R.id.userPassLogin);

        login = findViewById(R.id.btnLogin);
        cancel = findViewById(R.id.btnCancel);

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
                                        // Obtenemos los datos del usuario logeado
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class) ;

                                        // creamos un diccionario para poner los datos del usuario
                                        Bundle bundle = new Bundle() ;
                                        bundle.putSerializable("_usuario", usuario) ;

                                        // Pasamos a la actividad de usuario
                                        Intent intent = new Intent(LoginActivity.this, UsersActivity.class) ;

                                        // Almacenamos información en la intencion
                                        intent.putExtras(bundle) ;

                                        // Lanzamos la intención
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
