package com.company;

import javax.swing.*;
import java.awt.*;

public class ChessView extends JFrame {
/*
* Sur la vue tu bouges la piece ce qui provoque une action dans le controlleur qui va appeler par exemple la fonction move
* du modèle qui lui va faire tout le traitement (affecter la nouvelle pos etc) et notifier (observer observable) la vue
* du changement
* */

    public ChessView(ChessController controller, ChessModel model) {
        super("Chess Game");
        this.setPreferredSize(new Dimension(Settings.WIDTH,Settings.HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BoardView board = new BoardView(this, model);
        this.add(board);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
        this.setVisible(true);
    }


}
