package com.company.entity;

import java.awt.*;
import java.util.ArrayList;

public class Node {
    int value;
    Piece piece;
    Coordinates move;
    String[][] board;
    ArrayList<Node> children;
    Color color;

    public Node(Color color) {
        this.color = color;
        this.children = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Coordinates getMove() {
        return move;
    }

    public void setMove(Coordinates move) {
        this.move = move;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
}
