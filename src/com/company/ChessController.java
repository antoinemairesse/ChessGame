package com.company;

import com.company.Pieces.*;

import java.awt.*;
import java.awt.event.*;

public class ChessController implements MouseListener, MouseMotionListener {
    ChessModel model;
    ChessView view;
    Piece selectedPiece = null;

    //Collection de piece blanc noir
    //l'ordi choisi une piece random dans la collection puis regarde les possibles moves
    // si y'en a pas alors il rechoisi
    //si il y en a au moins un il execute un random
    //Possibilite de changer de side dans les settings

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
        if(selectedPiece != null && selectedPiece.color != Settings.SIDE){
            selectedPiece = null;
        }
        if(selectedPiece != null && !(selectedPiece instanceof King)){
            selectedPiece.nextPossibleMoves(model);
            model.setNextPossiblesMovesCasesHinted(selectedPiece.nextMoves);
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
        Piece p;
        if((p = model.getPieceByCoords(e.getX(), e.getY())) != null && p.color == Settings.SIDE){
            this.view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }else{
            this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
