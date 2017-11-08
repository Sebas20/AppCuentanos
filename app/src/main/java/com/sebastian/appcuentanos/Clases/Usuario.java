package com.sebastian.appcuentanos.Clases;

/**
 * Created by Usuario on 5/11/2017.
 */

public class Usuario {
    private String uid;
    private String nombre;
    private String correo;
    private String fecha_nacimiento;
    private String sexo;
    private String url_foto;

    public Usuario() {
    }

    public Usuario(String uid, String nombre, String correo, String fecha_nacimiento, String sexo, String url_foto) {
        this.uid = uid;
        this.nombre = nombre;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.sexo = sexo;
        this.url_foto = url_foto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }
}
