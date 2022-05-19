package game.containers.non_lootables;

import static game.IoHandler.*;
import static game.assets.StringValues.*;

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
  public Container handleContainerJSONContents(JSONObject jsonObject) {
    return new Wall();
  }

  @Override
  public void onInteractListener(Contents inventory) {
    println(UI_WALL);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}