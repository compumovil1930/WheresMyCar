package com.ratatouille.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Servicio {


    private String keyClient;
    private String keyChef;
    private String comments;
    private Date initialDate;
    private Date finalDate;
    private double cost_hour;
    private double calification;
    private String status;

    private  static final AtomicInteger count = new AtomicInteger(0);
    private int id;

    public Servicio(String keyClient, String keyChef, String comments, Date initialDate, Date finalDate, double cost_hour, double calification) {
        this.keyClient = keyClient;
        this.keyChef = keyChef;
        this.comments = comments;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.cost_hour = cost_hour;
        this.calification = calification;
        this.id = count.incrementAndGet();
    }

    public Servicio(String keyClient, String keyChef, Date initialDate, double cost_hour) {
        this.keyClient = keyClient;
        this.keyChef = keyChef;
        this.comments = "";
        this.initialDate = initialDate;
        this.finalDate = null;
        this.cost_hour = cost_hour;
        this.calification = -1;
        this.status = "Solicitado";
        this.id = count.incrementAndGet();
    }

    public Servicio (){

    }

    public String getKeyClient() {
        return keyClient;
    }

    public void setKeyClient(String keyClient) {
        this.keyClient = keyClient;
    }

    public String getKeyChef() {
        return keyChef;
    }

    public void setKeyChef(String keyChef) {
        this.keyChef = keyChef;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public double getCost_hour() {
        return cost_hour;
    }

    public void setCost_hour(double cost_hour) {
        this.cost_hour = cost_hour;
    }

    public double getCalification() {
        return calification;
    }

    public void setCalification(double calification) {
        this.calification = calification;
    }

    public static AtomicInteger getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
