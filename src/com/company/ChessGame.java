package com.company;

public class ChessGame {

    public ChessGame() {
        Menu menu = new Menu();
        menu.setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(ChessGame::new);
    }

}
