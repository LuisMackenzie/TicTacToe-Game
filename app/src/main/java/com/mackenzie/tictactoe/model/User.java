package com.mackenzie.tictactoe.model;

public class User {
    private String name;
    private int points;
    private int partidas;

    public User() {
    }

    public User(String name, int points, int partidas) {
        this.name = name;
        this.points = points;
        this.partidas = partidas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPartidas() {
        return partidas;
    }

    public void setPartidas(int partidas) {
        this.partidas = partidas;
    }
}
