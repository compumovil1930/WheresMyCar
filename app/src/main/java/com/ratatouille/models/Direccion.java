package com.ratatouille.models;

import java.io.Serializable;

public class Direccion implements Serializable {
    private int tipo;
    private String detalles;
    private String direccion;
    private double latitud;
    private double longitud;

    public Direccion() {

    }

    public Direccion(int tipo, String detalles, String direccion, double latitud, double longitud) {
        this.tipo = tipo;
        this.detalles = detalles;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

