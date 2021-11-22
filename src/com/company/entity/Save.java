package com.company.entity;

import com.company.ChessModel;

import java.awt.*;
import java.io.Serializable;

public class Save implements Serializable {
    public ChessModel model;
    public String playerName;
    public String computerName;
    public int caseSize;
    public Color side;
    public Color caseColor1;
    public Color caseColor2;
    public String piecePath;

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
}
