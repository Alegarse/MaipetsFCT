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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maipetsfct.adapters.ActividadAdapter;
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
import java.util.GregorianCalendar;
import java.util.UUID;

public class PopSelectAct extends AppCompatActivity implements View.OnClickListener {

    private TextView mFecha;
    private TextView mHora;
    private ImageButton get_fecha,get_hora;
    private Button sacarCita;
    private String razonSoc,horaCita,fechaCita,nombreCita,nombreMasc;

    // Calendario para fecha y hora
    public final Calendar f = Calendar.getInstance();

    // Variables de fecha y hora
    int dia = f.get(Calendar.DAY_OF_MONTH);
    final int mes = f.get(Calendar.MONTH);
    int anio = f.get(Calendar.YEAR);
    final int hora = f.get(Calendar.HOUR_OF_DAY);
    final int minuto = f.get(Calendar.MINUTE);

    //Variables para event Calendar
    int diaC,mesC,anioC,horaC,minC,calId;

    // Variables de seteo de fecha y hora
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    // Variables necesarios para la notificacion
    private NotificationManager nm;
    private final String CHANNEL_ID = "Channel1";
    private NotificationCompat.Builder not;
    private final int NOTIFICATION_ID = 111;

    // Para el sonido personalizado de notificación
    private Uri uri;

    // Para versiones de API >= 26
    private NotificationChannel ch;
    private final String CHANNEL_NAME = "Temporizador";

    // Recyclerviews, listas y adaptadores
    RecyclerView recyclerView, recyclerView2;
    ArrayList<Usuario> usuarios,usuarios2;
    ArrayList<String> repes = new ArrayList<>();
    ActividadAdapter actividadAdapter;
    UsuarioAdapter2 usuarioAdapter;

    // Conexión Firebase
    private FirebaseAuth fbauth;
    private FirebaseDatabase fbdatabase;
    DatabaseReference ref;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_select);

        this.setTitle(R.string.selecAct);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle datosCitaServ = getIntent().getExtras();
        nombreMasc = datosCitaServ.getString("nombreMasc");

        // Instanciamos los elementos que intervienen
        sacarCita = findViewById(R.id.sacaCita);
        mFecha = findViewById(R.id.muestra_fecha);
        mHora = findViewById(R.id.muestra_hora);
        get_fecha = findViewById(R.id.coge_fecha);
        get_hora = findViewById(R.id.coge_hora);

        //Definimos la ruta hacia el archivo de audio de la notificación
        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.pipes);

        sacarCita.setText(getText(R.string.dateFor) + " " + nombreMasc);

        // Eventos onCLick para fecha y hora
        get_fecha.setOnClickListener(this);
        get_hora.setOnClickListener(this);

        calId =(int) Math.random() * 100000;

        //Obtenemos conexiones Firebase
        fbauth = FirebaseAuth.getInstance() ;
        fbdatabase =  FirebaseDatabase.getInstance();
        String uid = fbauth.getCurrentUser().getUid();

        // ZONA DEL CARDVIEW DE SERVICIOS DIPONIBLES CLICKABLES
        recyclerView = findViewById(R.id.servDispCit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuarios = new ArrayList<Usuario>();

        ref = FirebaseDatabase.getInstance().getReference().child("usuarios");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    Usuario u = datos.getValue(Usuario.class);

                    // Para que no muestre actividades repetidas
                    if (u.getActividad() != null) {
                        if(repes.contains(u.getServCode())) {
                        } else {
                            usuarios.add(u);
                            repes.add(u.getServCode());
                        }
                    }
                }
                actividadAdapter = new ActividadAdapter(getApplicationContext(),usuarios);
                actividadAdapter.setUsuarios(usuarios);
                actividadAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Usuario user = usuarios.get(recyclerView.getChildAdapterPosition(v));
                        Toast.makeText(getApplicationContext(),"Selected: "+ usuarios.get(recyclerView.getChildAdapterPosition(v)).getActividad(), Toast.LENGTH_SHORT).show();
                        String servCode = user.getServCode();

                        // RELLENO DATOS DEL SEGUNDO RECYCLER
                        recyclerView2 = findViewById(R.id.chooseServ);
                        LinearLayoutManager llm = new LinearLayoutManager(PopSelectAct.this);
                        llm.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView2.setLayoutManager(llm);

                        usuarios2 = new ArrayList<Usuario>();
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usuarios2.clear();
                                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                                    Usuario u = datos.getValue(Usuario.class);

                                    // Para que solo muestre los negocios de este tipo
                                    if (u.getActividad() != null) {
                                        if(u.getServCode().equals(servCode)) {
                                            usuarios2.add(u);
                                        }
                                    }
                                }
                                usuarioAdapter = new UsuarioAdapter2(getApplicationContext(),usuarios2);
                                usuarioAdapter.setUsuarios(usuarios2);
                                usuarioAdapter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Usuario user = usuarios2.get(recyclerView.getChildAdapterPosition(v));
                                        razonSoc = usuarios2.get(recyclerView.getChildAdapterPosition(v)).getRazon();
                                        Toast.makeText(getApplicationContext(),getText(R.string.sel)+" " + razonSoc, Toast.LENGTH_LONG).show();
                                    }
                                });
                                recyclerView2.setAdapter(usuarioAdapter);
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
                                // Verificamos que se seleccionen todos los campos
                                // Nombre del negocio
                                if (razonSoc == null){
                                    Snackbar.make(v, getResources().getText(R.string.pChoBus), Snackbar.LENGTH_LONG).show();
                                    return ;
                                } else {
                                    nombreCita = razonSoc;
                                }
                                // Fecha de la cita
                                if (fechaCita == null) {
                                    Snackbar.make(v, getResources().getText(R.string.sFecha), Snackbar.LENGTH_LONG).show();
                                    return;
                                } else {
                                    fechaCita = mFecha.getText().toString();
                                }
                                // Hora de la cita
                                if (horaCita == null){
                                    Snackbar.make(v, getResources().getText(R.string.sHora), Snackbar.LENGTH_LONG).show();
                                    return ;
                                } else {
                                    horaCita = mHora.getText().toString();
                                }

                                final String nombreMascota = nombreMasc;
                                final String ident = uid;

                                // Creamos la cita en la BBDD
                                Cita cita = new Cita(nombreCita,fechaCita,horaCita,nombreMascota,ident,cUid);
                                DatabaseReference dbref = fbdatabase.getReference("citas/"+cUid);
                                dbref.setValue(cita);
                                setResult(RESULT_OK);
                                finish();

                                // Ponemos la cita en el calendario del usuario
                                addEvent(getText(R.string.citaPara)+ " "+ nombreMascota,nombreCita);

                                // Ahora, una vez guardada, creamos la notificación  ////////////////////////
                                // Obtenenmos referencia al servicio de notificaciones de Android
                                nm =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                                // Solo si se usa version API >= 26
                                createNotificationChannel();

                                // Ahora creamos la notificación
                                not = new NotificationCompat.Builder(PopSelectAct.this, CHANNEL_ID);
                                not.setSmallIcon(R.drawable.ic_bell); // Android me obliga a definir un icono
                                not.setContentTitle(getText(R.string.notiTitle));
                                not.setContentText(getText(R.string.notiDesc1) + " "+ nombreMascota + " " + getText(R.string.notiDesc2));
                                not.setSound(uri);
                                not.setPriority(NotificationCompat.PRIORITY_HIGH);

                                //Ahora una vez configurada enviamos la notificacion a través del canal elegido
                                nm.notify(NOTIFICATION_ID, not.build());

                                // Volvemos atrás
                                Intent volver = new Intent(PopSelectAct.this,CitasActivity.class);
                                // Para poder volver necesita datos para el bundle de esa activity
                                volver.putExtra("nombre",nombreMascota);
                                startActivity(volver);
                            }
                        });
                        }
                });
                recyclerView.setAdapter(actividadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        registerForContextMenu(recyclerView);
    }

    //Para agregar la cita al calendario del movil
    public void addEvent (String titulo, String ubic){

        // Creamos la intención
        Intent agregaCita = new Intent(Intent.ACTION_INSERT);
        agregaCita.setData(CalendarContract.Events.CONTENT_URI);

        //Rellenamos con datos
        agregaCita.setType("vnd.android.cursor.item/event");
        agregaCita.putExtra(Events.CALENDAR_ID,calId);
        agregaCita.putExtra(Events.TITLE, titulo);
        agregaCita.putExtra(Events.EVENT_LOCATION, ubic);
        agregaCita.putExtra(Events.DESCRIPTION, getText(R.string.citaPara)+" "+nombreMasc+" en "+ubic);

        //Preparamos la fecha y hora (Duración 1/2 hora por defecto)
        GregorianCalendar calDate = new GregorianCalendar(anioC, mesC, diaC);
        long beginTime = calDate.getTimeInMillis() +(((horaC*3600)+(minC)*60))*1000;
        agregaCita.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime);
        agregaCita.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,beginTime+1800000);

        // Configuración del evento como ocupado y privado
        agregaCita.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
        agregaCita.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
        startActivity(agregaCita);
    }

    // Eventos onClick para seleccion fecha/hora
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

    // Uso de widget para seleccionar fecha
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
                diaC = dayOfMonth;
                mesC = month;
                anioC = year;
            }
        },anio, mes, dia);
        fechaCita = mFecha.toString();
        //Muestro el widget
        recogerFecha.show();

    }

    // Uso de widget para seleccionar hora
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
                horaC = hourOfDay;
                minC = minute;
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, true);
        horaCita = mHora.toString();
        recogerHora.show();
    }

    // Extra para la notificación al crearse la cita
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
