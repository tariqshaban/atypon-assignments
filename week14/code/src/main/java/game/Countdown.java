package game;

import java.util.Timer;
import java.util.TimerTask;

import static game.IoHandler.printlnSys;
import static game.assets.StringValues.*;

public class Countdown implements Runnable {
    Timer timer = new Timer();
    private MazeGame mazeGame;
    private int startingCountdown;
    private int remaining;
    private boolean isStarted = false;

    public Countdown(MazeGame mazeGame) {
        this.mazeGame = mazeGame;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setStartingCountdown(int startingCountdown) {
        if (this.startingCountdown == 0) this.startingCountdown = startingCountdown;
        else printlnSys(translate("TIMER_ALREADY_SET"));
        remaining = startingCountdown;
    }

    private void startTimer() {
        isStarted = true;
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        updateRemaining();
                    }
                },
                1000,
                1000);
    }

    private void updateRemaining() {
        if (remaining < 0) {
            GamesList.getGamesList().remove(mazeGame);
            for (Player player : mazeGame.getPlayers().keySet())
                player.setMazeGame(null);
            mazeGame = null;
            timer.cancel();
        }
        --remaining;
    }

    @Override
    public void run() {
        startTimer();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + translate("TO_STRING_COUNTDOWN", 0)
                + remaining
                + translate("TO_STRING_COUNTDOWN", 1)
                + startingCountdown;
    }
}