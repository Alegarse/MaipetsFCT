package com.example.maipetsfct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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

    private TextView mFecha;
    private TextView mHora;
    private ImageButton get_fecha,get_hora;
    private Button sacarCita;
    private String razonSoc;

    // Necesarios para la notificacion
    private NotificationManager nm;
    private final String CHANNEL_ID = "Channel1";
    private NotificationCompat.Builder not;
    private final int NOTIFICATION_ID = 111;

    // PAra el sonido personalizado de notificación
    private Uri uri;

    // Para versiones de API >= 26
    private NotificationChannel ch;
    private final String CHANNEL_NAME = "Temporizador";



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

        //Definimos la ruta hacia el archivo de audio de la notificación
        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.pipes);

        // Para tester
        mHora.setText("hora");
        mFecha.setText("fecha");

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
                final String horaCita,fechaCita,nombreCita;

                    if (razonSoc == null){
                        Snackbar.make(v, getResources().getText(R.string.pChoBus), Snackbar.LENGTH_LONG).show();
                        return ;
                    } else {
                        nombreCita = razonSoc;
                    }
                    if (mFecha.toString().equals("fecha")){
                        Snackbar.make(v, getResources().getText(R.string.sFecha), Snackbar.LENGTH_LONG).show();
                        return ;
                    } else {
                        fechaCita = mFecha.getText().toString();
                    }
                    if (mHora.toString().equals("hora")){
                        Snackbar.make(v, getResources().getText(R.string.sHora), Snackbar.LENGTH_LONG).show();
                        return ;
                    } else {
                        horaCita = mHora.getText().toString();
                    }


                final String nombreMascota = nombreMasc;
                final String ident = uid;

                Cita cita = new Cita(nombreCita,fechaCita,horaCita,nombreMascota,ident,cUid);
                DatabaseReference dbref = fbdatabase.getReference("citas/"+cUid);
                dbref.setValue(cita);

                // Ahora, una vez guardada, creamos la notificación  ////////////////////////

                // Obtenenmos referencia al servicio de notificaciones de Android
                nm =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                // Solo si se usa version API >= 26
                createNotificationChannel();

                // Ahora creamos la notificación
                not = new NotificationCompat.Builder(ServDispActivity.this, CHANNEL_ID);
                not.setSmallIcon(R.drawable.ic_bell); // Android me obliga a definir un icono
                not.setContentTitle(getText(R.string.notiTitle));
                not.setContentText(getText(R.string.notiDesc1) + " "+ nombreMascota);
                not.setSound(uri);
                not.setPriority(NotificationCompat.PRIORITY_HIGH);

                //Ahora una vez configurada enviamos la notificacion a través del canal elegido
                nm.notify(NOTIFICATION_ID, not.build());

                setResult(RESULT_OK);
                finish();
                return;
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

    // Comprueba si la aplicacioón esta corriendo en una version de API => 26,
    // en cuyo caso tendremos que crear el canal de comunicacion.
    private void createNotificationChannel ()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // Definimos los atributos de audio
            AudioAttributes att = new AudioAttributes.Builder()
                    .setContentType((AudioAttributes.CONTENT_TYPE_SONIFICATION))
                    .setUsage((AudioAttributes.USAGE_NOTIFICATION))
                    .build();

            // Creamos nuestro canal de comunicacion
            ch = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH );

            ch.setDescription(CHANNEL_NAME);

            // Asociamos el audio al canal
            ch.setSound(uri,att);

            // Creamos el canal
            nm.createNotificationChannel(ch);

            // Verlo con la pantalla bloqueada
            ch.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
    }
}
