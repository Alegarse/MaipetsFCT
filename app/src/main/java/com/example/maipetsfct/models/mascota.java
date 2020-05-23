package com.example.maipetsfct.models;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

public class mascota implements Serializable {

    private Uri urlImage;
    private String nombre;
    private String tipo;
    private String raza;
    private String color;
    private String fechaNac;

    public mascota() {
    }

    public mascota(String nombre, String tipo, String raza, String color, String fechaNac) {
        //this.imagen = imagen;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.fechaNac = fechaNac;
    }

    public mascota(Uri urlImage,String nombre, String tipo, String raza, String color, String fechaNac) {
        this.urlImage = urlImage;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.fechaNac = fechaNac;
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

    public Uri getUrlImage() { return urlImage; }

    public void setUrlImage(Uri urlImage) { this.urlImage = urlImage; }

    @Override
    public String toString() {
        return "Mascota: " + tipo + ": " + nombre + "de raza " + raza;
    }
}
