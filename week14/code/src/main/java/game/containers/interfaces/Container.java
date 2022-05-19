package game.containers.interfaces;

import game.MazeGame;
import game.Player;
import game.loot.Contents;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public interface Container extends java.io.Serializable {

  String getDisplayName();

  Container handleContainerJSONContents(MazeGame mazeGame, JSONObject jsonObject) throws FileNotFoundException;

  void onInteractListener(MazeGame mazeGame, Player player, Contents inventory);
}