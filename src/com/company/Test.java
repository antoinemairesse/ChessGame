package com.company;

import javax.swing.*;
import java.awt.*;

public class Test extends JDialog {
    public Test(){
        this.setPreferredSize(new Dimension(400,400));
        this.setResizable(false);
        this.setTitle("Chess Game - Pawn promotion");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLayout(null);
        this.pack();
    }
}
