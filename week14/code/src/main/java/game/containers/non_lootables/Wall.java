package game.containers.non_lootables;

import static game.assets.StringValues.*;

import game.MazeGame;
import game.Player;
import game.containers.interfaces.Container;
import game.loot.Contents;
import org.json.simple.JSONObject;

public class Wall implements Container {
  private static final long serialVersionUID = 3040187407646395031L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  @Override
  public Container handleContainerJSONContents(MazeGame mazeGame, JSONObject jsonObject) {
    return new Wall();
  }

  @Override
  public void onInteractListener(MazeGame mazeGame, Player player, Contents inventory) {
    player.println(translate("UI_WALL"));
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}