package com.ratatouille.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Reserva implements Serializable {
    private  static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String idUsuario;
    private String nombre;
    private Direccion direccion;
    private String fechaReserva;
    private String hora;
    private int cantidadInvitados;

    public Reserva() {

    }

    public Reserva(String idUsuario, String nombre, Direccion direccion, String fechaReserva, String hora, int cantidadInvitados) {
        this.id = count.incrementAndGet();
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaReserva = fechaReserva;
        this.hora = hora;
        this.cantidadInvitados = cantidadInvitados;
    }

    public int getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCantidadInvitados() {
        return cantidadInvitados;
    }

    public void setCantidadInvitados(int cantidadInvitados) {
        this.cantidadInvitados = cantidadInvitados;
    }
}
