package com.company;

import com.company.Pieces.Pawn;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class BoardView extends JPanel implements AutreEventListener {

    private final ChessView view;
    private final ChessModel model;
    private final LinkedList<Case> cases;


    public BoardView(ChessView view, ChessModel model) {
        this.view = view;
        this.model = model;
        model.getNotifieur().addAutreEventListener(this);
        this.cases = model.getCases();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        for (Case c : cases) {
            g.setColor(c.getColor());
            g.fillRect(c.getX() * c.getWidth(), c.getY() * c.getHeight(), c.getWidth(), c.getHeight());
        }

        paintPieces(model.getPieces(), g);

        //White border on hovering case.
        for (Case c : cases) {
            if (c.isHovered()) {
                g.setColor(Color.white);
                g2D.setStroke(new BasicStroke(7));
                g.drawRect(c.getX() * c.getWidth(), c.getY() * c.getHeight(), c.getWidth(), c.getHeight());
            }
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
        paintCaseNumber(g);
    }

    private void paintPieces(LinkedList<Piece> pieces, Graphics g) {
        for (Piece piece : pieces) {
            g.drawImage(piece.icon, (int) piece.coords.getX(), (int) piece.coords.getY(), piece.size, piece.size, null);
        }
    }

    public void paintCaseNumber(Graphics g) {


        g.setFont(new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.2187)));
        boolean colorTest = true;
        for (int x = 0; x < Settings.WIDTH_CASES; x++) {
            Object[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
            Object[] numbers = {1, 2, 3, 4, 5, 6, 7};
            Object[] invertedNumbers = {7, 6, 5, 4, 3, 2, 1};

            if (Settings.SIDE == Color.BLACK) {
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
                if (Settings.SIDE == Color.BLACK) {
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
    }

    void invertArray(Object[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    public ChessView getView() {
        return view;
    }

    public ChessModel getModel() {
        return model;
    }

    public LinkedList<Case> getCases() {
        return cases;
    }
}
