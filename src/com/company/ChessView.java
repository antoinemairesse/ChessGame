package com.company;

import javax.swing.*;
import java.awt.*;

public class ChessView extends JFrame {
    public ChessView(ChessController controller, ChessModel model) {
        super("Chess Game");
        this.setPreferredSize(new Dimension(Settings.WIDTH, (int) (Settings.HEIGHT+(Settings.CASE_SIZE*1.5))));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BoardView board = new BoardView(this, model, controller);
        this.add(board);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
        this.setVisible(true);
        this.pack();
    }
}
