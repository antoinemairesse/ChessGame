package com.company;


import com.company.entity.Case;
import com.company.entity.Piece;
import com.company.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class BoardView extends JPanel implements AutreEventListener {

    private final ChessFrame view;
    private final ChessModel model;
    private final LinkedList<Case> cases;

    public BoardView(ChessFrame view, ChessModel model, ChessController controller) {
        this.setBounds(0, Settings.REAL_HEIGHT, Settings.REAL_WIDTH, 400);
        this.setLayout(null);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds((int) ((Settings.REAL_WIDTH / 2) - (Settings.CASE_SIZE * 0.75)), (int) (Settings.REAL_HEIGHT + (Settings.CASE_SIZE * 0.15)), (int) (Settings.CASE_SIZE * 1.5), (int) (Settings.CASE_SIZE * 0.5));
        saveButton.addActionListener(controller);
        this.add(saveButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setBounds((int) ((Settings.REAL_WIDTH / 2) - (Settings.CASE_SIZE * 0.75)), (int) (Settings.REAL_HEIGHT + (Settings.CASE_SIZE * 0.75)), (int) (Settings.CASE_SIZE * 1.5), (int) (Settings.CASE_SIZE * 0.5));
        menuButton.addActionListener(controller);
        this.add(menuButton);

        this.view = view;
        this.model = model;
        model.getNotifieur().addAutreEventListener(this);
        this.cases = model.getCases();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;

        //While game is running
        if (!model.isGameWon() && !model.isGameLost()) {

            //Paint board
            for (Case c : cases) {
                g.setColor(c.getColor());
                g.fillRect(c.getX() * c.getWidth(), c.getY() * c.getHeight(), c.getWidth(), c.getHeight());
            }

            //Paint pieces
            paintPieces(model.getPieces(), g);

            /*White border on hovering case.
               and hint if case is a possible move */
            for (Case c : cases) {
                //Hover
                if (c.isHovered()) {
                    g.setColor(Color.white);
                    g2D.setStroke(new BasicStroke(7));
                    g.drawRect(c.getX() * c.getWidth(), c.getY() * c.getHeight(), c.getWidth(), c.getHeight());
                }

                //Hint
                if (c.isHinted()) {
                    if (c.getPiece() != null) { // Can kill
                        g.setColor(new Color(255, 0, 0, 100));
                    } else { // Empty case
                        g.setColor(new Color(0, 0, 0, 50));
                    }
                    g.fillOval(
                            c.getX() * c.getWidth() + (c.getWidth() / 3),
                            c.getY() * c.getHeight() + (c.getHeight() / 3),
                            c.getHeight() / 3,
                            c.getHeight() / 3
                    );
                }
            }
            //Paint the numbers of each case A-H 1-7
            paintCaseNumber(g);

            //Paint timers
            paintTimers(g);

            //Paint the names of the players
            paintNames(g);
        }

        //Winning screen
        if (model.isGameWon()) {
            Font font = new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.8));
            g.setFont(font);
            g.setColor(Color.GREEN);
            centerString(g, "You won !!", new Rectangle(0, 0, Settings.REAL_WIDTH, Settings.REAL_HEIGHT), font);
            Timer tm = new Timer(1500, e -> EventQueue.invokeLater(() -> {
                model.setGameWon(false);
                repaint();
            }));
            tm.setRepeats(false);
            tm.start();
        }

        //Loose screen
        else if (model.isGameLost()) {
            Font font = new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.8));
            g.setFont(font);
            g.setColor(Color.RED);
            centerString(g, "You lost...", new Rectangle(0, 0, Settings.REAL_WIDTH, Settings.REAL_HEIGHT), font);
            Timer tm = new Timer(1500, e -> EventQueue.invokeLater(() -> {
                model.setGameLost(false);
                repaint();
            }));
            tm.setRepeats(false);
            tm.start();
        }
    }

    //Print a string centered in the rectangle given in parameters.
    public void centerString(Graphics g, String s, Rectangle r, Font font) {
        FontRenderContext frc =
                new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, r.x + a, r.y + b);
    }

    //Paint timers
    private void paintTimers(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        Rectangle r1 = new Rectangle(0, Settings.REAL_HEIGHT, Settings.RATIO_WIDTH_CASES * 2, (int) (Settings.RATIO_WIDTH_CASES / 1.5));
        Rectangle r2 = new Rectangle((Settings.REAL_WIDTH - Settings.RATIO_WIDTH_CASES * 2), Settings.REAL_HEIGHT, Settings.RATIO_WIDTH_CASES * 2, (int) (Settings.RATIO_WIDTH_CASES / 1.5));
        g.fillRect(r1.x, r1.y, r1.width, r1.height);
        g.fillRect(r2.x, r2.y, r2.width, r2.height);
        g.setColor(Color.WHITE);

        centerString(g, calculateTimer(model.getPlayer()), r1, new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.5)));

        centerString(g, calculateTimer(model.getComputer()), r2, new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.5)));
    }

    //return string of type min:sec -> 3:47
    private String calculateTimer(Player player) {
        int min = (int) Math.floor(player.getTimeLeft() / 60);
        int sec = (int) player.getTimeLeft() - (min * 60);
        String test = sec < 10 ? "0" : "";
        return min + ":" + test + sec;
    }

    private void paintNames(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        Rectangle r1 = new Rectangle(0, (Settings.REAL_HEIGHT + (Settings.RATIO_WIDTH_CASES / 2)), Settings.RATIO_WIDTH_CASES * 2, (int) (Settings.RATIO_WIDTH_CASES / 1.5));
        Rectangle r2 = new Rectangle((Settings.REAL_WIDTH - Settings.RATIO_WIDTH_CASES * 2), (Settings.REAL_HEIGHT + (Settings.RATIO_WIDTH_CASES / 2)), Settings.RATIO_WIDTH_CASES * 2, (int) (Settings.RATIO_WIDTH_CASES / 1.5));
        centerString(g, Settings.PLAYER_NAME, r1, new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.25)));
        centerString(g, Settings.COMPUTER_NAME, r2, new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.25)));
    }

    private void paintPieces(LinkedList<Piece> pieces, Graphics g) {
        for (Piece piece : pieces) {
            g.drawImage(piece.getIcon(), (int) piece.getCoords().getX(), (int) piece.getCoords().getY(), piece.getSize(), piece.getSize(), null);
        }
    }

    public void paintCaseNumber(Graphics g) {
        g.setFont(new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.2187)));
        boolean colorTest = true;
        for (int x = 0; x < Settings.WIDTH_CASES; x++) {
            Object[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
            Object[] numbers = {1, 2, 3, 4, 5, 6, 7};
            Object[] invertedNumbers = {7, 6, 5, 4, 3, 2, 1};

            if (Settings.SIDE.equals(Color.BLACK)) {
                invertArray(alphabet);
            }

            if (colorTest) {
                g.setColor(Settings.CASE_COLOR2);
            } else {
                g.setColor(Settings.CASE_COLOR1);
            }
            g.drawString(
                    String.valueOf(alphabet[x]),
                    ((x + 1) * (Settings.REAL_WIDTH / Settings.WIDTH_CASES) - 15),
                    (Settings.REAL_HEIGHT - 15)
            );
            if (x < 7) {
                if (Settings.SIDE.equals(Color.BLACK)) {
                    g.drawString(
                            String.valueOf(numbers[x]),
                            3,
                            (Settings.REAL_HEIGHT - (((int) invertedNumbers[x]) * (Settings.REAL_HEIGHT / Settings.HEIGHT_CASES) - 25))
                    );
                } else {
                    g.drawString(
                            String.valueOf(numbers[x]),
                            3,
                            (Settings.REAL_HEIGHT - (((int) numbers[x]) * (Settings.REAL_HEIGHT / Settings.HEIGHT_CASES) - 25))
                    );
                }
            }
            colorTest = !colorTest;
        }
    }


    @Override
    public void actionADeclancher(AutreEvent evt) {
        if (evt.getSource() instanceof ChessModel && evt.getDonnee() instanceof Piece) {
            this.repaint();
            this.view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        if (evt.getSource() instanceof ChessModel && evt.getDonnee() == "place") {
            this.repaint();
            this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        if (evt.getSource() instanceof ChessModel && evt.getDonnee() == "timer") {
            this.repaint();
        }
    }


    void invertArray(Object[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    public ChessFrame getView() {
        return view;
    }

    public ChessModel getModel() {
        return model;
    }

    public LinkedList<Case> getCases() {
        return cases;
    }
}
