package com.ratatouille.models;

import java.util.List;

public class Cliente extends Usuario {
    private Boolean prime;

    public Cliente(String nombre, double calificacion, String correo, Long documento, String clave, int telefono, String fechaNacimiento, String foto, int creditos, Direccion direccion, List<Herramienta> listaHerramientas, Boolean prime) {
        super(nombre, calificacion, correo, documento, clave, telefono, fechaNacimiento, foto, creditos, direccion, listaHerramientas);
        this.prime = prime;
    }


    public Boolean getPrime() {
        return prime;
    }

    public void setPrime(Boolean prime) {
        this.prime = prime;
    }
}
