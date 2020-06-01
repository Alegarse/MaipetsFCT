package com.example.maipetsfct.popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maipetsfct.R;

public class PopUpInfoS extends Activity {

    private ImageView imagen;
    private TextView titulo, informacion;
    private String servCode,tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info_s);

        imagen = findViewById(R.id.imgDescInfo);
        titulo = findViewById(R.id.nameServInfo);
        informacion = findViewById(R.id.InfoServ);

        Bundle info = getIntent().getExtras();
        servCode = info.getString("servCode");
        tit = info.getString("titulo");

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

        switch (servCode) {
            case "1":
                imagen.setImageResource(R.drawable.clinica);
                titulo.setText(tit);
                informacion.setText(getText(R.string.descCliVet));
                break;
            case "2":
                imagen.setImageResource(R.drawable.peluqueria);
                titulo.setText(tit);
                informacion.setText(getText(R.string.descPelu));
                break;
            case "3":
                imagen.setImageResource(R.drawable.comida);
                titulo.setText(tit);
                informacion.setText(getText(R.string.descAlim));
                break;
            case "4":
                imagen.setImageResource(R.drawable.guarde);
                titulo.setText(tit);
                informacion.setText(getText(R.string.descGuarde));
                break;
            case "5":
                imagen.setImageResource(R.drawable.protectora);
                titulo.setText(tit);
                informacion.setText(getText(R.string.descProtec));
                break;
        }
    }
}
