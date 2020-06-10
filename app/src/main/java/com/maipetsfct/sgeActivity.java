package com.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.maipetsfct.R;
import com.maipetsfct.popups.Pop_com;
import com.maipetsfct.popups.Pop_db;
import com.maipetsfct.popups.Pop_spr;

public class sgeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton sgedb, sgespr, sgecom;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sge);

        this.setTitle(R.string.introSge);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Instanciamos los elementos
        sgedb = findViewById(R.id.sgedb);
        sgespr = findViewById(R.id.sgespr);
        sgecom = findViewById(R.id.sgecom);

        // Llamada el onClickListener
        sgedb.setOnClickListener(this);
        sgespr.setOnClickListener(this);
        sgecom.setOnClickListener(this);
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
                Intent com = new Intent(sgeActivity.this, Pop_com.class);
                startActivity(com);
                break;
        }
    }
}
