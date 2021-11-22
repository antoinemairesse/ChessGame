package com.company.entity.Pieces;

import com.company.*;
import com.company.entity.Coordinates;
import com.company.entity.Piece;
import com.company.entity.Player;

import java.awt.*;

public class Knight extends Piece {

    public Knight(int xCase, int yCase, Color color, Player player){
        super(xCase, yCase, color, player);
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
        if ((p = model.getPieceByCase(this.xCase+offsetX, this.yCase+offsetY)) == null
                && this.yCase+offsetY <= Settings.HEIGHT_CASES && this.yCase+offsetY > 0
                && this.xCase+offsetX <= Settings.WIDTH_CASES && this.xCase+offsetX > 0){
            //Can move to case
            this.nextMoves.add(new Coordinates(this.xCase+offsetX, this.yCase+offsetY));
        } else if((p = model.getPieceByCase(this.xCase+offsetX, this.yCase+offsetY)) != null){
            if(!p.getColor().equals(this.color)){
                //Can capture piece
                this.nextMoves.add(new Coordinates(this.xCase+offsetX, this.yCase+offsetY));
            }
            //There is an obstacle blocking the way
        }
    }
}
