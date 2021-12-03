package com.company.entity.Pieces;

import com.company.ChessModel;
import com.company.Settings;
import com.company.entity.Coordinates;
import com.company.entity.Piece;
import com.company.entity.Player;

import java.awt.*;

public class Pawn extends Piece {

    public Pawn(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player);
    }

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.nextMoves.clear();
        Piece p;
        int colorDiff = this.color.equals(Settings.SIDE) ? 0 : -2;
        if ((p = model.getPieceByCase(this.xCase, this.yCase - (1 + colorDiff))) == null
                && this.yCase - (1 + colorDiff) <= Settings.HEIGHT_CASES && this.yCase - (1 + colorDiff) > 0
                && this.xCase <= Settings.WIDTH_CASES && this.xCase > 0) {

            //Check for pawn promotion
            if (this.getColor().equals(Settings.SIDE) && this.yCase - (1 + colorDiff) == 1) {
                if (!this.player.getCaptured().isEmpty()) {
                    this.nextMoves.add(new Coordinates(this.xCase, this.yCase - (1 + colorDiff)));
                }
            } else if (!this.getColor().equals(Settings.SIDE) && this.yCase - (1 + colorDiff) == 7) {
                if (!this.player.getCaptured().isEmpty()) {
                    this.nextMoves.add(new Coordinates(this.xCase, this.yCase - (1 + colorDiff)));
                }
            } else {
                //Can move to case
                this.nextMoves.add(new Coordinates(this.xCase, this.yCase - (1 + colorDiff)));
            }
        }
        if ((p = model.getPieceByCase(this.xCase + 1, this.yCase - (1 + colorDiff))) != null && !p.getColor().equals(this.color)) {
            //can capture piece
            this.nextMoves.add(new Coordinates(this.xCase + 1, this.yCase - (1 + colorDiff)));
        }
        if ((p = model.getPieceByCase(this.xCase - 1, this.yCase - (1 + colorDiff))) != null && !p.getColor().equals(this.color)) {
            //can capture piece
            this.nextMoves.add(new Coordinates(this.xCase - 1, this.yCase - (1 + colorDiff)));
        }

    }

}
