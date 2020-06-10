package com.maipetsfct.popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maipetsfct.R;

public class Pop_spr_det extends Activity {
    private ImageView imagen;
    private TextView titulo, informacion;
    private String tit,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info_s);

        imagen = findViewById(R.id.imgDescInfo);
        titulo = findViewById(R.id.nameServInfo);
        informacion = findViewById(R.id.InfoServ);

        Bundle info = getIntent().getExtras();
        tit = info.getString("titulo");
        desc = info.getString("desc");

        // Asignamos los campos para mostrarlos
        imagen.setImageResource(R.drawable.info_);
        titulo.setText(tit);
        informacion.setText(desc);


        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.85),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);
    }
}
