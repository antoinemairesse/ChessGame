package com.company.Pieces;

import com.company.*;

import javax.swing.*;
import java.awt.*;

public class Knight extends Piece {
    public Knight(int xCase, int yCase, Color color, Player player){
        super(xCase, yCase, color, player);
        if(color == Color.BLACK){
            this.icon = new ImageIcon(Settings.PIECE_PATH+"bn.png").getImage();
        } else {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"wn.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        movePossible(-1, 2, model);
        movePossible(1, 2, model);
        movePossible(-1, -2, model);
        movePossible(1, -2, model);
        movePossible(2, 1, model);
        movePossible(2, -1, model);
        movePossible(-2, 1, model);
        movePossible(-2, -1, model);
    }

    public void movePossible(int offsetX, int offsetY, ChessModel model){
        Piece p;
        if ((p = model.getPieceByCase(this.xCase+offsetX, this.yCase+offsetY)) == null) {
            //Can move to case
            this.nextMoves.add(new Coordinates(this.xCase+offsetX, this.yCase+offsetY));
        } else {
            if(p.getColor() != this.color){
                //Can capture piece
                this.nextMoves.add(new Coordinates(this.xCase+offsetX, this.yCase+offsetY));
            }
            //There is an obstacle blocking the way
        }
    }
}
