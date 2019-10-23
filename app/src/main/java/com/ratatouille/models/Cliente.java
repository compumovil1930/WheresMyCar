package com.ratatouille.models;

import java.io.Serializable;
import java.util.List;

public class Cliente extends Usuario implements Serializable {
    private Boolean prime;

    public Cliente(){

    }

    public Cliente(String nombre, double calificacion, String correo, Long documento, String clave, int telefono, String fechaNacimiento, String foto, int creditos, Direccion direccion, List<Herramienta> listaHerramientas, Boolean prime) {
        super(nombre, calificacion, correo, documento, clave, telefono, fechaNacimiento, foto, creditos, direccion, listaHerramientas);
        this.prime = prime;
    }


    public Boolean getPrime() {
        return prime;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "prime=" + prime +
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

    public void setPrime(Boolean prime) {
        this.prime = prime;
    }
}
