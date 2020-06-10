package com.maipetsfct.registros;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.maipetsfct.LoginActivity;
import com.example.maipetsfct.R;
import com.maipetsfct.models.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VetRegister extends AppCompatActivity {

    public Button btnCan, btnReg;
    private EditText razSoc, dire, telef, pass, conf_pass;
    private EditText email;
    private Spinner serv;
    private String cod = "ser";
    private String servCode,uid;
    private String ruta = "empty";

    // Para realizar el registro
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase ;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        // Instanciamos
        btnCan = findViewById(R.id.regBtnCancel);
        btnReg = findViewById(R.id.regBtnRegister);
        razSoc= findViewById(R.id.regRazSoc);
        dire = findViewById(R.id.regDir);
        telef = findViewById(R.id.regTelf);
        email = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPassword);
        conf_pass = findViewById(R.id.regPassConfirm);

        // Código para el spinner de eleccion de tipo de servicio
        serv = findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,R.array.servicios,R.layout.support_simple_spinner_dropdown_item);
        serv.setAdapter(adaptador);

        // Escuchador para el botón Cancelar
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelamos y volvemos al principal
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        });

        // Inicializamos Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance();

        // Escuchador para el botón Registro
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cogemos el valor de los campos del formulario de registro
                final String ser = serv.getSelectedItem().toString().trim();
                // Para meter el codigo del tipo de servicio
                switch (serv.getSelectedItemPosition()){
                    case 1:
                        servCode = "1";
                        break;
                    case 2:
                        servCode = "2";
                        break;
                    case 3:
                        servCode = "3";
                        break;
                    case 4:
                        servCode = "4";
                        break;
                    case 5:
                        servCode = "5";
                        break;
                }
                final String raz = getField(razSoc);
                final String dir = getField(dire);
                final String tel = getField(telef);
                final String ema = getField(email);
                final String pwd = getField(pass);
                String cpwd = getField(conf_pass);

                // Verificamos que se han introducido todos los campos
                if (ema.isEmpty() || raz.isEmpty() || dir.isEmpty() || tel.isEmpty() ||
                        ser.isEmpty() || pwd.isEmpty() || cpwd.isEmpty())
                {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                }
                // Verificamos si las contraseñas coinciden
                if (!pwd.equals(cpwd))
                {
                    Snackbar.make(v, getResources().getText(R.string.e_pass), Snackbar.LENGTH_LONG).show();
                    return ;
                }

                // Pasamos a registrar en el sistema de autentificación
                mAuth.createUserWithEmailAndPassword(ema,pwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())  // Usuario se registra correctamente
                            {
                                // 1.Obtenemos UID del usuario registrado
                                uid = mAuth.getUid();

                                // 2. Creamos el usuario
                                Usuario usuario = new Usuario(ser,raz,dir,tel,ema,pwd,cod,servCode,ruta,uid);

                                // 3. Obtenemos referencia al documento de usuarios en FB
                                DatabaseReference dbref = fbdatabase.getReference("usuarios");

                                // 4. Guardamos la información en RealTime Database
                                dbref.child(uid).setValue(usuario) ;

                                // 5. Salimos de la aplicación
                                mAuth.signOut();
                                setResult(RESULT_OK);
                                finish();
                                // Tras el registro correcto redirijo a la pagina de login
                                Intent aLogin = new Intent(VetRegister.this, LoginActivity.class);
                                Toast.makeText(getApplicationContext(),getText(R.string.ok_register), Toast.LENGTH_LONG).show();
                                startActivity(aLogin);

                            } else {  // Usuario no se registra correctamente
                                // Aviso de error por pantalla
                                Snackbar.make(v, getResources().getText(R.string.e_register), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
    // Método para obtener texto de los campos
    private String getField(EditText edit)
    {
        return edit.getText().toString().trim() ;
    }
}
