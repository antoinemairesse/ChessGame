package com.company;

public class ChessGame {

    public ChessGame() {
        ChessModel model = new ChessModel();
        ChessController controller = new ChessController(model, null);
        ChessView view = new ChessView(controller, model);
        controller.view = view;
        view.pack();
        view.setVisible(true);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(ChessGame::new);
    }
}
