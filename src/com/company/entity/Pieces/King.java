package com.company.entity.Pieces;

import com.company.ChessModel;
import com.company.Settings;
import com.company.entity.Coordinates;
import com.company.entity.Piece;
import com.company.entity.Player;

import java.awt.*;
import java.util.LinkedList;

public class King extends Piece {
    private boolean movesCalculated = false;

    public King(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player, 900, "K");
    }

    //

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.getNextMoves().clear();
        Piece p;
        int colorDiff = this.color.equals(Settings.SIDE) ? 0 : -2;

        for (int x = xCase - 1; x <= xCase + 1; x++) {
            for (int y = yCase - 1; y <= yCase + 1; y++) {
                if (!(x == xCase && y == yCase)) {
                    if ((p = model.getPieceByCase(x, y)) == null && y <= Settings.HEIGHT_CASES && y > 0 && x <= Settings.WIDTH_CASES && x > 0) {
                        //Can move to case
                        this.nextMoves.add(new Coordinates(x, y));
                    } else if ((p = model.getPieceByCase(x, y)) != null) {
                        if (!p.getColor().equals(this.color)) {
                            //Can capture piece
                            this.nextMoves.add(new Coordinates(x, y));
                        }
                        //There is an obstacle blocking the way
                    }
                }
            }
        }

        //Removing next moves that will put the king in danger
        for (Piece piece : model.getPieces()) {
            // Recalculate nextmoves of all pieces since they are only calculated
            //when the piece is grabbed
            if (!(piece instanceof King) && !piece.getColor().equals(this.color)) {
                piece.nextPossibleMoves(model);
            } else if ((piece instanceof King) && !piece.getColor().equals(this.color) && !movesCalculated) {
                // movesCalculated prevent StackOverflow error (infinite loop between the two kings)
                this.movesCalculated = true;
                piece.nextPossibleMoves(model);
                this.movesCalculated = false;
            }
            if (!piece.getColor().equals(this.color)) {
                if (!(piece instanceof Pawn)) {
                    for (Coordinates coordinates : piece.getNextMoves()) {
                        this.nextMoves.removeIf(
                                coKing -> coordinates.getY() == coKing.getY()
                                        && coordinates.getX() == coKing.getX()
                        );
                    }
                } else {
                    //Special case for pawns since they do not attack where they go "forward"
                    Piece pi;

                    /*We add the moves that we want to remove in a list to remove them later
                    since we can't edit a list while going through it*/
                    LinkedList<Coordinates> toRemove = new LinkedList<>();

                    for (Coordinates coKing : this.nextMoves) {
                        // Remove move where king move to case and pawn can attack king
                        if ((pi = model.getPieceByCase(
                                (int) coKing.getX() - 1,
                                (int) coKing.getY() - (1 + colorDiff)
                        )) != null && pi instanceof Pawn && !pi.getColor().equals(this.color)) {
                            toRemove.add(coKing);
                        } else if ((pi = model.getPieceByCase(
                                (int) coKing.getX() + 1,
                                (int) coKing.getY() - (1 + colorDiff)
                        )) != null && pi instanceof Pawn && !pi.getColor().equals(this.color)) {
                            toRemove.add(coKing);
                        }
                    }
                    for (Coordinates tr : toRemove) {
                        this.nextMoves.remove(tr);
                    }
                }
            }
        }
    }
}
