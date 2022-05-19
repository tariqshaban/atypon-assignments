package game.containers.interfaces;

import game.loot.Contents;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public interface Container extends java.io.Serializable {

  String getDisplayName();

  Container handleContainerJSONContents(JSONObject jsonObject) throws FileNotFoundException;

  void onInteractListener(Contents inventory);
}