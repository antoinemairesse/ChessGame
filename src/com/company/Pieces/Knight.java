package com.company.Pieces;

import com.company.ChessModel;
import com.company.Piece;

import javax.swing.*;
import java.awt.*;

public class Knight extends Piece {
    public Knight(int xCase, int yCase, Color color){
        super(xCase, yCase, color);
        if(color == Color.BLACK){
            this.icon = new ImageIcon("resources/bn.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wn.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {

    }
}
