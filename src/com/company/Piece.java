package com.company;

import java.awt.*;
import java.util.LinkedList;

public abstract class Piece {
    public int xCase, yCase, size = Settings.CASE_SIZE;
    public Coordinates coords;
    public Image icon;
    public Color color;
    public LinkedList<Coordinates> nextMoves = new LinkedList<>();

    public Piece(int xCase, int yCase, Color color) {
        this.xCase = xCase;
        this.yCase = yCase;
        this.color = color;
        double x =  ((this.xCase - 1)*((double)Settings.REAL_WIDTH/Settings.WIDTH_CASES));
        double y = ((this.yCase - 1)*((double)Settings.REAL_HEIGHT/Settings.HEIGHT_CASES));
        this.coords = new Coordinates(x,y);
    }

    public abstract void nextPossibleMoves(ChessModel model);

}
