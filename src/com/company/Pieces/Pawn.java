package com.company.Pieces;

import com.company.*;

import javax.swing.*;
import java.awt.*;

public class Pawn extends Piece {
    public Pawn(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player);
        if (color == Color.BLACK) {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"bp.png").getImage();
        } else {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"wp.png").getImage();
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
