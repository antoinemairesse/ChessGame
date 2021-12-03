package com.company.entity;

import com.company.ChessModel;
import com.company.Settings;
import com.company.entity.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

public abstract class Piece implements Serializable {
    protected int xCase, yCase, size = Settings.CASE_SIZE;
    protected Coordinates coords;
    protected transient Image icon;
    protected Color color;
    protected LinkedList<Coordinates> nextMoves = new LinkedList<>();
    protected transient Player player;

    public Piece(int xCase, int yCase, Color color, Player player) {
        this.xCase = xCase;
        this.yCase = yCase;
        this.color = color;
        this.player = player;
        double x = ((this.xCase - 1) * ((double) Settings.REAL_WIDTH / Settings.WIDTH_CASES));
        double y = ((this.yCase - 1) * ((double) Settings.REAL_HEIGHT / Settings.HEIGHT_CASES));
        this.coords = new Coordinates(x, y);
        this.setIcon();
    }

    public abstract void nextPossibleMoves(ChessModel model);

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

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

    public void setIcon() {
        if (this instanceof Pawn) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "bp.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wp.png").getImage());
            }
        } else if (this instanceof Bishop) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "bb.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wb.png").getImage());
            }
        } else if (this instanceof King) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "bk.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wk.png").getImage());
            }
        } else if (this instanceof Knight) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "bn.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wn.png").getImage());
            }
        } else if (this instanceof Queen) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "bq.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wq.png").getImage());
            }
        } else if (this instanceof Rook) {
            if (color.equals(Color.BLACK)) {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "br.png").getImage());
            } else {
                this.setIcon(new ImageIcon(Settings.PIECE_PATH + "wr.png").getImage());
            }
        }
    }
}
