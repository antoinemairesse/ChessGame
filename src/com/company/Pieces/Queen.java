package com.company.Pieces;

import com.company.ChessModel;
import com.company.Coordinates;
import com.company.Piece;
import com.company.Settings;

import javax.swing.*;
import java.awt.*;

public class Queen extends Piece {
    public Queen(int xCase, int yCase, Color color){
        super(xCase, yCase, color);
        if(color == Color.BLACK){
            this.icon = new ImageIcon("resources/bq.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wq.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();

        // Queen moves are a combination of Rook & Bishop
        Rook rook = new Rook(this.xCase, this.yCase, this.color);
        Bishop bishop = new Bishop(this.xCase, this.yCase, this.color);
        rook.nextPossibleMoves(model);
        bishop.nextPossibleMoves(model);
        this.nextMoves.addAll(rook.getNextMoves());
        this.nextMoves.addAll(bishop.getNextMoves());

    }

}
