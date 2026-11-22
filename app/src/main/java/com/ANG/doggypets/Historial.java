package com.ANG.doggypets;

import java.util.Date;

public class Historial {

    private int id_dispensacion;
    private Date fecha_hora;
    private float gramos;

    public Historial (int id_dispensacion, Date fecha_hora, float gramos) {
        this.id_dispensacion = id_dispensacion;
        this.fecha_hora = fecha_hora;
        this.gramos = gramos;
    }

    public int getId_dispensacion() {
        return id_dispensacion;
    }

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public float getGramos() {
        return gramos;
    }
}
