package com.mackenzie.tictactoe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Jugada {

    private String jugador1;
    private String jugador2;
    private String ganadorId;
    private String abandonoId;
    private List<Integer> celdas;
    private boolean turnoP1;
    private Date created;

    public Jugada() {
    }

    public Jugada(String jugador1) {
        this.jugador1 = jugador1;
        this.jugador2 = "";
        this.celdas = new ArrayList<>();
        for (int i = 0 ; i<9 ; i++) {
            this.celdas.add(new Integer(0));
        }
        this.turnoP1 = true;
        this.created = new Date();
        this.ganadorId = "";
        this.abandonoId = "";
    }

    public String getJugador1() {
        return jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public String getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(String ganadorId) {
        this.ganadorId = ganadorId;
    }

    public String getAbandonoId() {
        return abandonoId;
    }

    public void setAbandonoId(String abandonoId) {
        this.abandonoId = abandonoId;
    }

    public List<Integer> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<Integer> celdas) {
        this.celdas = celdas;
    }

    public boolean isTurnoP1() {
        return turnoP1;
    }

    public void setTurnoP1(boolean turnoP1) {
        this.turnoP1 = turnoP1;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
