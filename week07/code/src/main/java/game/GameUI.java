package game;

import java.util.ArrayList;

import static game.MazeGame.runGame;
import static game.MazeGame.setControls;

public class GameUI implements BatchControls {

  static GameUI gameUI = new GameUI();

  /**
   * Pass commands here to automate them
   *
   * Static
   *
   * @param batchControls provide indefinite commands
   */
  public static void setBatchControls(char... batchControls) {
    // Other than entering commands in real-time, you can set predefined batch commands from here (optional)
    gameUI.setBatchControlsHidden(batchControls);
  }

  /**
   * Starts the game
   */
  public static void start() {
    runGame();
  }

  /**
   * Pass commands here to automate them
   *
   * Non-static
   *
   * @param batchControls provide indefinite commands
   */
  @Override
  public void setBatchControlsHidden(char... batchControls) {
    ArrayList<Character> batch = new ArrayList<>();
    for (char control : batchControls) batch.add(control);
    setControls(batch);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}