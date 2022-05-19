import game.GameUI;

import static game.Controls.*;

public class MainActivity extends GameUI {

  public static void main(String[] args) {
    // Override default binding controls here, i.e. setForwardKey('8'); (optional)


    // Set predefined commands here, use: setBatchControls(getForwardKey(), ...),
    // this will automate the movement of the player and deducts 5 seconds for every move (optional)


    start();
  }
}