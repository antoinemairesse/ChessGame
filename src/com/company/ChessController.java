package com.company;

import com.company.entity.Piece;

import java.awt.*;
import java.awt.event.*;

public class ChessController implements MouseListener, MouseMotionListener, ActionListener {
    private ChessModel model;
    private ChessFrame view;
    private Piece selectedPiece = null;


    public ChessController(ChessModel model, ChessFrame view) {
        this.model = model;
        this.view = view;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /*When mouse is pressed we get the piece at the mouse's coordinates
    if the player can play & piece is one of his, then we display the hints on the cases
    where the piece can go*/
    @Override
    public void mousePressed(MouseEvent e) {
        selectedPiece = model.getPieceByCoords(e.getX(), e.getY());
        if (selectedPiece != null && selectedPiece.getColor().equals(Settings.SIDE) && !model.getPlayer().isCanPlay()) {
            selectedPiece = null;
        } else if (selectedPiece != null && !selectedPiece.getColor().equals(Settings.SIDE)) {
            selectedPiece = null;
        }
        if (selectedPiece != null) {
            selectedPiece.nextPossibleMoves(model);
            model.setNextPossiblesMovesCasesHinted(selectedPiece.getNextMoves());
        }
    }

    /*When the mouse is released we set the cursor back to the default one
    and we place the piece (if possible) in the case where the mouse is currently*/
    @Override
    public void mouseReleased(MouseEvent e) {
        this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (selectedPiece != null) {
            model.place(selectedPiece, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //When a piece is grabbed and we move the mouse around, we update the piece's position.
    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedPiece != null) {
            model.move(selectedPiece, (e.getX()), (e.getY()));
        }
    }

    /*When the mouse is moved, if the mouse is over one of the player's piece then we
    change the cursor to a hand to indicate that the piece can be grabbed.*/
    @Override
    public void mouseMoved(MouseEvent e) {
        Piece p;
        if ((p = model.getPieceByCoords(e.getX(), e.getY())) != null && p.getColor().equals(Settings.SIDE)) {
            this.view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public ChessModel getModel() {
        return model;
    }

    public void setModel(ChessModel model) {
        this.model = model;
    }

    public ChessFrame getView() {
        return view;
    }

    public void setView(ChessFrame view) {
        this.view = view;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    //actionListener for the Save & Menu buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        //Save game
        if (e.getActionCommand().equals("Save")) {
            if (!model.isGameWon() && !model.isGameLost()) {
                model.saveGame();
            }
        }
        //Go back to menu
        else if (e.getActionCommand().equals("Menu")) {
            view.dispose();
            if (model.getMenu() != null) {
                model.getMenu().setVisible(true);
            } else {
                Menu menu = new Menu();
                menu.setLocationRelativeTo(null);
                model.setMenu(menu);
            }
        }
    }
}
