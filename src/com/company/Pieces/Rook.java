package com.company.Pieces;

import com.company.ChessModel;
import com.company.Piece;

import javax.swing.*;
import java.awt.*;

public class Rook extends Piece {
    public Rook(int xCase, int yCase, Color color){
        super(xCase, yCase, color);
        if(color == Color.BLACK){
            this.icon = new ImageIcon("resources/br.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wr.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {

    }
}
