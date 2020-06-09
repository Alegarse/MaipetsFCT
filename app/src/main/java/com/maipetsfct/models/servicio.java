package com.maipetsfct.models;

import java.io.Serializable;

public class servicio implements Serializable {

    // Variales del servicio
    private String sUid;
    private String uid;
    private String nombre;
    private String desc;

    // Contructor vacío para Firebase
    public servicio() { }

    public servicio(String sUid,String uid,String nombre, String desc) {
        this.sUid = sUid;
        this.uid = uid;
        this.nombre = nombre;
        this.desc = desc;
    }

    public servicio (String nombre, String desc) {
        this.nombre = nombre;
        this.desc = desc;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Servicio: " + nombre;
    }
}
