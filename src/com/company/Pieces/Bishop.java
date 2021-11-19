package com.company.Pieces;

import com.company.*;

import javax.swing.*;
import java.awt.*;

public class Bishop extends Piece {
    public Bishop(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player);
        if (color == Color.BLACK) {
            this.setIcon(new ImageIcon(Settings.PIECE_PATH+"bb.png").getImage());
        } else {
            this.setIcon(new ImageIcon(Settings.PIECE_PATH+"wb.png").getImage());
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.getNextMoves().clear();
        Piece p;

        // x+ y+
        for (int offset = 1; (this.yCase + offset) <= Settings.HEIGHT_CASES && (this.xCase + offset) <= Settings.WIDTH_CASES; offset++) {
            if ((p = model.getPieceByCase(this.xCase + offset, this.yCase + offset)) == null) {
                //Can move to case
                this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase + offset));
            } else {
                if (p.getColor() != this.color) {
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
                //Can move to case
                this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase - offset));
            } else {
                if (p.getColor() != this.color) {
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
                //Can move to case
                this.nextMoves.add(new Coordinates(this.xCase + offset, this.yCase - offset));
            } else {
                if (p.getColor() != this.color) {
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
                //Can move to case
                this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase + offset));
            } else {
                if (p.getColor() != this.color) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase + offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }
    }
}
