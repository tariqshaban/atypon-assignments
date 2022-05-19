package game;

import game.loot.Contents;
import java.util.LinkedHashMap;
import java.util.Map;

import static game.assets.StringValues.*;

public class Store {
  private static LinkedHashMap<Contents, Integer> storeItems = new LinkedHashMap<>();

  private Store() {}

  public static void setStoreItems(Map<Contents, Integer> storeParam) {
    storeItems = (LinkedHashMap<Contents, Integer>) storeParam;
  }

  public static Map<Contents, Integer> getStoreItems() {
    return storeItems;
  }

  public static String getStoreItemsString() {
    StringBuilder items = new StringBuilder();
    for (Map.Entry<Contents, Integer> contents : storeItems.entrySet())
      items
          .append(contents.getKey().toString())
          .append(TO_STRING_STORE.get(0))
          .append(contents.getValue())
          .append(TO_STRING_STORE.get(1));
    return Store.class.getSimpleName() + TO_STRING_STORE.get(2) + items;
  }

  @Override
  public String toString() {
    return getStoreItemsString();
  }
}