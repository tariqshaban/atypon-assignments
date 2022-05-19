package game.containers.lootables;

import game.containers.interfaces.Container;
import org.json.simple.JSONObject;

import static game.assets.StringValues.*;

public class Mirror extends Lootables {
  private static final long serialVersionUID = 2064032560879967140L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  @Override
  public Container handleContainerJSONContents(JSONObject jsonObject) {
    Mirror mirror = new Mirror();
    mirror.setContents(getContainerContents(jsonObject));
    return mirror;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+FORMAT_TAB+getContents().toString();
  }
}