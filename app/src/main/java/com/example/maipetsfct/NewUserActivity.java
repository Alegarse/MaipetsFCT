package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.maipetsfct.registros.FamilyRegister;
import com.example.maipetsfct.registros.VetRegister;

public class NewUserActivity extends AppCompatActivity {

    private ImageView familyPets;
    private ImageView serPets;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        //Localizo las imagenes que actuarán como botones
        familyPets = findViewById(R.id.familyuser);
        serPets = findViewById(R.id.seruser);

        //Creo las intenciones para su redirección
        familyPets.setOnClickListener(v -> {
            Intent familyRegister = new Intent(NewUserActivity.this, FamilyRegister.class);
            startActivity(familyRegister);
        });

        serPets.setOnClickListener(v -> {
            Intent vetRegister = new Intent(NewUserActivity.this, VetRegister.class);
            startActivity(vetRegister);
        });
    }
}
