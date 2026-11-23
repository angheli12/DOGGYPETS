package com.ANG.doggypets;

import java.util.List;

public class HorarioComida {
    private String id_horario;
    private String hora;
    private int cantidad_gramos;
    private List<String> dias;

    // Constructor vacío obligatorio para Cloud Firestore
    public HorarioComida() {
    }

    // Constructor completo
    public HorarioComida(String id_horario, String hora, int cantidad_gramos, List<String> dias) {
        this.id_horario = id_horario;
        this.hora = hora;
        this.cantidad_gramos = cantidad_gramos;
        this.dias = dias;
    }

    // Getters y Setters
    public String getId_horario() {
        return id_horario;
    }

    public void setId_horario(String id_horario) {
        this.id_horario = id_horario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCantidad_gramos() {
        return cantidad_gramos;
    }

    public void setCantidad_gramos(int cantidad_gramos) {
        this.cantidad_gramos = cantidad_gramos;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }
}
