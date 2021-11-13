package com.company.Pieces;

import com.company.ChessModel;
import com.company.Coordinates;
import com.company.Piece;
import com.company.Settings;

import javax.swing.*;
import java.awt.*;

public class Bishop extends Piece {
    public Bishop(int xCase, int yCase, Color color) {
        super(xCase, yCase, color);
        if (color == Color.BLACK) {
            this.icon = new ImageIcon("resources/bb.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wb.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        Piece p;

        // x+ y+
        for (int offset = 1; (this.yCase + offset) <= Settings.HEIGHT_CASES && (this.xCase + offset) <= Settings.WIDTH_CASES; offset++) {
            if ((p = model.getPieceByCase(this.xCase + offset, this.yCase + offset)) == null) {
                //Can move forward
                this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase + offset));
            } else {
                if (p.color != this.color) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase + offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }


        // x- y-
        for (int offset = 1; (this.yCase - offset) >= 1 && (this.xCase - offset) >= 1; offset++) {
            if ((p = model.getPieceByCase(this.xCase - offset, this.yCase - offset)) == null) {
                //Can move forward
                this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase - offset));
            } else {
                if (p.color != this.color) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase - offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }


        // x+ y-
        for (int offset = 1; (this.yCase - offset) >= 1 && (this.xCase + offset) <= Settings.WIDTH_CASES; offset++) {
            if ((p = model.getPieceByCase(this.xCase + offset, this.yCase - offset)) == null) {
                //Can move forward
                this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase - offset));
            } else {
                if (p.color != this.color) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase - offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }


        // x- y+
        for (int offset = 1; (this.yCase + offset) <= Settings.HEIGHT_CASES && (this.xCase - offset) >= 1; offset++) {
            if ((p = model.getPieceByCase(this.xCase - offset, this.yCase + offset)) == null) {
                //Can move forward
                this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase + offset));
            } else {
                if (p.color != this.color) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase + offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }
    }
}
