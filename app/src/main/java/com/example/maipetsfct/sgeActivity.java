package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maipetsfct.popups.Pop_db;
import com.example.maipetsfct.popups.Pop_spr;

public class sgeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button sgedb, sgespr, sgecom, sgeedr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sge);

        this.setTitle(R.string.introSge);

        // Instanciamos los elementos
        sgedb = findViewById(R.id.sgedb);
        sgespr = findViewById(R.id.sgespr);
        sgecom = findViewById(R.id.sgecom);
        sgeedr = findViewById(R.id.sgeedr);

        // Llamada el onClickListener
        sgedb.setOnClickListener(this);
        sgespr.setOnClickListener(this);
        sgecom.setOnClickListener(this);
        sgeedr.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sgedb:
                Intent db = new Intent(sgeActivity.this, Pop_db.class);
                startActivity(db);
                break;
            case R.id.sgespr:
                Intent spr = new Intent(sgeActivity.this, Pop_spr.class);
                startActivity(spr);
                break;
            case R.id.sgecom:
                break;
            case R.id.sgeedr:
                break;
        }

    }
}
