package com.example.maipetsfct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.maipetsfct.fragments.MyDatePicker;

import java.util.Calendar;

public class ServDispActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mFecha, mHora;
    private ImageButton get_fecha,get_hora;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    // Calendario para fecha y hora
    public final Calendar f = Calendar.getInstance();

    // Variables de fecha y hora
    final int dia = f.get(Calendar.DAY_OF_MONTH);
    final int mes = f.get(Calendar.MONTH);
    final int anio = f.get(Calendar.YEAR);
    final int hora = f.get(Calendar.HOUR_OF_DAY);
    final int minuto = f.get(Calendar.MINUTE);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv_disp);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle recuperar = getIntent().getExtras();


        // Instanciamos los elementos que intervienen
        mFecha = findViewById(R.id.muestra_fecha);
        mHora = findViewById(R.id.muestra_hora);
        get_fecha = findViewById(R.id.coge_fecha);
        get_hora = findViewById(R.id.coge_hora);


        // Eventos onCLick para fecha y hora
        get_fecha.setOnClickListener(this);
        get_hora.setOnClickListener(this);

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
