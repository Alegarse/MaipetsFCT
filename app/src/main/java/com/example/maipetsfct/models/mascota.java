package com.example.maipetsfct.models;

import android.net.Uri;
import android.widget.ImageView;

import com.example.maipetsfct.adapters.GetDownUrl;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class mascota implements Serializable {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private Uri urlImage;
    private String codigo;
    private String nombre;
    private String tipo;
    private String raza;
    private String color;
    private String fechaNac;

    public mascota() {
    }

    public mascota(String codigo,String nombre, String tipo, String raza, String color, String fechaNac) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.fechaNac = fechaNac;
    }

    public mascota(Uri urlImage,String codigo, String nombre, String tipo, String raza, String color, String fechaNac) {
        this.urlImage = urlImage;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.fechaNac = fechaNac;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Uri getUrlImage(String codigo) {

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;
        String uid = FirebaseAuth.getInstance().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();

        if (mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_" + codigo + ".jpg") != null) {

            StorageReference profileImgPath = mStorage.child("images").child(mAuth.getCurrentUser().getUid()).child("PetImg_" + codigo + ".jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(uri -> {
                urlImage = uri;
            });
        }
        return urlImage;
    }

    public void setUrlImage(Uri urlImage) { this.urlImage = urlImage; }

    @Override
    public String toString() {
        return "Mascota: " + tipo + ": " + nombre + "de raza " + raza;
    }
}
