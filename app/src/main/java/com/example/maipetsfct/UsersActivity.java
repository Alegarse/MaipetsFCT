package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsersActivity extends AppCompatActivity {

    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        bnv = findViewById(R.id.bottom_navigation);

        NavController nc = Navigation.findNavController(this,R.id.fragmentTab);

        AppBarConfiguration abc = new AppBarConfiguration
                .Builder(R.id.perfil, R.id.masc, R.id.serv)
                .build();

        NavigationUI.setupActionBarWithNavController(this,nc,abc);

        NavigationUI.setupWithNavController(bnv,nc);
    }
}
