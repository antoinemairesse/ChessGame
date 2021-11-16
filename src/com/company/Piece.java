package com.company;

import java.awt.*;
import java.util.LinkedList;

public abstract class Piece {
    protected int xCase, yCase, size = Settings.CASE_SIZE;
    protected Coordinates coords;
    protected Image icon;
    protected Color color;
    protected LinkedList<Coordinates> nextMoves = new LinkedList<>();
    protected Player player;

    public Piece(int xCase, int yCase, Color color, Player player) {
        this.xCase = xCase;
        this.yCase = yCase;
        this.color = color;
        this.player = player;
        double x =  ((this.xCase - 1)*((double)Settings.REAL_WIDTH/Settings.WIDTH_CASES));
        double y = ((this.yCase - 1)*((double)Settings.REAL_HEIGHT/Settings.HEIGHT_CASES));
        this.coords = new Coordinates(x,y);
    }

    public abstract void nextPossibleMoves(ChessModel model);

    public int getxCase() {
        return xCase;
    }

    public void setxCase(int xCase) {
        this.xCase = xCase;
    }

    public int getyCase() {
        return yCase;
    }

    public void setyCase(int yCase) {
        this.yCase = yCase;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public LinkedList<Coordinates> getNextMoves() {
        return nextMoves;
    }

    public void setNextMoves(LinkedList<Coordinates> nextMoves) {
        this.nextMoves = nextMoves;
    }
}
