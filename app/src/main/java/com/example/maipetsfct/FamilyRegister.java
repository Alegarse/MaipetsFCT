package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FamilyRegister extends AppCompatActivity {

    public Button btnCan, btnReg;
    private EditText nombre, apellidos, pass, conf_pass;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_register);

        // Instanciamos
        btnCan = findViewById(R.id.regBtnCancel);
        btnReg = findViewById(R.id.regBtnRegister);
        nombre= findViewById(R.id.regName);
        apellidos = findViewById(R.id.regSurname);
        email = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPassword);
        conf_pass = findViewById(R.id.regPassConfirm);
    }
    // Método para obtener texto de los campos
    private String getField(EditText edit)
    {
        return edit.getText().toString().trim() ;
    }
}
