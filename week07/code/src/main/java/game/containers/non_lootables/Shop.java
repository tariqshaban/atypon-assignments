package game.containers.non_lootables;

import game.GameSettings;
import game.Store;
import game.containers.interfaces.Container;
import game.loot.ChestKey;
import game.loot.Contents;
import game.loot.DoorKey;
import game.MazeGame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static game.IoHandler.*;
import static game.IoHandler.println;
import static game.assets.StringValues.*;
import static game.containers.lootables.Lootables.getContainerContents;
import static game.containers.lootables.Lootables.jsonToInt;

public class Shop implements Container {
  private static final long serialVersionUID = 7457365249711164690L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  @Override
  public Container handleContainerJSONContents(JSONObject jsonObject) throws FileNotFoundException {
    if (Store.getStoreItems().isEmpty()) {
      final JSONObject fileJSON =
          (JSONObject) JSONValue.parse(new FileReader(MazeGame.getJsonDirectory()));
      final JSONArray jsonArray = (JSONArray) fileJSON.get(JSON_MERCHANT_STORE);
      for (Object deal : jsonArray) {
        Contents contents = getContainerContents(deal);
        if (contentIsNotEmpty(contents))
          Store.getStoreItems().put(contents, jsonToInt(deal, JSON_PRICE));
      }
    }
    return new Shop();
  }

  private boolean contentIsNotEmpty(Contents contents) {
    return !contents.getDoorKeys().isEmpty()
        || !contents.getChestKeys().isEmpty()
        || contents.getGold().getAmount() > 0
        || contents.getFlashlight().getCharge() > 0;
  }

  @Override
  public void onInteractListener(Contents inventory) {
    viewShopItems();
  }

  private static void viewShopItems() {
    int itemsViewed = 0;
    println();
    println(SHOP_TITLE);
    println(FORMAT_SEPARATOR);
    for (Contents item : Store.getStoreItems().keySet()) {

      print((itemsViewed + 1) + FORMAT_COLON);
      itemsViewed++;

      if (!item.getDoorKeys().isEmpty())
        for (DoorKey dKey : item.getDoorKeys())
          print(SHOP_DOOR_KEYS + dKey.getWhich() + FORMAT_TAB + FORMAT_TAB + FORMAT_TAB);
      if (!item.getChestKeys().isEmpty())
        for (ChestKey cKey : item.getChestKeys())
          print(SHOP_CHEST_KEYS + cKey.getWhich() + FORMAT_SPACE + FORMAT_TAB + FORMAT_TAB);
      if (item.getFlashlight().getCharge() > 0)
        print(SHOP_FLASHLIGHT.get(0) + item.getFlashlight().getCharge() + SHOP_FLASHLIGHT.get(1));
      println(FORMAT_TAB + FORMAT_TAB + Store.getStoreItems().get(item) + SHOP_GOLD);
    }
    if (itemsViewed == 0) {
      println(SHOP_EMPTY);
      return;
    }

    println(SHOP_EXIT);

    promptPurchase();
  }

  private static void promptPurchase() {
    while (true) {

      if (MazeGame.getBatchControls().isEmpty()) MazeGame.setCommand(getUserInput());
      else {
        MazeGame.setCommand(Character.toUpperCase(MazeGame.getBatchControls().get(0)));
        MazeGame.getBatchControls().remove(0);
      }

      if (!Character.isDigit(MazeGame.getCommand())) {
        println(UI_INVALID_SELECTION);
      } else {
        int choice = Integer.parseInt((MazeGame.getCommand() + FORMAT_SPACE).substring(0, 1));

        if ((choice > Store.getStoreItems().size())) println(UI_INVALID_SELECTION);
        else if (choice != 0) {
          makeTransaction(choice);
        }
        break;
      }
    }
  }

  private static void makeTransaction(int choice) {
    Contents chosenItem = (Contents) Store.getStoreItems().keySet().toArray()[choice - 1];
    int chosenItemPrice = (Integer) Store.getStoreItems().values().toArray()[choice - 1];

    if (chosenItemPrice <= GameSettings.getInventory().getGold().getAmount()) {
      print(SHOP_YOU_BOUGHT);

      if (!chosenItem.getDoorKeys().isEmpty())
        for (DoorKey dKey : chosenItem.getDoorKeys()) {
          println(SHOP_DOOR_KEYS + dKey.getWhich());
          GameSettings.getInventory().addDoorKey(dKey.getWhich());
        }
      if (!chosenItem.getChestKeys().isEmpty())
        for (ChestKey cKey : chosenItem.getChestKeys()) {
          println(SHOP_CHEST_KEYS + cKey.getWhich());
          GameSettings.getInventory().addChestKey(cKey.getWhich());
        }
      if (chosenItem.getFlashlight().getCharge() > 0) {
        print(
            SHOP_FLASHLIGHT.get(0)
                + chosenItem.getFlashlight().getCharge()
                + SHOP_FLASHLIGHT.get(1));
        GameSettings.getInventory()
            .getFlashlight()
            .setCharge(
                GameSettings.getInventory().getFlashlight().getCharge()
                    + chosenItem.getFlashlight().getCharge());
      }

      println(SHOP_YOU_BOUGHT_FOR.get(0) + chosenItemPrice + SHOP_YOU_BOUGHT_FOR.get(1));
      GameSettings.getInventory()
          .getGold()
          .setAmount(GameSettings.getInventory().getGold().getAmount() - chosenItemPrice);

      Store.getStoreItems().remove(chosenItem);

    } else println(SHOP_INSUFFICIENT);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}