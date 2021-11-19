package com.company.Pieces;

import com.company.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class King extends Piece {
    public boolean movesCalculated = false;
    public King(int xCase, int yCase, Color color, Player player) {
        super(xCase, yCase, color, player);
        if (color == Color.BLACK) {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"bk.png").getImage();
        } else {
            this.icon = new ImageIcon(Settings.PIECE_PATH+"wk.png").getImage();
        }
    }

    //

    @Override
    public void nextPossibleMoves(ChessModel model) {
        this.getNextMoves().clear();
        Piece p;
        int colorDiff = this.color == Settings.SIDE ? 0 : -2;

        for (int x = xCase - 1; x <= xCase + 1; x++) {
            for (int y = yCase - 1; y <= yCase + 1; y++) {
                if (!(x == xCase && y == yCase)) {
                    if ((p = model.getPieceByCase(x, y)) == null) {
                        //Can move to case
                        this.nextMoves.add(new Coordinates(x, y));
                    } else {
                        if (p.getColor() != this.color) {
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
            if (!(piece instanceof King) && piece.getColor() != this.color) {
                piece.nextPossibleMoves(model);
                //TODO King in danger
            } else if((piece instanceof King) && piece.getColor() != this.color && !movesCalculated){
                // movesCalculated prevent StackOverflow error (infinite loop between the two kings)
                this.movesCalculated = true;
                piece.nextPossibleMoves(model);
                this.movesCalculated = false;
            }
            if (piece.getColor() != this.color) {
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
                    LinkedList<Coordinates> toRemove = new LinkedList<>();
                    for (Coordinates coKing : this.nextMoves) {
                        if ((pi = model.getPieceByCase(
                                (int) coKing.getX() - 1,
                                (int) coKing.getY() - (1 + colorDiff)
                        )) != null && pi instanceof Pawn && pi.getColor() != this.color) {
                            toRemove.add(coKing);
                        } else if ((pi = model.getPieceByCase(
                                (int) coKing.getX() + 1,
                                (int) coKing.getY() - (1 + colorDiff)
                        )) != null && pi instanceof Pawn && pi.getColor() != this.color) {
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
