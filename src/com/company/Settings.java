package com.company;

import java.awt.*;

public final class Settings {
    public static String PIECE_PATH = "resources/Bases/";
    public static Color SIDE = Color.WHITE;   //Color.WHITE or Color.BLACK
    public static int CASE_SIZE = 100;
    public static int WIDTH = (CASE_SIZE*8);
    public static int HEIGHT = (CASE_SIZE*7);
    public static int REAL_WIDTH = WIDTH-16; // without window border
    public static int REAL_HEIGHT = HEIGHT-35; // without window border
    public static int HEIGHT_CASES = 7;
    public static int WIDTH_CASES = 8;
    public static int RATIO_WIDTH_CASES = (Settings.REAL_WIDTH / Settings.WIDTH_CASES);
    public static int RATIO_HEIGHT_CASES = (Settings.REAL_HEIGHT / Settings.HEIGHT_CASES);
    public static Color CASE_COLOR1 = Color.decode("#4B7399");
    public static Color CASE_COLOR2 = Color.decode("#EAE9D2");
    public static double PLAY_TIME = 180.0; // time in seconds
    public static String PLAYER_NAME = "Player";
    public static String COMPUTER_NAME = "Computer";

    private Settings(){
    }

    public static void reCalculate(){
        WIDTH = (CASE_SIZE*8);
        HEIGHT = (CASE_SIZE*7);
        REAL_WIDTH = WIDTH-16;
        REAL_HEIGHT = HEIGHT-35;
        RATIO_WIDTH_CASES = (Settings.REAL_WIDTH / Settings.WIDTH_CASES);
        RATIO_HEIGHT_CASES = (Settings.REAL_HEIGHT / Settings.HEIGHT_CASES);
    }
}
