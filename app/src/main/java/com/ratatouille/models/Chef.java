package com.ratatouille.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chef extends Usuario implements Serializable {

    private String parrafoDescriptivo;
    private Boolean estado;
    private List<Receta> listaRecetas;

    public Chef(){

    }

    public Chef(String nombre, double calificacion, String correo, Long documento, String clave, int telefono, String fechaNacimiento, String foto, int creditos, Direccion direccion, List<Herramienta> listaHerramientas, String parrafoDescriptivo, Boolean estado, List<Receta> listaRecetas) {
        super(nombre, calificacion, correo, documento, clave, telefono, fechaNacimiento, foto, creditos, direccion, listaHerramientas);
        this.parrafoDescriptivo = parrafoDescriptivo;
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

    @Override
    public String toString() {
        return "Chef{" +
                "parrafoDescriptivo='" + parrafoDescriptivo + '\'' +
                ", estado=" + estado +
                ", listaRecetas=" + listaRecetas +
                ", nombre='" + nombre + '\'' +
                ", calificacion=" + calificacion +
                ", correo='" + correo + '\'' +
                ", documento=" + documento +
                ", clave='" + clave + '\'' +
                ", telefono=" + telefono +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", foto='" + foto + '\'' +
                ", creditos=" + creditos +
                ", direccion=" + direccion +
                ", listaHerramientas=" + listaHerramientas +
                '}';
    }
}
