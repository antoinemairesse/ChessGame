package com.company.Pieces;

import com.company.ChessModel;
import com.company.Coordinates;
import com.company.Piece;

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
        if (this.color == Color.WHITE) {
            if ((p = model.getPieceByCase(this.xCase, this.yCase - 1)) == null) {
                //can move forward
                this.nextMoves.add(new Coordinates(this.xCase, this.yCase - 1));
            }
            if ((p = model.getPieceByCase(this.xCase + 1, this.yCase - 1)) != null && p.color != this.color) {
                //can capture piece
                this.nextMoves.add(new Coordinates(this.xCase + 1, this.yCase - 1));
            }
            if ((p = model.getPieceByCase(this.xCase - 1, this.yCase - 1)) != null && p.color != this.color) {
                //can capture piece
                this.nextMoves.add(new Coordinates(this.xCase - 1, this.yCase - 1));
            }
        } else if (this.color == Color.BLACK){
            if((p = model.getPieceByCase(this.xCase, this.yCase+1)) == null){
                // avance
                this.nextMoves.add(new Coordinates(this.xCase, this.yCase + 1));
            } else {
                // bloqué
            }
            if((p = model.getPieceByCase(this.xCase+1, this.yCase+1)) != null && p.color != this.color){
                //attaque
                this.nextMoves.add(new Coordinates(this.xCase+1, this.yCase + 1));
            }
            if((p = model.getPieceByCase(this.xCase-1, this.yCase+1)) != null && p.color != this.color){
                //attaque
                this.nextMoves.add(new Coordinates(this.xCase-1, this.yCase + 1));
            }
        }
    }
}
