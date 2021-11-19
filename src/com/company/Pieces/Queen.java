package com.company.Pieces;

import com.company.*;

import javax.swing.*;
import java.awt.*;

public class Queen extends Piece {
    public Queen(int xCase, int yCase, Color color, Player player){
        super(xCase, yCase, color, player);
        if(color == Color.BLACK){
            this.icon = new ImageIcon(Settings.PIECE_PATH+"bq.png").getImage();
        } else {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"wq.png").getImage();
        }
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();

        // Queen moves are a combination of Rook & Bishop
        Rook rook = new Rook(this.xCase, this.yCase, this.color, this.player);
        Bishop bishop = new Bishop(this.xCase, this.yCase, this.color, this.player);
        rook.nextPossibleMoves(model);
        bishop.nextPossibleMoves(model);
        this.nextMoves.addAll(rook.getNextMoves());
        this.nextMoves.addAll(bishop.getNextMoves());

    }

}
