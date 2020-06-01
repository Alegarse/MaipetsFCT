package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maipetsfct.adapters.UsuarioAdapter2;
import com.example.maipetsfct.models.Cita;
import com.example.maipetsfct.models.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ServDispActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mFecha, mHora;
    private ImageButton get_fecha,get_hora;
    private Button sacarCita;
    private String razonSoc;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    // Calendario para fecha y hora
    public final Calendar f = Calendar.getInstance();

    // Variables de fecha y hora
    int dia = f.get(Calendar.DAY_OF_MONTH);
    final int mes = f.get(Calendar.MONTH);
    int anio = f.get(Calendar.YEAR);
    final int hora = f.get(Calendar.HOUR_OF_DAY);
    final int minuto = f.get(Calendar.MINUTE);

    // Colección de negocios
    RecyclerView recyclerView;
    ArrayList<Usuario> usuarios;
    UsuarioAdapter2 usuarioAdapter;

    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference ref;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv_disp);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle recuperar = getIntent().getExtras();
        String nombreMasc = recuperar.getString("nombreMasc");
        String mail = recuperar.getString("mail");
        String servCode = recuperar.getString("servCode");


        // Instanciamos los elementos que intervienen
        sacarCita = findViewById(R.id.sacaCita);
        mFecha = findViewById(R.id.muestra_fecha);
        mHora = findViewById(R.id.muestra_hora);
        get_fecha = findViewById(R.id.coge_fecha);
        get_hora = findViewById(R.id.coge_hora);

        sacarCita.setText(getText(R.string.dateFor) + " " + nombreMasc);


        // Eventos onCLick para fecha y hora
        get_fecha.setOnClickListener(this);
        get_hora.setOnClickListener(this);

        //Obtenemos conexiones Firebase
        fbauth = FirebaseAuth.getInstance() ;
        fbdatabase =  FirebaseDatabase.getInstance();
        String uid = fbauth.getCurrentUser().getUid();

        // ZONA CARDVIEW PARA SELECCIONAR NEGOCIO PARA LA CITA

        recyclerView = findViewById(R.id.chooseServ);

        // Para poner el scroll horizontal
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(llm);

        usuarios = new ArrayList<Usuario>();

        ref = FirebaseDatabase.getInstance().getReference().child("usuarios");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    Usuario u = datos.getValue(Usuario.class);

                    // Para que solo muestre los negocios de este tipo
                    if (u.getActividad() != null) {
                        if(u.getServCode().equals(servCode)) {
                            usuarios.add(u);
                        }
                    }
                }

                usuarioAdapter = new UsuarioAdapter2(getApplicationContext(),usuarios);
                usuarioAdapter.setUsuarios(usuarios);

                usuarioAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));
                        razonSoc = usuarios.get(recyclerView.getChildAdapterPosition(v)).getRazon();
                        Toast.makeText(getApplicationContext(),getText(R.string.sel)+" " + razonSoc, Toast.LENGTH_LONG).show();
                    }
                });

                recyclerView.setAdapter(usuarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Escuchador crear cita
        sacarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cUid = UUID.randomUUID().toString();

                // Valor de los campos
                final String nombreCita = razonSoc.trim();
                final String fechaCita = mFecha.getText().toString().trim();
                final String horaCita = mHora.getText().toString().trim();
                final String nombreMascota = nombreMasc.trim();
                final String ident = uid.trim();

                //Verificamos que los campos contienen información
                if (nombreCita.isEmpty() || fechaCita.isEmpty() || horaCita.isEmpty() || nombreMascota.isEmpty() || ident.isEmpty())
                {
                    Snackbar.make(v, getResources().getText(R.string.e_empty), Snackbar.LENGTH_LONG).show();
                    return ;
                } else {
                    Cita cita = new Cita(nombreCita,fechaCita,horaCita,nombreMascota,ident,cUid);
                    DatabaseReference dbref = fbdatabase.getReference("citas/"+cUid);
                    dbref.setValue(cita);

                    Intent volver = new Intent(ServDispActivity.this,UsersActivity.class);
                    startActivity(volver);

                    //setResult(RESULT_OK);
                    //finish();
                    //return;
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coge_fecha:
                obtenerFecha();
                break;
            case R.id.coge_hora:
                obtenerHora();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                mFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Muestro la hora con el formato deseado
                mHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, true);

        recogerHora.show();
    }
}
