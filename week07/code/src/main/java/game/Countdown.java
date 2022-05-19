package game;

import game.exceptions.TimeUpException;

import java.util.Timer;
import java.util.TimerTask;

import static game.IoHandler.println;
import static game.assets.StringValues.*;

public class Countdown implements Runnable {
  private int startingCountdown;
  private int remaining;
  private boolean isStarted = false;
  private boolean isObjectiveCompleted = false;
  private Timer timer;

  public boolean isStarted() {
    return isStarted;
  }

  public void setIsObjectiveCompleted(boolean isObjectiveCompleted) {
    this.isObjectiveCompleted = isObjectiveCompleted;
  }

  public int getRemaining() {
    return remaining;
  }

  public void setRemaining(int remaining) {
    this.remaining = remaining;
  }

  public void setStartingCountdown(int startingCountdown) {
    if (this.startingCountdown == 0) this.startingCountdown = startingCountdown;
    else println(TIMER_ALREADY_SET);
    remaining = startingCountdown;
  }

  public void resetTimer() {
    remaining = startingCountdown;
  }

  public void markAutomationPenalty() {
    remaining -= 5;
  }

  private void startTimer() {
    isStarted = true;
    timer = new Timer();
    println(
        TIMER_START_DIALOG.get(0)
            + startingCountdown
            + TIMER_START_DIALOG.get(1)
            + GameSettings.getGoldThresholdObjective()
            + TIMER_START_DIALOG.get(2));
    timer.scheduleAtFixedRate(
        new TimerTask() {
          public void run() {
            if (isObjectiveCompleted) timer.cancel();

            try {
              updateRemaining();
            } catch (TimeUpException e) {
              System.exit(1);
            }
          }
        },
        1000,
        1000);
  }

  private void updateRemaining() throws TimeUpException {
    if (remaining == 60)
      println(
          TIMER_60_SECS.get(0)
              + (GameSettings.getGoldThresholdObjective()
                  - GameSettings.getInventory().getGold().getAmount())
              + TIMER_60_SECS.get(1));
    else if (remaining == 5) println(TIMER_5_SECS);
    else if (remaining < 1) {
      remaining = 0;
      timer.cancel();
      throw new TimeUpException();
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
        + TO_STRING_COUNTDOWN.get(0)
        + remaining
        + TO_STRING_COUNTDOWN.get(1)
        + startingCountdown;
  }
}