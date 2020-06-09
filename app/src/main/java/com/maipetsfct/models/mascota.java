package com.maipetsfct.models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class mascota implements Serializable {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private String urlImage;
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

    public mascota(String urlImage,String codigo, String nombre, String tipo, String raza, String color, String fechaNac) {
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "Mascota: " + tipo + ": " + nombre + "de raza " + raza;
    }
}
