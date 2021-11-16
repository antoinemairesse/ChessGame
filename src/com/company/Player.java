package com.company;

import java.util.LinkedList;

public class Player {
    private String name;
    private LinkedList<Piece> pieces = new LinkedList<>();
    private double timeLeft = Settings.PLAY_TIME;
    private boolean canPlay;
    private LinkedList<Piece> captured = new LinkedList<>();

    public Player(String name, boolean canPlay) {
        this.name = name;
        this.canPlay = canPlay;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public LinkedList<Piece> getCaptured() {
        return captured;
    }

    public void setCaptured(LinkedList<Piece> captured) {
        this.captured = captured;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(LinkedList<Piece> pieces) {
        this.pieces = pieces;
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }
}
