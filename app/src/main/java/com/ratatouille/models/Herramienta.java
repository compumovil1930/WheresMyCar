package com.ratatouille.models;

public class Herramienta {
    private int id;
    private String nombre;
    private int tipo;


    public Herramienta(int id, String nombre, int tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
