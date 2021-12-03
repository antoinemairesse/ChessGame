package com.company;

import javax.swing.*;
import java.awt.*;

public class Promotion extends JDialog {
    //Window popup when piece is being promoted
    public Promotion() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setResizable(false);
        this.setTitle("Chess Game - Pawn promotion");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLayout(null);
        this.pack();
    }
}
