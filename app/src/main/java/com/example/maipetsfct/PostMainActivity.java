package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PostMainActivity extends AppCompatActivity {

    private ImageView newUser;
    private ImageView alreadyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        //Localizo las imagenes que actuarán como botones
        newUser = findViewById(R.id.newuser);
        alreadyUser = findViewById(R.id.alreadyuser);

        //Creo las intenciones de ambos
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUser = new Intent(PostMainActivity.this,NewUserActivity.class);
                startActivity(newUser);
            }
        });

        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alreadyUser = new Intent(PostMainActivity.this,LoginActivity.class);
                startActivity(alreadyUser);
            }
        });
    }
}
