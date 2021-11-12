package com.company;

import com.company.Pieces.Pawn;

import java.awt.*;
import java.awt.event.*;

public class ChessController implements MouseListener, MouseMotionListener {
    ChessModel model;
    ChessView view;
    Piece selectedPiece = null;

    public ChessController() {
    }

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedPiece = model.getPieceByCoords(e.getX(), e.getY());
        if(selectedPiece != null && selectedPiece instanceof Pawn && selectedPiece.color == Color.WHITE){
            selectedPiece.nextPossibleMoves(model);
            model.setNextPossiblesMovesCasesHinted(selectedPiece.nextMoves);
            for (Coordinates c : selectedPiece.nextMoves) {
                System.out.print("X : "+c.x+" Y : "+c.y+" ");
            }
            System.out.println();
        }
        if(selectedPiece != null && selectedPiece instanceof Pawn && selectedPiece.color == Color.BLACK){
            Piece p;
            if((p = model.getPieceByCase(selectedPiece.xCase, selectedPiece.yCase+1)) == null){
                System.out.println("peut avancer");
            } else {
                System.out.println("bloqué !");
            }
            if((p = model.getPieceByCase(selectedPiece.xCase+1, selectedPiece.yCase+1)) != null && p.color == Color.WHITE){
                System.out.println("peut attaquer !");
            }
            if((p = model.getPieceByCase(selectedPiece.xCase-1, selectedPiece.yCase+1)) != null && p.color == Color.WHITE){
                System.out.println("peut attaquer !");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if(selectedPiece != null){
            model.place(selectedPiece, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(selectedPiece != null){
            model.move(selectedPiece, (e.getX()), (e.getY()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(model.getPieceByCoords(e.getX(), e.getY()) != null){
            this.view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }else{
            this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
