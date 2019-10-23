package com.ratatouille.models;

import java.io.Serializable;

public class Herramienta implements Serializable {
    private String nombre;
    private int tipo;


    public Herramienta() {
    }


    public Herramienta(String nombre, int tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    @Override
    public String toString() {
        return "Herramienta{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
