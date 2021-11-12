package com.company.Pieces;

import com.company.ChessModel;
import com.company.Piece;

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

    }
}
