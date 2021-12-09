package com.company.entity.Pieces;

import com.company.ChessModel;
import com.company.Settings;
import com.company.entity.Coordinates;
import com.company.entity.Piece;
import com.company.entity.Player;

import java.awt.*;

public class Bishop extends Piece {
    public Bishop(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player, 30, "B");
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
                if (!p.getColor().equals(this.color)) {
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
                if (!p.getColor().equals(this.color)) {
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
                if (!p.getColor().equals(this.color)) {
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
                if (!p.getColor().equals(this.color)) {
                    //Can capture piece
                    this.nextMoves.add(new Coordinates(this.xCase - offset, this.yCase + offset));
                }
                //There is an obstacle blocking the way
                break;
            }
        }
    }
}
