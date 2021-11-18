package com.company;

import java.awt.*;

public final class Settings {
    public static final Color SIDE = Color.BLACK;   //Color.WHITE or Color.BLACK
    public static final int CASE_SIZE = 100;
    public static final int WIDTH = (CASE_SIZE*8);
    public static final int HEIGHT = (CASE_SIZE*7);
    public static final int REAL_WIDTH = WIDTH-16; // without window border
    public static final int REAL_HEIGHT = HEIGHT-35; // without window border
    public static final int HEIGHT_CASES = 7;
    public static final int WIDTH_CASES = 8;
    public static final int RATIO_WIDTH_CASES = (Settings.REAL_WIDTH / Settings.WIDTH_CASES);
    public static final int RATIO_HEIGHT_CASES = (Settings.REAL_HEIGHT / Settings.HEIGHT_CASES);
    public static final Color CASE_COLOR1 = Color.decode("#769656");
    public static final Color CASE_COLOR2 = Color.decode("#EEEED2");
    public static final double PLAY_TIME = 180.0; // time in seconds
    public static final String PLAYER_NAME = "Antoine";
    public static final String COMPUTER_NAME = "Machine";

    private Settings(){
    }
}
