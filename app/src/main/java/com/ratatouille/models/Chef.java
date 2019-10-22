package com.ratatouille.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chef extends Usuario implements Serializable {

    private String parrafoDescriptivo;
    private Boolean estado;
    private List<Receta> listaRecetas;

    public Chef(String nombre, double calificacion, String correo, Long documento, String clave, int telefono, String fechaNacimiento, String foto, int creditos, Direccion direccion, List<Herramienta> listaHerramientas, Boolean estado, List<Receta> listaRecetas) {
        super(nombre, calificacion, correo, documento, clave, telefono, fechaNacimiento, foto, creditos, direccion, listaHerramientas);
        this.estado = estado;
        this.listaRecetas = listaRecetas;
    }

    public String getParrafoDescriptivo() {
        return parrafoDescriptivo;
    }

    public void setParrafoDescriptivo(String parrafoDescriptivo) {
        this.parrafoDescriptivo = parrafoDescriptivo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }


    public List<Receta> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(List<Receta> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }
}
