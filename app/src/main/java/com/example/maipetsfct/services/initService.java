package com.example.maipetsfct.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.maipetsfct.R;

public class initService extends Service {

    private MediaPlayer media ;

    /**
     */
    @Override
    public void onCreate()
    {
        Log.i("Musica inicial", "Servicio de musica creado") ;
        media = MediaPlayer.create(this, R.raw.beginning) ;
        media.setLooping(false);
        media.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId)
    {
        Log.i("Musica inicial", "Servicio de musica arrancado") ;
        media.start() ;

        // por defecto, cuando la app finaliza, el
        // servicio se reinicia.
        return START_NOT_STICKY ;
    }

    @Override
    public void onDestroy()
    {
        Log.i("Musica inicial", "Servicio de musica parado") ;
        media.stop() ;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
