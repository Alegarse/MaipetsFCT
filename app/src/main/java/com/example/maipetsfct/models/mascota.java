package com.example.maipetsfct.models;

public class mascota {

    //private int imagen;
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

    /*public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }*/

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

    @Override
    public String toString() {
        return "Mascota: " + tipo + ": " + nombre + "de raza " + raza;
    }
}
