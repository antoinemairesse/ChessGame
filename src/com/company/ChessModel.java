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
    public Player computer;
    public Player player;
    public boolean isGameWon = false;
    public boolean isGameLost = false;

    public ChessModel() {
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
        createGame();
    }

    public ChessModel(FileInputStream fis) {
        createGameFromSave(fis);
    }

    public void move(Piece piece, int x, int y) {
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

    public void place(Piece piece, int x, int y) {
        Piece p;

        if (canMovePiece(x, y, piece)) {

            //if a piece is on the case we want to go
            if ((p = getPieceByCoords(x, y)) != null) {
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
                    } else if (p instanceof King && p.getColor().equals(Settings.SIDE)) {
                        isGameLost = true;
                        notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));
                        resetGame();
                        return;
                    } else if (!(p instanceof Pawn)) {
                        //Add captured piece to player
                        //We don't add pawns since they can't be used for pawn promotion
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
            } else if(piece.getyCase() == 7 && !(piece.getColor().equals(Settings.SIDE)) && piece instanceof Pawn){
                Piece t = computer.getCaptured().getFirst();
                computer.getCaptured().remove(t);
                t.setxCase(piece.getxCase());
                t.setyCase(piece.getyCase());
                t.setColor(piece.getColor());
                t.setPlayer(piece.getPlayer());
                t.setIcon();
                double xx =  ((piece.getxCase() - 1)*((double)Settings.REAL_WIDTH/Settings.WIDTH_CASES));
                double yy = ((piece.getyCase() - 1)*((double)Settings.REAL_HEIGHT/Settings.HEIGHT_CASES));
                t.setCoords(new Coordinates(xx,yy));
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

    public void computerMove() {
        /*computer choses a random piece and looks if it has any possible moves
          if it doesn't then we choose another piece again and again...
          if there is at least one move execute one randomly*/
        if (computer.isCanPlay()) {
            int lengthPieces = computer.getPieces().toArray().length - 1;
            int random = (int) (Math.random() * lengthPieces);
            LinkedList<Piece> pc = new LinkedList<>(computer.getPieces());
            Piece p = pc.get(random);
            p.nextPossibleMoves(this);
            System.out.println("1");
            while (p.getNextMoves().toArray().length <= 0) {
                System.out.println("***************");
                for(Piece t : pc){
                    System.out.print(t+" ");
                }
                System.out.println("");
                pc.remove(p);

                //Computer cannot make any legal move, player wins
                if (pc.isEmpty()) {
                    System.out.println("3");
                    isGameWon = true;
                    notifieur.diffuserAutreEvent(new AutreEvent(this, "place"));
                    resetGame();
                    return;
                }
                lengthPieces = pc.toArray().length - 1;
                random = (int) (Math.random() * lengthPieces);
                p = pc.get(random);
                p.nextPossibleMoves(this);
            }
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
            this.pieces = save.model.pieces;
            this.computer = save.model.computer;
            this.player = save.model.player;
            this.isGameWon = save.model.isGameWon;
            this.isGameLost = save.model.isGameLost;
            this.notifieur = save.model.notifieur;
            Settings.PLAYER_NAME = save.playerName;
            Settings.COMPUTER_NAME = save.computerName;
            Settings.CASE_SIZE = save.caseSize;
            Settings.SIDE = save.side;
            Settings.CASE_COLOR1 = save.caseColor1;
            Settings.CASE_COLOR2 = save.caseColor2;
            Settings.PIECE_PATH = save.piecePath;
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
        if (player.isChronoStarted()) {
            player.stopChrono();
        } else {
            computer.stopChrono();
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

    public Piece promotion(Piece piece) {
        boolean q = false, r = false, n = false, b = false;
        Piece pq = null, pr = null, pn = null, pb = null;
        for (Piece c : player.getCaptured()) {
            if (c instanceof Queen) {
                q = true;
                pq = c;
            } else if (c instanceof Rook) {
                r = true;
                pr = c;
            } else if (c instanceof Knight) {
                n = true;
                pn = c;
            } else if (c instanceof Bishop) {
                b = true;
                pb = c;
            }
        }
        Test test = new Test();
        JComboBox<String> jComboBox = new JComboBox<>();
        if (q)
            jComboBox.addItem("Queen");
        if (r)
            jComboBox.addItem("Rook");
        if (n)
            jComboBox.addItem("Knight");
        if (b)
            jComboBox.addItem("Bishop");
        jComboBox.setBounds(125, 125, 150, 40);
        jComboBox.setVisible(true);
        test.add(jComboBox);
        JButton jButton = new JButton("Promote");
        jButton.setBounds(142, 200, 116, 30);
        jButton.addActionListener(e -> {
            if (e.getActionCommand().equals("Promote")) {
                test.setVisible(false);
                test.dispose();
            }
        });
        test.add(jButton);
        test.setLocationRelativeTo(null);
        test.setVisible(true);
        System.out.println(jComboBox.getSelectedItem());
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
}
