package com.company.entity;

import java.awt.*;

public class Tree {
    private Node root;

    public Tree(Color color) {
        this.root = new Node(color);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
