package game;

import game.containers.interfaces.Container;
import game.containers.Room;
import game.loot.Contents;

import java.util.ArrayList;
import java.util.List;

import static game.assets.StringValues.*;

public class GameSettings implements java.io.Serializable {
  private static final long serialVersionUID = 4335769946171743216L;

  private static int goldThresholdObjective = 999999;
  private static Contents inventory = new Contents.Builder().build();
  private static List<Room<Container>> rooms = new ArrayList<>();

  private GameSettings() {}

  public static int getGoldThresholdObjective() {
    return goldThresholdObjective;
  }

  public static void setGoldThresholdObjective(int goldThresholdObjectiveParam) {
    goldThresholdObjective = goldThresholdObjectiveParam;
  }

  public static List<Room<Container>> getRooms() {
    return rooms;
  }

  public static void setRooms(List<Room<Container>> roomsParam) {
    rooms = roomsParam;
  }

  public static Contents getInventory() {
    return inventory;
  }

  public static void setInventory(Contents inventory) {
    GameSettings.inventory = inventory;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_GAME_SETTINGS.get(0)
        + goldThresholdObjective
        + TO_STRING_GAME_SETTINGS.get(1)
        + inventory.toString();
  }
}