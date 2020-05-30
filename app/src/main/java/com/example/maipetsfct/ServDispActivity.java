package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ServDispActivity extends AppCompatActivity {

    private TextView uno,dos,tres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv_disp);

        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        tres = findViewById(R.id.tres);

        Bundle recuperar = getIntent().getExtras();
        uno.setText(recuperar.getString("nombreMasc"));
        dos.setText(recuperar.getString("actividad"));
        tres.setText(recuperar.getString("mail"));
    }
}
