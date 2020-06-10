package com.example.maipetsfct.popups;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maipetsfct.R;
import com.example.maipetsfct.adapters.sgeAdapter;
import com.example.maipetsfct.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Pop_com extends Activity {

    private ImageView imgUsuario;
    private TextView nombreRUsu, emailRUsu, telRUsu;
    private TextView emailUsu, telUsu;
    private ImageButton botonLlamada, botonMail;

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference, ref;

    // Colección de servicios de usuarios
    ArrayList<Usuario> usuarios;
    RecyclerView recyclerView;
    sgeAdapter usuarioAdapter;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_com);

        // Instanciamos datos de ficha
        emailUsu = findViewById(R.id.emailUsu);
        telUsu = findViewById(R.id.teleUsu);

        // Instanciamos datos reales usuario
        imgUsuario = findViewById(R.id.imgUsu);
        nombreRUsu = findViewById(R.id.nombreRUsu);
        emailRUsu = findViewById(R.id.emailMUsu);
        telRUsu = findViewById(R.id.teleMUsu);

        // Instanciamos botones de llamada y envío email
        botonLlamada = findViewById(R.id.botonLlamada);
        botonMail = findViewById(R.id.botonMail);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance();

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase = FirebaseDatabase.getInstance();

        // LISTADO DE USUARIOS
        recyclerView = findViewById(R.id.listUsus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usuarios = new ArrayList<Usuario>();

        ref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarios.clear();
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Usuario u = data1.getValue(Usuario.class);
                    usuarios.add(u);
                }
                usuarioAdapter = new sgeAdapter(Pop_com.this, usuarios);
                usuarioAdapter.setUsuarios(usuarios);
                usuarioAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));

                        if (user.getCodigo().equals("fam")) {

                            if (user.getUrlImage().equals("empty")) {
                                imgUsuario.setImageResource(R.drawable.profile);
                            } else {
                                Picasso.get().load(user.getUrlImage()).into(imgUsuario);
                            }
                            nombreRUsu.setText(user.getNombre() + " " + user.getApellidos());
                            emailUsu.setText(R.string.mail2p);
                            emailRUsu.setText(user.getEmail());
                            telUsu.setText(R.string.telef2p);
                            telRUsu.setText(R.string.noData);
                            botonLlamada.setClickable(false);
                            botonMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String mail = user.getEmail();
                                    sendEmail(mail);
                                }
                            });
                        } else {
                            if (user.getUrlImage().equals("empty")) {
                                imgUsuario.setImageResource(R.drawable.profile);
                            } else {
                                Picasso.get().load(user.getUrlImage()).into(imgUsuario);
                            }
                            nombreRUsu.setText(user.getRazon());
                            emailUsu.setText(R.string.mail2p);
                            emailRUsu.setText(user.getEmail());
                            telUsu.setText(R.string.telef2p);
                            telRUsu.setText(user.getTelefono());
                            botonLlamada.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String phone = user.getTelefono();
                                    Intent marcar = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                                    startActivity(marcar);
                                }
                            });
                            botonMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String mail = user.getEmail();
                                    sendEmail(mail);
                                }
                            });
                        }
                    }
                });
                recyclerView.setAdapter(usuarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.s_noadd, Toast.LENGTH_LONG).show();
            }
        });
    }
    // Para enviar email
    public void sendEmail(String email) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));

        try {
            startActivity(Intent.createChooser(emailIntent, "Envío email"));
            finish();
            Log.e("Test email:", "Fin envio email");

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Pop_com.this, getText(R.string.err), Toast.LENGTH_SHORT).show();
        }
    }
}
