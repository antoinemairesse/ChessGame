package com.company.Pieces;

import com.company.ChessModel;
import com.company.Coordinates;
import com.company.Piece;
import com.company.Settings;

import javax.swing.*;
import java.awt.*;

public class Pawn extends Piece {
    public Pawn(int xCase, int yCase, Color color) {
        super(xCase, yCase, color);
        if (color == Color.BLACK) {
            this.icon = new ImageIcon("resources/bp.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wp.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        Piece p;
        int colorDiff = this.color == Settings.SIDE ? 0 : -2;

        if ((p = model.getPieceByCase(this.xCase, this.yCase - (1 + colorDiff))) == null) {
            //Can move to case
            this.nextMoves.add(new Coordinates(this.xCase, this.yCase - (1 + colorDiff)));
        }
        if ((p = model.getPieceByCase(this.xCase + 1, this.yCase - (1 + colorDiff))) != null && p.getColor() != this.color) {
            //can capture piece
            this.nextMoves.add(new Coordinates(this.xCase + 1, this.yCase - (1 + colorDiff)));
        }
        if ((p = model.getPieceByCase(this.xCase - 1, this.yCase - (1 + colorDiff))) != null && p.getColor() != this.color) {
            //can capture piece
            this.nextMoves.add(new Coordinates(this.xCase - 1, this.yCase - (1 + colorDiff)));
        }

    }
}
