package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NewUserActivity extends AppCompatActivity {

    private ImageView familyPets;
    private ImageView vetPets;
    private ImageView anotherPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        //Localizo las imagenes que actuarán como botones
        familyPets = findViewById(R.id.familyuser);
        vetPets = findViewById(R.id.vetuser);
        anotherPets = findViewById(R.id.anotheruser);

        //Creo las intenciones para su redirección
        familyPets.setOnClickListener(v -> {
            Intent familyRegister = new Intent(NewUserActivity.this, FamilyRegister.class);
            startActivity(familyRegister);
        });

        vetPets.setOnClickListener(v -> {
            Intent vetRegister = new Intent(NewUserActivity.this, VetRegister.class);
            startActivity(vetRegister);
        });

        anotherPets.setOnClickListener(v -> {
            Intent anotherRegister = new Intent(NewUserActivity.this, AnotherServRegister.class);
            startActivity(anotherRegister);
        });
    }
}
