package com.example.maipetsfct.models;

import java.io.Serializable;

public class Usuario implements Serializable {

    // Variales de usuario
    private String nombre;
    private String razon;
    private String direccion;
    private String telefono;
    private String actividad;
    private String apellidos;
    private String email;
    private String contrasena;
    private String codigo;

    // Contructor vacío para Firebase
    public Usuario(){}

    // Constructores para los diferentes tipos de usuario
    // Usuario tipo familia
    public Usuario(String nombre, String apellidos, String email, String contra, String code) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasena =  contra;
        this.codigo = code;
    }
    public Usuario(String nombre, String apellidos, String email, String contra) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasena = contra;
    }
    //Usuario tipo servicio
    public Usuario(String actividad,String razon, String direccion, String telefono, String email, String contrasena, String code) {
        this.actividad = actividad;
        this.razon = razon;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.codigo = code;
    }
    public Usuario(String actividad,String razon, String direccion, String telefono, String email, String contrasena) {
        this.actividad = actividad;
        this.razon = razon;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
    }




    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
// ToString

    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }
}
