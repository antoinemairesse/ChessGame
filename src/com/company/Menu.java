package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    public Menu(){
        super("Chess Game - Menu");

        this.setPreferredSize(new Dimension(600,600));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MenuShowcase menu = new MenuShowcase();
        menu.setBounds(0,0,600,300);
        menu.setLayout(null);

        JPanel p = new JPanel();
        p.setBounds(0,300,600,300);
        p.setLayout(null);

        JTextField playerNameTB = new JTextField(Settings.PLAYER_NAME, 16);
        playerNameTB.setBounds(25,300,150,25);
        playerNameTB.addActionListener(e -> {
            Settings.PLAYER_NAME = playerNameTB.getText();
        });
        p.add(playerNameTB);

        JButton playerNameSubmit = new JButton("Ok");
        playerNameSubmit.setBounds(180,300,50,25);
        playerNameSubmit.addActionListener(e -> {
            if(e.getActionCommand().equals("Ok")){
                Settings.PLAYER_NAME = playerNameTB.getText();
            }
        });
        p.add(playerNameSubmit);


        JTextField computerNameTB = new JTextField(Settings.COMPUTER_NAME, 16);
        computerNameTB.setBounds(415,300,150,25);
        computerNameTB.addActionListener(e -> {
            Settings.COMPUTER_NAME = computerNameTB.getText();
        });
        p.add(computerNameTB);

        JButton computerNameSubmit = new JButton("Ok");
        computerNameSubmit.setBounds(360,300,50,25);
        computerNameSubmit.addActionListener(e -> {
            if(e.getActionCommand().equals("Ok")){
                Settings.COMPUTER_NAME = computerNameTB.getText();
            }
        });
        p.add(computerNameSubmit);

        String[] pieceChoice = { "Ciel","Bases", "Neo","Alpha","SalleDeJeux"};
        String[] boardChoice = { "Vert","Bleu", "Brun","Violet","Rouge"};
        String[] sideChoice = { "White","Black"};
        String[] resolutionChoice = { "680*640","765*720", "850*800","935*880","1020*960"};

        JComboBox<String> pc = new JComboBox<>(pieceChoice);
        pc.setBounds(25,350,150,25);
        pc.setVisible(true);
        pc.addActionListener(e -> {
            if(e.getActionCommand().equals("comboBoxChanged")){
                Settings.PIECE_PATH = "resources/"+pc.getSelectedItem()+"/";
                menu.repaint();
            }
        });
        p.add(pc);

        JComboBox<String> bc = new JComboBox<>(boardChoice);
        bc.setBounds(25,400,150,25);
        bc.setVisible(true);
        bc.addActionListener(e -> {
            if(e.getActionCommand().equals("comboBoxChanged")){
                if(bc.getSelectedItem() == "Vert"){
                    Settings.CASE_COLOR1 = Color.decode("#769656");
                    Settings.CASE_COLOR2 = Color.decode("#EEEED2");
                } else if(bc.getSelectedItem() == "Bleu"){
                    Settings.CASE_COLOR1 = Color.decode("#4B7399");
                    Settings.CASE_COLOR2 = Color.decode("#EAE9D2");
                } else if(bc.getSelectedItem() == "Brun"){
                    Settings.CASE_COLOR1 = Color.decode("#B58863");
                    Settings.CASE_COLOR2 = Color.decode("#F0D9B5");
                } else if(bc.getSelectedItem() == "Violet"){
                    Settings.CASE_COLOR1 = Color.decode("#8877B7");
                    Settings.CASE_COLOR2 = Color.decode("#EFEFEF");
                } else if(bc.getSelectedItem() == "Rouge"){
                    Settings.CASE_COLOR1 = Color.decode("#BA5546");
                    Settings.CASE_COLOR2 = Color.decode("#EDD1B8");
                }
                menu.repaint();
            }
        });
        p.add(bc);

        JComboBox<String> gc = new JComboBox<>(sideChoice);
        gc.setBounds(415,350,150,25);
        gc.setVisible(true);
        gc.addActionListener(e -> {
            if(e.getActionCommand().equals("comboBoxChanged")){
                if(gc.getSelectedItem() == "White"){
                    Settings.SIDE = Color.WHITE;
                } else if(gc.getSelectedItem() == "Black"){
                    Settings.SIDE = Color.BLACK;
                }
            }
        });
        p.add(gc);

        JComboBox<String> rc = new JComboBox<>(resolutionChoice);
        rc.setBounds(415,400,150,25);
        rc.setVisible(true);
        rc.setSelectedIndex(2);
        rc.addActionListener(e -> {
            if(e.getActionCommand().equals("comboBoxChanged")){
                if(rc.getSelectedItem() == "680*640"){
                    Settings.CASE_SIZE = 80;
                } else if(rc.getSelectedItem() == "765*720"){
                    Settings.CASE_SIZE = 90;
                } else if(rc.getSelectedItem() == "850*800"){
                    Settings.CASE_SIZE = 100;
                } else if(rc.getSelectedItem() == "935*880"){
                    Settings.CASE_SIZE = 110;
                } else if(rc.getSelectedItem() == "1020*960"){
                    Settings.CASE_SIZE = 120;
                }
                menu.repaint();
            }
        });
        p.add(rc);

        JButton playButton = new JButton("PLAY");
        playButton.setBounds(225,460,150,50);
        playButton.addActionListener(e -> {
            if(e.getActionCommand().equals("PLAY")){
                Settings.reCalculate();
                ChessModel model = new ChessModel();
                ChessController controller = new ChessController(model, null);
                ChessView view = new ChessView(controller, model);
                controller.setView(view);
                view.pack();
                view.setVisible(true);
                view.setLocationRelativeTo(null);
                this.dispose();
            }
        });
        p.add(playButton);

        this.add(menu);
        this.add(p);
        this.setVisible(true);
        this.pack();
    }
}

class MenuShowcase extends JPanel{
    @Override
    public void paint(Graphics g) {
        int caseSize = 300/Settings.WIDTH_CASES;
        for(int x = 0; x < Settings.WIDTH_CASES; x++){
            for(int y = 0; y < Settings.HEIGHT_CASES; y++){
                if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
                    g.setColor(Settings.CASE_COLOR1);
                } else {
                    g.setColor(Settings.CASE_COLOR2);
                }
                g.fillRect((x*caseSize)+150, y*caseSize, caseSize, caseSize);
            }
        }


        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"br.png").getImage(), 0, 0, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"bn.png").getImage(), 75, 0, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"bp.png").getImage(), 0, 75, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"bp.png").getImage(), 75, 75, 75, 75, null);

        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"wb.png").getImage(), 445, 0, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"wq.png").getImage(), 515, 0, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"wp.png").getImage(), 445, 75, 75, 75, null);
        g.drawImage(new ImageIcon(Settings.PIECE_PATH+"wp.png").getImage(), 515, 75, 75, 75, null);
    }
}
