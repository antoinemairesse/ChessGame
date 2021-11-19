package com.company;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
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
        Timer timer = new Timer(50, e -> this.repaint());
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        if(!model.isGameWon && !model.isGameLost) {
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
            paintTimers(g);
            paintNames(g);
        }
        if(model.isGameWon){
            /*g.setColor(Color.white);
            g.fillRect(0,0, 1000, 1000);*/
            Font font = new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.8));
            g.setFont(font);
            g.setColor(Color.GREEN);
            centerString(g,"You won !!", new Rectangle(0,0,Settings.REAL_WIDTH,Settings.REAL_HEIGHT),font);
            Timer tm = new Timer(1500, e -> EventQueue.invokeLater(() -> {
                 model.isGameWon = false;
                 repaint();
             }));
            tm.setRepeats(false);
            tm.start();
        } else if (model.isGameLost) {
            /*g.setColor(Color.white);
            g.fillRect(0,0, 1000, 1000);*/
            Font font = new Font("", Font.BOLD, (int) (Settings.CASE_SIZE * 0.8));
            g.setFont(font);
            g.setColor(Color.RED);
            centerString(g,"You lost...", new Rectangle(0,0,Settings.REAL_WIDTH,Settings.REAL_HEIGHT),font);
            Timer tm = new Timer(1500, e -> EventQueue.invokeLater(() -> {
                model.isGameLost = false;
                repaint();
            }));
            tm.setRepeats(false);
            tm.start();
        }
    }

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

    private void paintTimers(Graphics g){
        g.setColor(Color.DARK_GRAY);
        Rectangle r1 = new Rectangle(0,Settings.REAL_HEIGHT, Settings.RATIO_WIDTH_CASES*2, (int) (Settings.RATIO_WIDTH_CASES/1.5));
        Rectangle r2 = new Rectangle((Settings.REAL_WIDTH - Settings.RATIO_WIDTH_CASES*2),Settings.REAL_HEIGHT, Settings.RATIO_WIDTH_CASES*2, (int) (Settings.RATIO_WIDTH_CASES/1.5));
        g.fillRect(r1.x, r1.y, r1.width, r1.height);
        g.fillRect(r2.x, r2.y, r2.width, r2.height);
        g.setColor(Color.WHITE);
        centerString(g, String.format("%.2f", model.player.getTimeLeft()), r1 ,new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.5)));
        centerString(g, String.format("%.2f", model.computer.getTimeLeft()), r2 ,new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.5)));
    }

    private void paintNames(Graphics g){
        g.setColor(Color.DARK_GRAY);
        Rectangle r1 = new Rectangle(0, (Settings.REAL_HEIGHT + (Settings.RATIO_WIDTH_CASES/2)), Settings.RATIO_WIDTH_CASES*2, (int) (Settings.RATIO_WIDTH_CASES/1.5));
        Rectangle r2 = new Rectangle((Settings.REAL_WIDTH - Settings.RATIO_WIDTH_CASES*2), (Settings.REAL_HEIGHT + (Settings.RATIO_WIDTH_CASES/2)), Settings.RATIO_WIDTH_CASES*2, (int) (Settings.RATIO_WIDTH_CASES/1.5));
        centerString(g, Settings.PLAYER_NAME, r1 ,new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.25)));
        centerString(g, Settings.COMPUTER_NAME, r2 ,new Font("", Font.BOLD, (int) (Settings.RATIO_WIDTH_CASES * 0.25)));
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
