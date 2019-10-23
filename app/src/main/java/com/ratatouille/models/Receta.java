package com.ratatouille.models;

import java.io.Serializable;
import java.util.List;

public class Receta implements Serializable {
    private String nombre;
    private String descripcion;
    private int tiempo;
    private List<Herramienta> listaHerramientas;
    private List<Ingrediente> listaIngredientes;

    public Receta() {
        
    }

    public Receta(String nombre, String descripcion, int tiempo, List<Herramienta> listaHerramientas, List<Ingrediente> listaIngredientes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.listaHerramientas = listaHerramientas;
        this.listaIngredientes = listaIngredientes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public List<Herramienta> getListaHerramientas() {
        return listaHerramientas;
    }

    public void setListaHerramientas(List<Herramienta> listaHerramientas) {
        this.listaHerramientas = listaHerramientas;
    }

    public List<Ingrediente> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<Ingrediente> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }
}
