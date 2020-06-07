package com.example.maipetsfct.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maipetsfct.MainActivity;
import com.example.maipetsfct.R;
import com.example.maipetsfct.adapters.sgeAdapter;
import com.example.maipetsfct.models.Cita;
import com.example.maipetsfct.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Pop_db extends Activity {

    private ImageView imgUsuario;
    private TextView nombreRUsu, tipoRUsu, emailRUsu, telRUsu;
    private TextView tipoUsu,emailUsu, telUsu;
    private Button borraUser;
    private String uidUser;

    private FirebaseAuth fbauth ;
    private FirebaseDatabase fbdatabase;
    DatabaseReference reference,ref;
    FirebaseUser usuario;

    // Colección de servicios de usuarios
    ArrayList<Usuario> usuarios;
    ArrayList<Cita> citas;
    RecyclerView recyclerView;
    sgeAdapter usuarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_db);

        // Instanciamos datos de ficha
        tipoUsu = findViewById(R.id.tipoUsu);
        emailUsu = findViewById(R.id.emailUsu);
        telUsu = findViewById(R.id.teleUsu);

        borraUser = findViewById(R.id.delAdminProf);


        // Instanciamos datos reales usuario
        imgUsuario = findViewById(R.id.imgUsu);
        nombreRUsu = findViewById(R.id.nombreRUsu);
        tipoRUsu = findViewById(R.id.tipoRUsu);
        emailRUsu = findViewById(R.id.emailRUsu);
        telRUsu = findViewById(R.id.teleRUsu);

        //Obtenemos la instancia de FirebaseAuth
        fbauth = FirebaseAuth.getInstance() ;

        //Obtenemos la instancia de FirebaseDatabase
        fbdatabase =  FirebaseDatabase.getInstance();

        // LISTADO DE USUARIOS
        recyclerView = findViewById(R.id.listUsus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuarios = new ArrayList<Usuario>();
        citas = new ArrayList<Cita>();

        ref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarios.clear();
                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                    Usuario u = data1.getValue(Usuario.class);
                    usuarios.add(u);
                }

                usuarioAdapter = new sgeAdapter(Pop_db.this,usuarios);
                usuarioAdapter.setUsuarios(usuarios);

                usuarioAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        borraUser.setEnabled(true);
                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));
                        uidUser = user.getUid();
                        if (user.getCodigo().equals("fam")) {
                            if (user.getUrlImage().equals("empty")) {
                                imgUsuario.setImageResource(R.drawable.profile);
                            } else {
                                Picasso.get().load(user.getUrlImage()).into(imgUsuario);
                            }
                            nombreRUsu.setText(user.getNombre() + " " + user.getApellidos());
                            tipoUsu.setText(R.string.tipu);
                            tipoRUsu.setText(R.string.tipuR);
                            emailUsu.setText(R.string.mail2p);
                            emailRUsu.setText(user.getEmail());
                            telUsu.setText(R.string.telef2p);
                            telRUsu.setText(R.string.noData);
                        } else {
                            if (user.getUrlImage().equals("empty")) {
                                imgUsuario.setImageResource(R.drawable.profile);
                            } else {
                                Picasso.get().load(user.getUrlImage()).into(imgUsuario);
                            }
                            nombreRUsu.setText(user.getRazon());
                            tipoUsu.setText(R.string.activity);
                            tipoRUsu.setText(user.getActividad());
                            emailUsu.setText(R.string.mail2p);
                            emailRUsu.setText(user.getEmail());
                            telUsu.setText(R.string.telef2p);
                            telRUsu.setText(user.getTelefono());
                        }

                        // Escuchador para el boton de eliminar al usuario
                        borraUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder myBuild = new AlertDialog.Builder(Pop_db.this);
                                myBuild.setTitle(R.string.cDel);
                                myBuild.setMessage(R.string.yesAdminDel);
                                myBuild.setPositiveButton(R.string.afirmative, (dialogInterface, i) -> {

                                    if (user.getCodigo().equals("fam")) {
                                        //usuario.delete();
                                        ref.child("usuarios").child(uidUser).removeValue();
                                        ref.child("mascotas").child(uidUser).removeValue();
                                        ref.child("citas").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                citas.clear();
                                                for (DataSnapshot data1: dataSnapshot.getChildren()) {
                                                    Cita c = data1.getValue(Cita.class);
                                                    if (c.getIdUsuario().equals(uidUser)){
                                                        ref.child("citas").child(c.getIdCita()).removeValue();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        finish();
                                    }

                                });
                                myBuild.setNegativeButton("No", (dialogInterface, i) ->
                                        dialogInterface.cancel());

                                AlertDialog dialog = myBuild.create();
                                dialog.show();
                            }
                        });

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
}
