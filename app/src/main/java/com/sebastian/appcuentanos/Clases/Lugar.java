package com.sebastian.appcuentanos.Clases;

/**
 * Created by felipe on 28/10/17.
 */

public class Lugar {
    private String Nombre;
    private String Direccion;
    private String Cuidad;
    private int Telefono;
    private String Categoria;
    private String Id;
    private String Imagen;
    private int Reseñas;
    private float Latitud;
    private float Longitud;
    private float Puntaje;

    public Lugar(String nombre, String direccion, String cuidad, int telefono, String categoria, String id, String imagen, int reseñas, float latitud, float longitud, float puntaje) {
        Nombre = nombre;
        Direccion = direccion;
        Cuidad = cuidad;
        Telefono = telefono;
        Categoria = categoria;
        Id = id;
        Imagen = imagen;
        Reseñas = reseñas;
        Latitud = latitud;
        Longitud = longitud;
        Puntaje = puntaje;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public void setCuidad(String cuidad) {
        Cuidad = cuidad;
    }

    public void setTelefono(int telefono) {
        Telefono = telefono;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public void setReseñas(int reseñas) {
        Reseñas = reseñas;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public void setPuntaje(float puntaje) {
        Puntaje = puntaje;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public String getCuidad() {
        return Cuidad;
    }

    public int getTelefono() {
        return Telefono;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getId() {
        return Id;
    }

    public String getImagen() {
        return Imagen;
    }

    public int getReseñas() {
        return Reseñas;
    }

    public float getLatitud() {
        return Latitud;
    }

    public float getLongitud() {
        return Longitud;
    }

    public float getPuntaje() {
        return Puntaje;
    }

    public Lugar() {
    }
}
