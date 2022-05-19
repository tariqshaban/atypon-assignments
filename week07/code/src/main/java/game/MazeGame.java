package game;

import game.containers.*;
import game.containers.interfaces.Container;
import game.containers.interfaces.ContainerWithReset;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Lootables;
import game.exceptions.ExceptionInfoHolder;
import game.exceptions.IllogicalMapping;
import game.loot.ChestKey;
import game.loot.Contents;
import game.loot.DoorKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.*;

import static game.Controls.*;
import static game.GameSettings.*;
import static game.IoHandler.*;
import static game.Store.*;
import static game.assets.StringValues.*;

public class MazeGame {

  private static String JsonDirectory;
  private static List<Character> batchControls = new ArrayList<>();
  private static char command;
  private static char facing = getRightKey();
  private static Room<Container> location;
  private static Room<Container> lastKnownLitRoom;
  private static boolean isFlashlightPermissionGranted = false;

  private static final Countdown countdown = new Countdown();
  private static final Thread thread = new Thread(countdown);

  private MazeGame() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static String getJsonDirectory() {
    return JsonDirectory;
  }

  public static List<Character> getBatchControls() {
    return batchControls;
  }

  public static char getCommand() {
    return command;
  }

  public static void setCommand(char command) {
    MazeGame.command = command;
  }

  public static char getFacing() {
    return facing;
  }

  public static void setFacing(char facing) {
    MazeGame.facing = facing;
  }

  public static Room<Container> getLocation() {
    return location;
  }

  public static void setLocation(Room<Container> location) {
    MazeGame.location = location;
  }

  public static Room<Container> getLastKnownLitRoom() {
    return lastKnownLitRoom;
  }

  public static void setLastKnownLitRoom(Room<Container> lastKnownLitRoom) {
    if (!lastKnownLitRoom.isDark()) MazeGame.lastKnownLitRoom = lastKnownLitRoom;
  }

  public static boolean isIsFlashlightPermissionGranted() {
    return isFlashlightPermissionGranted;
  }

  public static void setIsFlashlightPermissionGranted(boolean isFlashlightPermissionGranted) {
    MazeGame.isFlashlightPermissionGranted = isFlashlightPermissionGranted;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected static void runGame() {
    resetValues();

    try {
      decodeJSON();
    } catch (FileNotFoundException e) {
      println(EXCEPTION_JSON_ERR_FILE_NOT_FOUND);
      System.exit(3000);
    } catch (Exception e) {
      println(EXCEPTION_JSON_ERR);
      println(FORMAT_SEPARATOR);
      println(e.getMessage());
      println(EXCEPTION_LINE + e.getStackTrace()[0].getLineNumber());
      System.exit(3001);
    }
    try {
      checkMappingLogic();
    } catch (IllogicalMapping e) {
      System.exit(3002);
    }
    startGame();
  }

  private static void resetValues() {
    countdown.resetTimer();

    facing = getRightKey();
    isFlashlightPermissionGranted = false;

    for (ContainerWithReset containerWithReset : ContainerList.getGameResetList().values())
      containerWithReset.onGameResetListener();

    getStoreItems().clear();
    setInventory(new Contents.Builder().build());
    getRooms().clear();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static void decodeJSON() throws FileNotFoundException {

    if (countdown.getRemaining() == 0) {
      JsonDirectory = JSON_DIR;

      File folder = new File(MAP_DIRECTORY);
      File[] listOfFiles = folder.listFiles();

      if (listOfFiles.length == 0) println(SERIALIZE_NO_MAPS_FOUND);
      else {
        println(MAP_SELECT);
        println(FORMAT_SEPARATOR);
        for (int i = 0; i < listOfFiles.length; i++)
          if (listOfFiles[i].isFile()) {
            println((i + 1) + FORMAT_DOT + FORMAT_SPACE + listOfFiles[i].getName());
          }
        int option = getSelectedOption(1, listOfFiles.length);
        println(FORMAT_NEW_DIALOG);

        JsonDirectory = listOfFiles[option - 1].getPath();
      }
    }

    JSONObject fileJSON = (JSONObject) JSONValue.parse(new FileReader(JsonDirectory));

    JSONArray roomsJSON = (JSONArray) fileJSON.get(JSON_ROOMS);

    if (countdown.getRemaining() == 0)
      countdown.setStartingCountdown(jsonToInt(fileJSON, JSON_REMAINING_TIME));
    getInventory().getGold().setAmount(jsonToInt(fileJSON, JSON_INITIAL_GOLD));
    setGoldThresholdObjective(jsonToInt(fileJSON, JSON_GOLD_THRESHOLD));

    for (Object room : roomsJSON) {
      boolean isDark = (boolean) ((JSONObject) room).get(JSON_IS_DARK);

      String eastContainerType = getContainerType(room, JSON_EAST);
      String northContainerType = getContainerType(room, JSON_NORTH);
      String westContainerType = getContainerType(room, JSON_WEST);
      String southContainerType = getContainerType(room, JSON_SOUTH);

      getRooms()
          .add(
              new Room<>(
                  isDark,
                  getContainer(((JSONObject) room).get(JSON_EAST), eastContainerType),
                  getContainer(((JSONObject) room).get(JSON_NORTH), northContainerType),
                  getContainer(((JSONObject) room).get(JSON_WEST), westContainerType),
                  getContainer(((JSONObject) room).get(JSON_SOUTH), southContainerType)));
    }
  }

  public static String getContainerType(Object room, String json) {
    JSONObject child = (JSONObject) ((JSONObject) room).get(json);
    return (String) child.get(JSON_TYPE);
  }

  public static int jsonToInt(Object jsonObject, String location) {
    return ((Long) ((JSONObject) jsonObject).get(location)).intValue();
  }

  private static Container getContainer(Object roomSide, String flag) throws FileNotFoundException {
    for (Container container : ContainerList.getContainersList().values()) {
      if (container
          .getClass()
          .isAssignableFrom(ContainerList.getContainersList().get(flag).getClass())) {
        return container.handleContainerJSONContents((JSONObject) roomSide);
      }
    }
    return ContainerList.getContainersList().entrySet().iterator().next().getValue();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void setControls(List<Character> batchControls) {
    MazeGame.batchControls = getValidControls(batchControls);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static void checkMappingLogic() throws IllogicalMapping {

    if (countdown.getRemaining() < 60)
      throw new IllogicalMapping(EXCEPTION_ILLOGICAL_REMAINING_TIME);

    if (getInventory().getGold().getAmount() < 0)
      throw new IllogicalMapping(EXCEPTION_ILLOGICAL_STARTING_GOLD_NEGATIVE);

    if (getGoldThresholdObjective() <= getInventory().getGold().getAmount())
      throw new IllogicalMapping(EXCEPTION_ILLOGICAL_STARTING_GOLD);

    if (isGoldInsufficient()) throw new IllogicalMapping(EXCEPTION_ILLOGICAL_GOLD_SUM);

    if (getRooms().get(0).isDark())
      throw new IllogicalMapping(EXCEPTION_ILLOGICAL_SPAWN_ROOM_LIGHTNING);

    ExceptionInfoHolder exceptionInfoHolder;
    for (ContainerWithLogicCheck containerWithLogicCheck :
        ContainerList.getLogicCheckList().values()) {
      exceptionInfoHolder = containerWithLogicCheck.isLogical();
      if (!exceptionInfoHolder.isSuccess())
        throw new IllogicalMapping(exceptionInfoHolder.getReason());
    }
  }

  private static boolean isGoldInsufficient() {
    List<Container> containerList = new ArrayList<>();
    int totalGold = getInventory().getGold().getAmount();
    int containerGold;

    for (Room<Container> room : getRooms()) {
      containerList.add(room.getEast());
      containerList.add(room.getNorth());
      containerList.add(room.getWest());
      containerList.add(room.getSouth());

      for (Container container : containerList)
        if (container instanceof Lootables) {
          containerGold = ((Lootables) container).getContents().getGold().getAmount();
          if (containerGold != -1) totalGold += containerGold;
        }

      containerList.clear();
    }

    for (int i : Store.getStoreItems().values()) totalGold -= i;

    return totalGold < getGoldThresholdObjective();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static void startGame() {

    location = getRooms().get(0);
    drawMap(location, false);
    lastKnownLitRoom = location;

    if (!countdown.isStarted()) thread.start();

    while (getInventory().getGold().getAmount() < getGoldThresholdObjective()) {

      // Ensures that the thread is fired, hence, avoiding automated commands stream from colliding
      // with the timer
      if (thread.getState() != Thread.State.TERMINATED) continue;

      println(UI_AWAITING_COMMAND);

      if (batchControls.isEmpty()) {
        command = getUserInput();
      } else {
        if (countdown.getRemaining() >= 5) {
          command = Character.toUpperCase(batchControls.get(0));
          batchControls.remove(0);
          countdown.markAutomationPenalty();
        } else batchControls.clear(); // Avoid excessive automated commands when there is no time left
      }

      if (command == getStatusKey()) {
        printStatus();
      } else if (command == getResetKey()) {
        println(FORMAT_NEW_DIALOG);
        println(UI_RESET);
        runGame();
        break;
      } else if (command == getCheckpointKey()) {
        serialize();
      } else if (command == getLoadKey()) {
        deserialize();
      } else if (isClearForManeuver()) {
        handleManeuver();
      }
    }

    println(FORMAT_NEW_DIALOG);
    println(UI_OBJECTIVE_COMPLETE);
    printStatus();
    countdown.setIsObjectiveCompleted(true);
  }

  private static void serialize() {

    int num = 1;
    String save = SERIALIZE_FILE_NAME.get(0) + SERIALIZE_FILE_NAME.get(1);
    File file = new File(SERIALIZE_DIRECTORY, save);
    while (file.exists()) {
      save = SERIALIZE_FILE_NAME.get(0) + (num++) + SERIALIZE_FILE_NAME.get(1);
      file = new File(SERIALIZE_DIRECTORY, save);
    }

    try (FileOutputStream fileOut = new FileOutputStream(SERIALIZE_DIRECTORY + save);
        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

      out.writeObject(getRooms());
      out.writeObject(getInventory());
      out.writeObject(getGoldThresholdObjective());
      out.writeObject(getStoreItems());
      out.writeObject(countdown.getRemaining());
      out.writeObject(location);
      out.writeObject(lastKnownLitRoom);

      println(SERIALIZE_CHECKPOINT_MADE);
    } catch (IOException e) {
      println(SERIALIZE_CHECKPOINT_FAILED);
    }
  }

  private static void deserialize() {

    File folder = new File(SERIALIZE_DIRECTORY);
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles.length == 0) println(SERIALIZE_NO_CHECKPOINTS_FOUND);
    else {
      println(FORMAT_SEPARATOR);
      for (int i = 0; i < listOfFiles.length; i++)
        if (listOfFiles[i].isFile()) {
          println((i + 1) + FORMAT_DOT + FORMAT_SPACE + listOfFiles[i].getName());
        }
      println(SERIALIZE_EXIT);
      println(FORMAT_SEPARATOR);
      int option = getSelectedOption(0, listOfFiles.length);
      if (option == 0) return;
      String selectedFile = listOfFiles[option - 1].getPath();

      try (FileInputStream fileIn = new FileInputStream(selectedFile);
          ObjectInputStream in = new ObjectInputStream(fileIn)) {
        readCheckpointFile(in);
      } catch (IOException e) {
        println(EXCEPTION_FILE_NOT_FOUND);
      }
    }
  }

  // There is no other way to remove this warning; it is ensured that the objects in the file are
  // the same type as the formal parameters,
  // wildcards could be used; but sonarlint does not comply with it
  @SuppressWarnings("unchecked")
  private static void readCheckpointFile(ObjectInputStream in) {
    try {
      setRooms((List<Room<Container>>) in.readObject());
      setInventory((Contents) in.readObject());
      setGoldThresholdObjective((Integer) in.readObject());
      setStoreItems((Map<Contents, Integer>) in.readObject());
      countdown.setRemaining((Integer) in.readObject());
      location = (Room<Container>) in.readObject();
      lastKnownLitRoom = (Room<Container>) in.readObject();
      println(FORMAT_NEW_DIALOG);
      drawMap(location, false);
      println(SERIALIZE_CHECKPOINT_LOADED);
    } catch (InvalidClassException e) {
      println(EXCEPTION_CLASS_MODIFIED);
    } catch (IOException e) {
      println(EXCEPTION_SERIALIZE_FILE_ALTERED);
    } catch (ClassNotFoundException e) {
      println(EXCEPTION_SERIALIZE_ASSIGNING_FAILED);
    }
  }

  private static int getSelectedOption(int start, int length) {
    while (true) {

      if (getBatchControls().isEmpty()) setCommand(getUserInput());
      else {
        setCommand(Character.toUpperCase(getBatchControls().get(0)));
        getBatchControls().remove(0);
      }

      if (!Character.isDigit(getCommand())) {
        println(UI_INVALID_SELECTION);
      } else {
        int choice = Integer.parseInt((getCommand() + FORMAT_SPACE).substring(0, 1));

        if ((choice > length || choice < start)) println(UI_INVALID_SELECTION);
        else return choice;
      }
    }
  }

  private static boolean isClearForManeuver() {
    if (!isFlashlightPermissionGranted
        && location.isDark()
        && getInventory().getFlashlight().getCharge() > 0) {
      isFlashlightPermissionGranted = requestFlashlightGranted();
      return false;
    }

    if ((!isFlashlightPermissionGranted || getInventory().getFlashlight().getCharge() <= 0)
        && location.isDark()) {
      blinded();
      return false;
    }

    if (isFlashlightPermissionGranted && location.isDark()) {
      getInventory().getFlashlight().setCharge(getInventory().getFlashlight().getCharge() - 5);
      println(UI_FLASHLIGHT_CHARGE_IS + getInventory().getFlashlight().getCharge());
    }

    return true;
  }

  private static void handleManeuver() {

    if (command == getRightKey()) interactGeneric(location.getEast());
    else if (command == getForwardKey()) interactGeneric(location.getNorth());
    else if (command == getLeftKey()) interactGeneric(location.getWest());
    else if (command == getBackwardKey()) interactGeneric(location.getSouth());
    else println(UI_INVALID_COMMAND);
  }

  private static boolean requestFlashlightGranted() {
    println(UI_REQUEST_FLASHLIGHT);
    while (true) {
      command = getUserInput();
      if (command == getAffirmativeKey()) {
        println(FORMAT_NEW_DIALOG);
        drawMap(location, true);
        return true;
      } else if (command == getNegativeKey()) {
        blinded();
        return false;
      }
      println(UI_INVALID_SELECTION);
    }
  }

  private static void blinded() {
    println(FORMAT_NEW_DIALOG);
    println(UI_BLIND);
    location = lastKnownLitRoom;
    drawMap(location, false);
  }

  ///////////////////////////////////////////////////////////

  private static void interactGeneric(Container container) {

    facing = command;

    container.onInteractListener(getInventory());
  }

  ///////////////////////////////////////////////////////////

  public static void drawMap(Room<Container> location, boolean isCharted) {
    println(FORMAT_SEPARATOR);
    String east = location.getEast().getDisplayName();
    String north = location.getNorth().getDisplayName();
    String west = location.getWest().getDisplayName();
    String south = location.getSouth().getDisplayName();

    if (location.isDark() && !isCharted) {
      east = FORMAT_UNKNOWN;
      north = FORMAT_UNKNOWN;
      west = FORMAT_UNKNOWN;
      south = FORMAT_UNKNOWN;
    }

    for (int i = 0; i < 7; i++) {
      print(FORMAT_TAB + FORMAT_TAB);
      if (i == 0) {
        printMapTop(north);
      } else if (i == 6) {
        printMapBottom(south);
      } else if (i == 3) {
        printMapMiddle(east, west);
      } else {
        printMapBody();
      }
    }
    println(FORMAT_SEPARATOR);
  }

  private static void printMapTop(String north) {
    print(FORMAT_SPACE);
    for (int j = 0; j < (20 - north.length()) / 2; j++) print(FORMAT_STAR);
    print(north);
    for (int j = 0; j < (20 - north.length()) / 2; j++) print(FORMAT_STAR);
    println();
  }

  private static void printMapBottom(String south) {
    print(FORMAT_SPACE);
    for (int j = 0; j < (20 - south.length()) / 2; j++) print(FORMAT_STAR);
    print(south);
    for (int j = 0; j < (20 - south.length()) / 2; j++) print(FORMAT_STAR);
    println();
  }

  private static void printMapMiddle(String east, String west) {
    print(west);
    for (int j = 0; j < 18 - (west.length() + east.length()) / 2 + 1; j++) print(FORMAT_SPACE);
    println(east);
  }

  private static void printMapBody() {
    print(FORMAT_SPACE);
    print(FORMAT_STAR);
    for (int j = 0; j < 18; j++) print(FORMAT_SPACE);
    println(FORMAT_STAR);
  }

  ///////////////////////////////////////////////////////////

  public static void printStatus() {
    println();
    println(STATUS_TITLE);
    println(FORMAT_SEPARATOR);
    println(
        STATUS_GOLD
            + getInventory().getGold().getAmount()
            + FORMAT_DIVISION
            + getGoldThresholdObjective());
    println(FORMAT_SEPARATOR);
    if (!getInventory().getDoorKeys().isEmpty() || !getInventory().getChestKeys().isEmpty()) {
      print(STATUS_KEYS);
      if (!getInventory().getDoorKeys().isEmpty()) {
        print(STATUS_DOOR_KEYS);
        for (DoorKey doorKey : getInventory().getDoorKeys()) print(FORMAT_TAB + doorKey.getWhich());
      }
      if (!getInventory().getChestKeys().isEmpty()) {
        println();
        print(FORMAT_TAB);
        print(STATUS_CHEST_KEYS);
        for (ChestKey chestKey : getInventory().getChestKeys())
          print(FORMAT_TAB + chestKey.getWhich());
      }
      println();
      println(FORMAT_SEPARATOR);
    }
    if (getInventory().getFlashlight().getCharge() > 0) {
      print(STATUS_FLASHLIGHT);
      println(getInventory().getFlashlight().getCharge() + FORMAT_PERCENT);
      println(FORMAT_SEPARATOR);
    }
    println(STATUS_REMAINING_TIME.get(0) + countdown.getRemaining() + STATUS_REMAINING_TIME.get(1));
    println(FORMAT_SEPARATOR);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}