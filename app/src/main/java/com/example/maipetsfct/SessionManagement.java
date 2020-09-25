package com.example.maipetsfct;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.maipetsfct.models.Usuario;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String SESSION_MAIL = "session_mail";
    String SESSION_PASS = "session_pass";
    String id, name, mail, pass;

    public SessionManagement (Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Usuario user) {

        // Guarda sesion de usuario cuando se logea
        String codify = user.getCodigo();
        id = user.getUid();
        mail = user.getEmail();
        editor.putString(SESSION_MAIL,mail);
        pass = user.getContrasena();
        editor.putString(SESSION_PASS,pass);
        switch (codify){
            case "fam":
                name = user.getNombre();
                editor.putString(SESSION_KEY,name).commit();
                break;
            case "ser":
                name = user.getRazon();
                editor.putString(SESSION_KEY,name).commit();
                break;
        }
    }

    public String getSession () {
        // Devuelve el id de usuario cuya sesión está guardada
        return sharedPreferences.getString(SESSION_KEY, "Usuario");
    }
    public String getMail () {
        // Devuelve el id de usuario cuya sesión está guardada
        return sharedPreferences.getString(SESSION_MAIL, "Mail");
    }
    public String getPass () {
        // Devuelve el id de usuario cuya sesión está guardada
        return sharedPreferences.getString(SESSION_PASS, "Password");
    }

    public void removeSession () {
        editor.putString(SESSION_KEY, "Usuario").commit();
    }
}
