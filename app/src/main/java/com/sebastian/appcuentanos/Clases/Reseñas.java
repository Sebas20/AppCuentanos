package com.sebastian.appcuentanos.Clases;

/**
 * Created by Manuela on 7/11/2017.
 */

public class Reseñas {
    private String uid;
    private String sid;
    private String descripcion;
    private float puntaje;

    public Reseñas(String uid, String sid, String descripcion, float puntaje) {
        this.uid = uid;
        this.sid = sid;
        this.descripcion = descripcion;
        this.puntaje = puntaje;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }

    public String getUid() {
        return uid;
    }

    public String getSid() {
        return sid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getPuntaje() {
        return puntaje;
    }
}
