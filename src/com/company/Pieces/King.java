package com.company.Pieces;

import com.company.ChessModel;
import com.company.Piece;

import javax.swing.*;
import java.awt.*;

public class King extends Piece {
    public King(int xCase, int yCase, Color color){
        super(xCase, yCase, color);
        if(color == Color.BLACK){
            this.icon = new ImageIcon("resources/bk.png").getImage();
        } else {
            this.icon = new ImageIcon("resources/wk.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {

    }
}
