package com.company.Pieces;

import com.company.ChessModel;
import com.company.Coordinates;
import com.company.Piece;

import javax.swing.*;
import java.awt.*;

public class Pawn extends Piece {
    public Pawn(int xCase, int yCase, Color color){
        super(xCase, yCase, color);
        if(color == Color.BLACK){
            this.icon = new ImageIcon("resources/bp.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wp.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        Piece p;
        // Pour piece blanche
        if(( p = model.getPieceByCase(this.xCase, this.yCase-1)) == null){
            System.out.println("peut avancer");
            this.nextMoves.add(new Coordinates(this.xCase, this.yCase - 1));
        } else {
            System.out.println("bloqué !");
        }
        if((p = model.getPieceByCase(this.xCase+1, this.yCase-1)) != null && p.color == Color.BLACK){
            System.out.println("peut attaquer !");
            this.nextMoves.add(new Coordinates(this.xCase+1, this.yCase - 1));
        }
        if((p = model.getPieceByCase(this.xCase-1, this.yCase-1)) != null && p.color == Color.BLACK){
            System.out.println("peut attaquer !");
            this.nextMoves.add(new Coordinates(this.xCase-1, this.yCase - 1));
        }
    }
}
