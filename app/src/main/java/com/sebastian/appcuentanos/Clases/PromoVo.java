package com.sebastian.appcuentanos.Clases;

/**
 * Created by Usuario on 21/10/2017.
 */

public class PromoVo {
    private String descripcion;
    private String nombre_Restaurante;
    private String validez;
    private String foto;


    public PromoVo() {
    }

    public PromoVo(String descripcion, String nombre_Restaurante, String validez, String foto) {
        this.descripcion = descripcion;
        this.nombre_Restaurante = nombre_Restaurante;
        this.validez = validez;
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_Restaurante() {
        return nombre_Restaurante;
    }

    public void setNombre_Restaurante(String nombre_Restaurante) {
        this.nombre_Restaurante = nombre_Restaurante;
    }

    public String getValidez() {
        return validez;
    }

    public void setValidez(String validez) {
        this.validez = validez;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}


