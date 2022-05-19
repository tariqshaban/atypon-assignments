package game.exceptions;

import static game.IoHandler.println;
import static game.MazeGame.printStatus;
import static game.assets.StringValues.*;

public class TimeUpException extends Exception {
  public TimeUpException() {
    super();
    println(FORMAT_NEW_DIALOG);
    println(TIMER_DONE);
    printStatus();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_TIME_UP;
  }
}