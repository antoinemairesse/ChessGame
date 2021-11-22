package com.company.entity;

import com.company.Settings;

import java.awt.*;
import java.io.Serializable;

public class Case implements Serializable {
    private int x, y;
    private Color color;
    private boolean hinted = false;
    private boolean hovered = false;
    private Piece piece = null;

    public Case(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return Settings.RATIO_WIDTH_CASES;
    }

    public int getHeight() {
        return Settings.RATIO_HEIGHT_CASES;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isHinted() {
        return hinted;
    }

    public void setHinted(boolean hinted) {
        this.hinted = hinted;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
