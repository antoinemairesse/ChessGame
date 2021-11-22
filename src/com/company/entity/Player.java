package com.company.entity;

import com.company.Settings;

import java.io.Serializable;
import java.util.LinkedList;

public class Player implements Serializable {
    private String name;
    private LinkedList<Piece> pieces = new LinkedList<>();
    private double timeLeft = Settings.PLAY_TIME;
    private boolean canPlay;
    private LinkedList<Piece> captured = new LinkedList<>();
    private static long chrono = 0;
    private boolean chronoStarted = false;

    public Player(String name, boolean canPlay) {
        this.name = name;
        this.canPlay = canPlay;
        if(canPlay){
            startChrono();
        }
    }

    public void startChrono(){
        chrono = java.lang.System.currentTimeMillis();
        chronoStarted = true;
    }

    public void stopChrono(){
        timeLeft -= ((java.lang.System.currentTimeMillis() - chrono)/1000.0);
        chronoStarted = false;
        chrono = 0;
    }

    public static long getChrono() {
        return chrono;
    }

    public static void setChrono(long chrono) {
        Player.chrono = chrono;
    }

    public boolean isChronoStarted() {
        return chronoStarted;
    }

    public void setChronoStarted(boolean chronoStarted) {
        this.chronoStarted = chronoStarted;
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
        if(isChronoStarted()){
            return timeLeft - ((java.lang.System.currentTimeMillis() - chrono)/1000.0);
        } else {
            return timeLeft;
        }
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }
}
