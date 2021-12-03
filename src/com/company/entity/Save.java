package com.company.entity;

import com.company.ChessModel;

import java.awt.*;
import java.io.Serializable;

public class Save implements Serializable {
    private ChessModel model;
    private String playerName;
    private String computerName;
    private int caseSize;
    private Color side;
    private Color caseColor1;
    private Color caseColor2;
    private String piecePath;

    public Save(ChessModel model, String playerName, String computerName, int caseSize, Color side, Color caseColor1, Color caseColor2, String piecePath) {
        this.model = model;
        this.playerName = playerName;
        this.computerName = computerName;
        this.caseSize = caseSize;
        this.side = side;
        this.caseColor1 = caseColor1;
        this.caseColor2 = caseColor2;
        this.piecePath = piecePath;
    }

    public ChessModel getModel() {
        return model;
    }

    public void setModel(ChessModel model) {
        this.model = model;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public int getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(int caseSize) {
        this.caseSize = caseSize;
    }

    public Color getSide() {
        return side;
    }

    public void setSide(Color side) {
        this.side = side;
    }

    public Color getCaseColor1() {
        return caseColor1;
    }

    public void setCaseColor1(Color caseColor1) {
        this.caseColor1 = caseColor1;
    }

    public Color getCaseColor2() {
        return caseColor2;
    }

    public void setCaseColor2(Color caseColor2) {
        this.caseColor2 = caseColor2;
    }

    public String getPiecePath() {
        return piecePath;
    }

    public void setPiecePath(String piecePath) {
        this.piecePath = piecePath;
    }
}
