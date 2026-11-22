package com.ANG.doggypets;

import java.util.Date;

public class Mascotas {
    private int id_mascota;
    private String nombre;
    private float peso;
    private float racion;

    public Mascotas (int id_mascota, String nombre, float peso, float racion) {
        this.id_mascota = id_mascota;
        this.nombre = nombre;
        this.peso = peso;
        this.racion = racion;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPeso() {
        return peso;
    }

    public float getRacion() {
        return racion;
    }
}
