package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class VetActivity extends AppCompatActivity {

    private BottomNavigationView bnv;

    // Link Web de prueba para enlace a info (web final)
    private String url = "https://wcbkwldo.lucusprueba.es/";

    // Conexión con firebase
    private FirebaseAuth fbauth ;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bnv = findViewById(R.id.bottom_navigation);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        NavController nc = Navigation.findNavController(this,R.id.fragmentVet);

        AppBarConfiguration abc = new AppBarConfiguration
                .Builder(R.id.perfil, R.id.serv)
                .build();

        NavigationUI.setupActionBarWithNavController(this,nc,abc);

        NavigationUI.setupWithNavController(bnv,nc);
    }

    // Menú de la barra de acción
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.menu_up, menu) ;
        //
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuLogout :
                // cerramos la sesión en FireBase
                fbauth.signOut() ;
                // volvemos a la actividad principal
                setResult(0);
                finish();
                return true;

            case R.id.info:
                Uri uri = Uri.parse(url);
                Intent info = new Intent (Intent.ACTION_VIEW, uri);
                startActivity(info);
                break;

            /*
            case R.id.mShare:

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(android.content.Intent.EXTRA_TEXT,"https://www.linkedin.com/in/alegarse/");
                startActivity(Intent.createChooser(share,"Compartir via"));
                break;
             */
        }

        return super.onOptionsItemSelected(item);
    }
}
