package com.company;

import com.company.Pieces.*;

import java.awt.*;
import java.awt.event.*;

public class ChessController implements MouseListener, MouseMotionListener {
    private ChessModel model;
    private ChessView view;
    private Piece selectedPiece = null;

    //Collection de piece blanc noir
    //l'ordi choisi une piece random dans la collection puis regarde les possibles moves
    // si y'en a pas alors il rechoisi
    //si il y en a au moins un il execute un random

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Color color;
        if(Settings.SIDE == Color.WHITE){
            color = Color.BLACK;
        } else {
            color = Color.WHITE;
        }
        selectedPiece = model.getPieceByCoords(e.getX(), e.getY());
        if(selectedPiece != null && selectedPiece.color == Settings.SIDE && !model.player.isCanPlay()){
            selectedPiece = null;
        }else if(selectedPiece != null && selectedPiece.color == color && !model.computer.isCanPlay()){
            selectedPiece = null;
        }
        if(selectedPiece != null){
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

    public ChessModel getModel() {
        return model;
    }

    public void setModel(ChessModel model) {
        this.model = model;
    }

    public ChessView getView() {
        return view;
    }

    public void setView(ChessView view) {
        this.view = view;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }
}
