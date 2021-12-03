package com.company;

public class Main {

    public Main() {
        Menu menu = new Menu();
        menu.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Main::new);
    }

}
