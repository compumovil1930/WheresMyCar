package com.ratatouille.models;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {


    protected String nombre;
    protected double calificacion;
    protected String correo;
    protected Long documento;
    protected String clave;
    protected int telefono;
    protected String fechaNacimiento;
    protected String foto;
    protected int creditos;
    protected Direccion direccion;
    protected List<Herramienta> listaHerramientas;

    public Usuario(String nombre, double calificacion, String correo, Long documento, String clave, int telefono, String fechaNacimiento, String foto, int creditos, Direccion direccion, List<Herramienta> listaHerramientas) {
        this.nombre = nombre;
        this.calificacion = calificacion;
        this.correo = correo;
        this.documento = documento;
        this.clave = clave;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.creditos = creditos;
        this.direccion = direccion;
        this.listaHerramientas = listaHerramientas;
    }

    public List<Herramienta> getListaHerramientas() {
        return listaHerramientas;
    }

    public void setListaHerramientas(List<Herramienta> listaHerramientas) {
        this.listaHerramientas = listaHerramientas;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }


}
