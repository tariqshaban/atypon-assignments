package game.containers.lootables;

import game.containers.interfaces.Container;
import org.json.simple.JSONObject;

import static game.assets.StringValues.*;

public class Painting extends Lootables {
  private static final long serialVersionUID = 7866619388737459481L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  @Override
  public Container handleContainerJSONContents(JSONObject jsonObject) {
    Painting painting = new Painting();
    painting.setContents(getContainerContents(jsonObject));
    return painting;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+FORMAT_TAB+getContents().toString();
  }
}