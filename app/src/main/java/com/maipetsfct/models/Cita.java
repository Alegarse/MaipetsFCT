package com.maipetsfct.models;

public class Cita {

    // Variables
    private String nombreCita;
    private String fechaCita;
    private String horaCita;
    private String nombreMascota;
    private String idUsuario;
    private String idCita;

    public Cita() {

    }

    public Cita(String nombreCita, String fechaCita, String horaCita, String nombreMascota, String idUsuario, String idCita) {
        this.nombreCita = nombreCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.nombreMascota = nombreMascota;
        this.idUsuario = idUsuario;
        this.idCita = idCita;
    }

    public String getNombreCita() {
        return nombreCita;
    }

    public void setNombreCita(String nombreCita) {
        this.nombreCita = nombreCita;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    @Override
    public String toString() {
        return "Cita para " + nombreMascota + " en " + nombreCita + " el " + fechaCita + " a las " + horaCita;
    }
}
