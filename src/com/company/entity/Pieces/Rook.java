package com.company.entity.Pieces;

import com.company.*;
import com.company.entity.Coordinates;
import com.company.entity.Piece;
import com.company.entity.Player;

import java.awt.*;

public class Rook extends Piece {

    public Rook(int xCase, int yCase, Color color, Player player){
        super(xCase, yCase, color, player);
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        Piece p;
        for(int x = this.xCase + 1; x <= Settings.WIDTH_CASES; x++){
            if (xAxisPossibleMoves(model, x)) break;
        }

        for(int x = this.xCase - 1; x > 0; x--){
            if (xAxisPossibleMoves(model, x)) break;
        }

        for(int y = this.yCase + 1; y <= Settings.HEIGHT_CASES; y++){
            if (yAxisPossibleMoves(model, y)) break;
        }

        for(int y = this.yCase - 1; y > 0; y--){
            if (yAxisPossibleMoves(model, y)) break;
        }

    }

    private boolean yAxisPossibleMoves(ChessModel model, int y) {
        Piece p;
        if ((p = model.getPieceByCase(this.xCase, y)) == null) {
            //Can move to case
            this.nextMoves.add(new Coordinates(this.xCase, y));
        } else {
            if(!p.getColor().equals(this.color)){
                //Can capture piece
                this.nextMoves.add(new Coordinates(this.xCase, y));
            }
            //There is an obstacle blocking the way
            return true;
        }
        return false;
    }

    private boolean xAxisPossibleMoves(ChessModel model, int x) {
        Piece p;
        if ((p = model.getPieceByCase(x, this.yCase)) == null) {
            //Can move to case
            this.nextMoves.add(new Coordinates(x, this.yCase));
        } else {
            if(!p.getColor().equals(this.color)){
                //Can capture piece
                this.nextMoves.add(new Coordinates(x, this.yCase));
            }
            //There is an obstacle blocking the way
            return true;
        }
        return false;
    }
}
