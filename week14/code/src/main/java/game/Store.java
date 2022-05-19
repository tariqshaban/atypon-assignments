package game;

import game.loot.Contents;
import java.util.LinkedHashMap;
import java.util.Map;

import static game.assets.StringValues.*;

public class Store {

  private static final String TO_STRING = "TO_STRING_STORE";

  private final LinkedHashMap<Contents, Integer> storeItems = new LinkedHashMap<>();

  public Map<Contents, Integer> getStoreItems() {
    return storeItems;
  }

  public String getStoreItemsString() {
    StringBuilder items = new StringBuilder();
    for (Map.Entry<Contents, Integer> contents : storeItems.entrySet())
      items
          .append(contents.getKey().toString())
          .append(translate(TO_STRING,0))
          .append(contents.getValue())
          .append(translate(TO_STRING,1));
    return Store.class.getSimpleName() + translate(TO_STRING,2) + items;
  }

  @Override
  public String toString() {
    return getStoreItemsString();
  }
}