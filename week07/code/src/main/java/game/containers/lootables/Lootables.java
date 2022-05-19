package game.containers.lootables;

import game.GameSettings;
import game.containers.interfaces.Container;
import game.loot.*;
import org.json.simple.JSONObject;

import static game.IoHandler.*;
import static game.assets.StringValues.*;

public abstract class Lootables implements Container {
  private static final long serialVersionUID = 6592507137261779895L;

  private Contents contents = new Contents.Builder().build();

  public Contents getContents() {
    return contents;
  }

  public void setContents(Contents contents) {
    this.contents = contents;
  }

  @Override
  public void onInteractListener(Contents inventory) {
    attemptLoot(this);
  }

  public static Contents getContainerContents(Object jsonContents) {
    int goldAmount = -1;
    int doorKey = -1;
    int chestKey = -1;
    int flashlightCharge = -1;
    if (((JSONObject) jsonContents).get(JSON_WHICH).equals(JSON_WHICH_GOLD))
      goldAmount = jsonToInt(jsonContents, JSON_VALUE);
    else if (((JSONObject) jsonContents).get(JSON_WHICH).equals(JSON_WHICH_DOOR_KEY))
      doorKey = jsonToInt(jsonContents, JSON_VALUE);
    else if (((JSONObject) jsonContents).get(JSON_WHICH).equals(JSON_WHICH_CHEST_KEY))
      chestKey = jsonToInt(jsonContents, JSON_VALUE);
    else if (((JSONObject) jsonContents).get(JSON_WHICH).equals(JSON_WHICH_FLASHLIGHT))
      flashlightCharge = jsonToInt(jsonContents, JSON_VALUE);

    return new Contents.Builder()
        .withGold(goldAmount)
        .havingDoorKey(doorKey)
        .havingChestKey(chestKey)
        .containingFlashlight(flashlightCharge)
        .build();
  }

  public static int jsonToInt(Object jsonObject, String location) {
    return ((Long) ((JSONObject) jsonObject).get(location)).intValue();
  }

  public static void attemptLoot(Lootables lootables) {
    if (lootFailed(lootables)) println(UI_NO_LOOT);
  }

  public static boolean lootFailed(Lootables lootables) {
    if (isLootEmpty(lootables)) return true;

    lootGold(lootables);
    lootDoorKey(lootables);
    lootChestKey(lootables);
    lootFlashLight(lootables);

    return false;
  }

  public static boolean isLootEmpty(Lootables lootables) {
    return lootables.getContents().getGold().getAmount() == -1
        && lootables.getContents().getDoorKeys().isEmpty()
        && lootables.getContents().getChestKeys().isEmpty()
        && lootables.getContents().getFlashlight().getCharge() <= 0;
  }

  public static void lootGold(Lootables lootables) {
    Gold myGold = new Gold(GameSettings.getInventory().getGold().getAmount());
    Gold containerGold = new Gold(lootables.getContents().getGold().getAmount());
    if (containerGold.getAmount() != -1) {
      println(
          UI_GOLD_FOUND.get(0)
              + containerGold.getAmount()
              + UI_GOLD_FOUND.get(1)
              + (containerGold.getAmount() + myGold.getAmount())
              + UI_GOLD_FOUND.get(2));
      GameSettings.getInventory()
          .getGold()
          .setAmount(myGold.getAmount() + containerGold.getAmount());
      lootables.getContents().getGold().setAmount(-1);
    }
  }

  public static void lootDoorKey(Lootables lootables) {
    if (!lootables.getContents().getDoorKeys().isEmpty()) {
      GameSettings.getInventory().getDoorKeys().addAll(lootables.getContents().getDoorKeys());
      print(UI_DOOR_KEYS_FOUND);
      for (DoorKey key : lootables.getContents().getDoorKeys())
        print(key.getWhich() + FORMAT_SPACE);
      println();
      lootables.getContents().getDoorKeys().clear();
    }
  }

  public static void lootChestKey(Lootables lootables) {
    if (!lootables.getContents().getChestKeys().isEmpty()) {
      GameSettings.getInventory().getChestKeys().addAll(lootables.getContents().getChestKeys());
      println(UI_CHEST_KEYS_FOUND);
      for (ChestKey key : lootables.getContents().getChestKeys())
        print(key.getWhich() + FORMAT_SPACE);
      println();
      lootables.getContents().getChestKeys().clear();
    }
  }

  public static void lootFlashLight(Lootables lootables) {
    if (lootables.getContents().getFlashlight().getCharge() > 0) {
      GameSettings.getInventory()
          .getFlashlight()
          .setCharge(
              GameSettings.getInventory().getFlashlight().getCharge()
                  + lootables.getContents().getFlashlight().getCharge());
      println(
          UI_FLASHLIGHT_FOUND.get(0)
              + lootables.getContents().getFlashlight().getCharge()
              + UI_FLASHLIGHT_FOUND.get(1)
              + GameSettings.getInventory().getFlashlight().getCharge());
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}