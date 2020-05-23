package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Para ocultar la action bar y que no de problemas con los estilos de Material Design
        getSupportActionBar().hide();

        // Instancio el boton y configuro su listener
        letsGo = findViewById(R.id.go);

        letsGo.setOnClickListener(v -> {
            Intent start = new Intent(MainActivity.this, PostMainActivity.class);
            startActivity(start);
        });
    }
}
