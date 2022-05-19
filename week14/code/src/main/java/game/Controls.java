package game;

import java.util.Random;

public class Controls {
    private static final char FORWARD = 'W';
    private static final char RIGHT = 'D';
    private static final char LEFT = 'A';
    private static final char BACKWARD = 'S';
    private static final char STATUS = 'T';
    private static final char EXIT = 'E';
    private static final char ENGAGE = 'K';
    private static final char ROCK = '1';
    private static final char PAPER = '2';
    private static final char SCISSORS = '3';


    private Controls() {
    }

    public static char getForwardKey() {
        return FORWARD;
    }

    public static char getRightKey() {
        return RIGHT;
    }

    public static char getLeftKey() {
        return LEFT;
    }

    public static char getBackwardKey() {
        return BACKWARD;
    }

    public static char getStatusKey() {
        return STATUS;
    }

    public static char getExitKey() {
        return EXIT;
    }

    public static char getEngageKey() { return ENGAGE; }

    public static char getRockKey() { return ROCK; }

    public static char getPaperKey() { return PAPER; }

    public static char getScissorsKey() { return SCISSORS; }

    public static char getRandomTieBreakerChoice() { return (new Random().nextInt(4) + " ").charAt(0); }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}