package com.company;


import com.company.entity.*;
import com.company.entity.Pieces.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;

public class ChessModel implements Serializable {
    private LinkedList<Piece> pieces = new LinkedList<>();
    private AutreEventNotifieur notifieur = new AutreEventNotifieur();
    private LinkedList<Case> cases = new LinkedList<>();
    private Player computer;
    private Player player;
    private boolean isGameWon = false;
    private boolean isGameLost = false;
    private transient Menu menu;

    public ChessModel() {
        timerSetup();
        createGame();
    }

    public ChessModel(FileInputStream fis) {
        timerSetup();
        createGameFromSave(fis);
    }

    //Update piece position
    public void move(Piece piece, int x, int y) {
        //Reset hovering & set new case to hovered
        for (Case c : cases) {
            c.setHovered(false);
        }
        Case c = getCaseByCaseCoords(
                (int) Math.ceil((x / (double) Settings.CASE_SIZE)),
                (int) Math.ceil((y / (double) Settings.CASE_SIZE))
        );
        if (c != null)
            c.setHovered(true);


        //Piece can't go out of window
        if (x > 0 && x < Settings.REAL_WIDTH) {
            piece.getCoords().setX(x - (Settings.CASE_SIZE * 0.5208));
        }
        if (y < Settings.REAL_HEIGHT - (Settings.CASE_SIZE * 0.25) && y > 0 + (Settings.CASE_SIZE * 0.25)) {
            piece.getCoords().setY(y - (Settings.CASE_SIZE * 0.6770));
        }

        notifieur.diffuserAutreEvent(new AutreEvent(this, piece));
    }

    //Place piece (if possible) in new case
    public void place(Piece piece, int x, int y) {
        Piece p;

        if (canMovePiece(x, y, piece)) {

            //if a piece is on the case we want to go
            if ((p = getPieceByCoords(x, y)) != null) {
                //if the piece is an "enemy" piece
                if (!p.getColor().equals(piece.getColor())) {

                    //remove piece from case who has it
                    if (p.getColor().equals(Settings.SIDE)) {
                        player.getPieces().remove(p);
                    } else {
                        computer.getPieces().remove(p);
                    }
                    getCaseByCaseCoords(p.getxCase(), p.getyCase()).setPiece(null);
                    pieces.remove(p);

                    //Player won
                    if (p instanceof King && !p.getColor().equals(Settings.SIDE)) {
                        isGameWon = true;
                        notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));
                        resetGame();
                        return;
                    }
                    //Player lost
                    else if (p instanceof King && p.getColor().equals(Settings.SIDE)) {
                        isGameLost = true;
                        notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));
                        resetGame();
                        return;
                    }
                    /*Add captured piece to player
                     We don't add pawns since they can't be used for pawn promotion*/
                    else if (!(p instanceof Pawn)) {
                        if (!player.isCanPlay()) {
                            computer.getCaptured().add(p);
                        } else {
                            player.getCaptured().add(p);
                        }
                    }
                }
            }

            // We delete piece in old case
            Case ca = getCaseByCaseCoords(piece.getxCase(), piece.getyCase());
            if (ca != null)
                ca.setPiece(null);

            int oldX = piece.getxCase();
            int oldY = piece.getyCase();

            //Calculate piece new case
            piece.setxCase((int) Math.ceil((x / (double) Settings.CASE_SIZE)));
            piece.setyCase((int) Math.ceil((y / (double) Settings.CASE_SIZE)));


            //Player has played
            if (oldX != piece.getxCase() || oldY != piece.getyCase()) {
                if (computer.isChronoStarted()) {
                    computer.stopChrono();
                    player.startChrono();
                } else if (player.isChronoStarted()) {
                    player.stopChrono();
                    computer.startChrono();
                }
                computer.setCanPlay(!computer.isCanPlay());
                player.setCanPlay(!player.isCanPlay());
            }

            //Set piece in new case
            ca = getCaseByCaseCoords(piece.getxCase(), piece.getyCase());
            if (ca != null)
                ca.setPiece(piece);
            pieceSound();

            //Player want to promote pawn
            if (piece.getyCase() == 1 && piece.getColor().equals(Settings.SIDE) && piece instanceof Pawn) {
                Piece t = promotion(piece);
                pieces.remove(piece);
                player.getPieces().add(t);
                if (ca != null)
                    ca.setPiece(t);
                pieces.add(t);
            }
            //Computer want to promote pawn
            else if (piece.getyCase() == 7 && !(piece.getColor().equals(Settings.SIDE)) && piece instanceof Pawn) {
                Piece t = computer.getCaptured().getFirst();
                computer.getCaptured().remove(t);
                t.setxCase(piece.getxCase());
                t.setyCase(piece.getyCase());
                t.setColor(piece.getColor());
                t.setPlayer(piece.getPlayer());
                t.setIcon();
                double xx = ((piece.getxCase() - 1) * ((double) Settings.REAL_WIDTH / Settings.WIDTH_CASES));
                double yy = ((piece.getyCase() - 1) * ((double) Settings.REAL_HEIGHT / Settings.HEIGHT_CASES));
                t.setCoords(new Coordinates(xx, yy));
                pieces.remove(piece);
                computer.getPieces().add(t);
                if (ca != null)
                    ca.setPiece(t);
                pieces.add(t);
            }
        }

        //Calculate piece new coordinates (centered in the new case)
        piece.getCoords().setX((piece.getxCase() - 1) * ((double) Settings.REAL_WIDTH / Settings.WIDTH_CASES));
        piece.getCoords().setY((piece.getyCase() - 1) * ((double) Settings.REAL_HEIGHT / Settings.HEIGHT_CASES));

        //Notify view that it needs to be repainted
        notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));

        //Reset
        for (Case c : cases) {
            c.setHovered(false);
            c.setHinted(false);
        }

        if (computer.isCanPlay()) {
            computerMove();
        }
    }

    public Piece getPieceByCoords(int x, int y) {
        int xCase = (int) Math.ceil((x / (double) Settings.CASE_SIZE));
        int yCase = (int) Math.ceil((y / (double) Settings.CASE_SIZE));
        for (Piece piece : pieces) {
            if (piece.getxCase() == xCase && piece.getyCase() == yCase) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPieceByCase(int xCase, int yCase) {
        for (Piece piece : pieces) {
            if (piece.getxCase() == xCase && piece.getyCase() == yCase) {
                return piece;
            }
        }
        return null;
    }

    public Case getCaseByCaseCoords(int xCase, int yCase) {
        for (Case c : cases) {
            if (c.getX() == xCase - 1 && c.getY() == yCase - 1) {
                return c;
            }
        }
        return null;
    }

    public void setNextPossiblesMovesCasesHinted(LinkedList<Coordinates> nextPossiblesMoves) {
        for (Coordinates c : nextPossiblesMoves) {
            Case ca = getCaseByCaseCoords((int) c.getX(), (int) c.getY());
            if (ca != null) {
                ca.setHinted(true);
            }
        }
    }

    // Returns true if the move is legal, false otherwise
    public boolean canMovePiece(int x, int y, Piece now) {
        boolean response = false;
        int xCase = (int) Math.ceil((x / (double) Settings.CASE_SIZE));
        int yCase = (int) Math.ceil((y / (double) Settings.CASE_SIZE));
        Case c = getCaseByCaseCoords(xCase, yCase);
        Case test;
        if (c != null) {
            for (Coordinates cd : now.getNextMoves()) {
                test = getCaseByCaseCoords((int) cd.getX(), (int) cd.getY());
                if (c == test) {
                    response = true;
                }
            }
        }
        return response;
    }

    //Plays a "piece placing" sound
    public void pieceSound() {
        File file = new File("resources/piece_sound.wav");
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip;
            clip = AudioSystem.getClip();
            assert clip != null;
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException unsupportedAudioFileException) {
            unsupportedAudioFileException.printStackTrace();
        }
    }

    /*computer choses a random piece and looks if it has any possible moves
     if it doesn't then we choose another piece again and again...
     if there is at least one move, it executes one randomly*/
    public void computerMove() {
        if (computer.isCanPlay()) {

            //get random piece
            int lengthPieces = computer.getPieces().toArray().length - 1;
            int random = (int) (Math.random() * lengthPieces);
            LinkedList<Piece> pc = new LinkedList<>(computer.getPieces());
            Piece p = pc.get(random);
            p.nextPossibleMoves(this);

            //if the piece has no moves
            while (p.getNextMoves().toArray().length <= 0) {
                pc.remove(p);

                //Computer cannot make any legal move, player wins
                if (pc.isEmpty()) {
                    isGameWon = true;
                    notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));
                    resetGame();
                    return;
                }

                //get new random piece
                lengthPieces = pc.toArray().length - 1;
                random = (int) (Math.random() * lengthPieces);
                p = pc.get(random);
                p.nextPossibleMoves(this);
            }

            //Get random move from the piece and place it
            int lengthMoves = p.getNextMoves().toArray().length - 1;
            random = (int) (Math.random() * lengthMoves);
            Coordinates move = p.getNextMoves().get(random);
            place(p, (int) move.getX() * Settings.CASE_SIZE, (int) move.getY() * Settings.CASE_SIZE);
            computer.setCanPlay(false);
        }
    }

    public void createGame() {
        Color color;
        if (Settings.SIDE.equals(Color.WHITE)) {
            color = Color.BLACK;
            player = new Player(Settings.PLAYER_NAME, true);
            computer = new Player(Settings.COMPUTER_NAME, false);
        } else {
            color = Color.WHITE;
            player = new Player(Settings.PLAYER_NAME, false);
            computer = new Player(Settings.COMPUTER_NAME, true);
        }

        player.getPieces().add(new Pawn(1, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(2, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(3, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(4, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(5, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(6, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(7, 6, Settings.SIDE, player));
        player.getPieces().add(new Pawn(8, 6, Settings.SIDE, player));

        player.getPieces().add(new Rook(1, 7, Settings.SIDE, player));
        player.getPieces().add(new Knight(2, 7, Settings.SIDE, player));
        player.getPieces().add(new Bishop(3, 7, Settings.SIDE, player));
        player.getPieces().add(new Queen(4, 7, Settings.SIDE, player));
        player.getPieces().add(new King(5, 7, Settings.SIDE, player));
        player.getPieces().add(new Bishop(6, 7, Settings.SIDE, player));
        player.getPieces().add(new Knight(7, 7, Settings.SIDE, player));
        player.getPieces().add(new Rook(8, 7, Settings.SIDE, player));

        computer.getPieces().add(new Pawn(1, 2, color, computer));
        computer.getPieces().add(new Pawn(2, 2, color, computer));
        computer.getPieces().add(new Pawn(3, 2, color, computer));
        computer.getPieces().add(new Pawn(4, 2, color, computer));
        computer.getPieces().add(new Pawn(5, 2, color, computer));
        computer.getPieces().add(new Pawn(6, 2, color, computer));
        computer.getPieces().add(new Pawn(7, 2, color, computer));
        computer.getPieces().add(new Pawn(8, 2, color, computer));

        computer.getPieces().add(new Rook(1, 1, color, computer));
        computer.getPieces().add(new Knight(2, 1, color, computer));
        computer.getPieces().add(new Bishop(3, 1, color, computer));
        computer.getPieces().add(new Queen(4, 1, color, computer));
        computer.getPieces().add(new King(5, 1, color, computer));
        computer.getPieces().add(new Bishop(6, 1, color, computer));
        computer.getPieces().add(new Knight(7, 1, color, computer));
        computer.getPieces().add(new Rook(8, 1, color, computer));

        pieces.addAll(computer.getPieces());
        pieces.addAll(player.getPieces());

        // Create board cases
        boolean colorTest = true;
        for (int y = 0; y < Settings.HEIGHT_CASES; y++) {
            for (int x = 0; x < Settings.WIDTH_CASES; x++) {
                if (colorTest) {
                    cases.add(new Case(x, y, Settings.CASE_COLOR1));
                } else {
                    cases.add(new Case(x, y, Settings.CASE_COLOR2));
                }
                colorTest = !colorTest;
            }
            colorTest = !colorTest;
        }

        //Set corresponding piece to case
        for (Piece piece : pieces) {
            Case c = getCaseByCaseCoords(piece.getxCase(), piece.getyCase());
            if (c != null)
                c.setPiece(piece);
        }
        //if player side is black make computer first move
        if (computer.isCanPlay()) {
            computerMove();
        }
    }

    public void createGameFromSave(FileInputStream fis) {
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            Save save = (Save) ois.readObject();
            this.pieces = save.getModel().pieces;
            this.computer = save.getModel().computer;
            this.player = save.getModel().player;
            this.isGameWon = save.getModel().isGameWon;
            this.isGameLost = save.getModel().isGameLost;
            this.notifieur = save.getModel().notifieur;
            Settings.PLAYER_NAME = save.getPlayerName();
            Settings.COMPUTER_NAME = save.getComputerName();
            Settings.CASE_SIZE = save.getCaseSize();
            Settings.SIDE = save.getSide();
            Settings.CASE_COLOR1 = save.getCaseColor1();
            Settings.CASE_COLOR2 = save.getCaseColor2();
            Settings.PIECE_PATH = save.getPiecePath();
            for (Piece p : pieces) {
                p.setIcon();
            }
            ois.close();
            if (player.isCanPlay()) {
                player.startChrono();
            } else {
                computer.startChrono();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Create board cases
        boolean colorTest = true;
        for (int y = 0; y < Settings.HEIGHT_CASES; y++) {
            for (int x = 0; x < Settings.WIDTH_CASES; x++) {
                if (colorTest) {
                    cases.add(new Case(x, y, Settings.CASE_COLOR1));
                } else {
                    cases.add(new Case(x, y, Settings.CASE_COLOR2));
                }
                colorTest = !colorTest;
            }
            colorTest = !colorTest;
        }

        //Set corresponding piece to case
        for (Piece piece : pieces) {
            Case c = getCaseByCaseCoords(piece.getxCase(), piece.getyCase());
            if (c != null)
                c.setPiece(piece);
        }
        //if player side is black make computer first move
        if (computer.isCanPlay()) {
            computerMove();
        }
    }

    public void resetGame() {
        pieces.clear();
        cases.clear();
        computer = null;
        player = null;
        createGame();
    }

    public void saveGame() {
        Player p;
        //Freeze time while in the JFileChooser
        if (player.isChronoStarted()) {
            player.stopChrono();
            p = player;
        } else {
            computer.stopChrono();
            p = computer;
        }
        JFileChooser jFileChooser = new JFileChooser();
        int response = jFileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fileOut = new FileOutputStream(jFileChooser.getSelectedFile() + ".txt");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                Save save = new Save(this, Settings.PLAYER_NAME, Settings.COMPUTER_NAME, Settings.CASE_SIZE, Settings.SIDE, Settings.CASE_COLOR1, Settings.CASE_COLOR2, Settings.PIECE_PATH);
                out.writeObject(save);
                out.close();
                fileOut.close();
                System.out.println("Saved !");
                if (player.isCanPlay()) {
                    player.startChrono();
                } else {
                    computer.startChrono();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        //Save is canceled
        else {
            p.startChrono();
        }
    }


    public LinkedList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(LinkedList<Piece> pieces) {
        this.pieces = pieces;
    }

    public AutreEventNotifieur getNotifieur() {
        return notifieur;
    }

    public void setNotifieur(AutreEventNotifieur notifieur) {
        this.notifieur = notifieur;
    }

    public LinkedList<Case> getCases() {
        return cases;
    }

    public void setCases(LinkedList<Case> cases) {
        this.cases = cases;
    }

    //Background task for timers
    public void timerSetup() {
        Timer timer = new Timer(100, e -> {
            notifieur.diffuserAutreEvent(new AutreEvent(this, "timer"));
            if (player.getTimeLeft() < 0) {
                isGameLost = true;
                resetGame();
            } else if (computer.getTimeLeft() < 0) {
                isGameWon = true;
                resetGame();
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    public Piece promotion(Piece piece) {
        Piece pq = null, pr = null, pn = null, pb = null;
        for (Piece c : player.getCaptured()) {
            if (c instanceof Queen) {
                pq = c;
            } else if (c instanceof Rook) {
                pr = c;
            } else if (c instanceof Knight) {
                pn = c;
            } else if (c instanceof Bishop) {
                pb = c;
            }
        }
        Promotion promotion = new Promotion();
        JComboBox<String> jComboBox = new JComboBox<>();
        if (pq != null)
            jComboBox.addItem("Queen");
        if (pr != null)
            jComboBox.addItem("Rook");
        if (pn != null)
            jComboBox.addItem("Knight");
        if (pb != null)
            jComboBox.addItem("Bishop");

        jComboBox.setBounds(125, 125, 150, 40);
        jComboBox.setVisible(true);
        promotion.add(jComboBox);
        JButton jButton = new JButton("Promote");
        jButton.setBounds(142, 200, 116, 30);
        jButton.addActionListener(e -> {
            if (e.getActionCommand().equals("Promote")) {
                promotion.setVisible(false);
                promotion.dispose();
            }
        });
        promotion.add(jButton);
        promotion.setLocationRelativeTo(null);
        promotion.setVisible(true);
        Piece p;

        if (jComboBox.getSelectedItem() == "Rook") {
            p = new Rook(piece.getxCase(), piece.getyCase(), piece.getColor(), piece.getPlayer());
            player.getCaptured().remove(pr);
        } else if (jComboBox.getSelectedItem() == "Knight") {
            p = new Knight(piece.getxCase(), piece.getyCase(), piece.getColor(), piece.getPlayer());
            player.getCaptured().remove(pn);
        } else if (jComboBox.getSelectedItem() == "Bishop") {
            p = new Bishop(piece.getxCase(), piece.getyCase(), piece.getColor(), piece.getPlayer());
            player.getCaptured().remove(pb);
        } else {
            p = new Queen(piece.getxCase(), piece.getyCase(), piece.getColor(), piece.getPlayer());
            player.getCaptured().remove(pq);
        }
        return p;
    }

    public Player getComputer() {
        return computer;
    }

    public void setComputer(Player computer) {
        this.computer = computer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void setGameWon(boolean gameWon) {
        isGameWon = gameWon;
    }

    public boolean isGameLost() {
        return isGameLost;
    }

    public void setGameLost(boolean gameLost) {
        isGameLost = gameLost;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
