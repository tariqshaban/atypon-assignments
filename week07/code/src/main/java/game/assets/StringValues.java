package game.assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static game.Controls.getAffirmativeKey;
import static game.Controls.getNegativeKey;

public class StringValues {

  public static final String FORMAT_SPACE = " ";
  public static final String FORMAT_LINE = "\n";
  public static final String FORMAT_NEW_DIALOG =
      "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
  public static final String FORMAT_TAB = "\t";
  public static final String FORMAT_SEPARATOR = "-----------------------------------------";
  public static final String FORMAT_COLON = ": ";
  public static final String FORMAT_PERCENT = "%";
  public static final String FORMAT_STAR = "*";
  public static final String FORMAT_VERTICAL_BAR = "|";
  public static final String FORMAT_DIVISION = "/";
  public static final String FORMAT_DOT = ".";
  public static final String FORMAT_NULL = "Null";
  public static final String FORMAT_UNKNOWN = "???";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String JSON_DIR = "src/main/java/game/assets/maps/Default.json";

  public static final String JSON_ROOMS = "Rooms";
  public static final String JSON_MERCHANT_STORE = "MerchantStore";
  public static final String JSON_REMAINING_TIME = "RemainingTime";
  public static final String JSON_INITIAL_GOLD = "InitialGold";
  public static final String JSON_GOLD_THRESHOLD = "GoldThresholdObjective";

  public static final String JSON_TYPE = "type";
  public static final String JSON_WHICH = "which";
  public static final String JSON_VALUE = "value";
  public static final String JSON_PRICE = "price";
  public static final String JSON_WHICH_GOLD = "G";
  public static final String JSON_WHICH_DOOR_KEY = "DK";
  public static final String JSON_WHICH_CHEST_KEY = "CK";
  public static final String JSON_WHICH_FLASHLIGHT = "F";

  public static final String JSON_WHICH_TO = "to";
  public static final String JSON_WHICH_IS_LOCKED = "isLocked";

  public static final String JSON_IS_DARK = "isDark";
  public static final String JSON_EAST = "east";
  public static final String JSON_NORTH = "north";
  public static final String JSON_WEST = "west";
  public static final String JSON_SOUTH = "south";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String CONTAINER_MERCH = "Merch";
  public static final String CONTAINER_DOOR = "Door";
  public static final String CONTAINER_PAINTING = "Painting";
  public static final String CONTAINER_MIRROR = "Mirror";
  public static final String CONTAINER_CHEST = "Chest";
  public static final String CONTAINER_WALL = "Wall";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String UI_AWAITING_COMMAND = "Awaiting command...";
  public static final String UI_INVALID_COMMAND = "Invalid command";
  public static final String UI_INVALID_SELECTION = "Invalid selection";
  public static final String UI_FLASHLIGHT_CHARGE_IS = "Current flashlight charge is: ";
  public static final String UI_OBJECTIVE_COMPLETE = "I think I have more than enough for today";
  public static final String UI_BLIND = "I cant see, I'll head back to the last location";
  public static final String UI_REQUEST_FLASHLIGHT =
      "The room is not lit, should I turn on the flashlight? ("
          + getAffirmativeKey()
          + "/"
          + getNegativeKey()
          + ")";
  public static final String UI_DOOR_UNLOCKED = "I've unlocked the door using key number ";
  public static final String UI_DOOR_LOCKED = "It's locked, I'll need door key number ";
  public static final String UI_OPPOSITE_DOOR_UNLOCKED =
      "I've unlocked the opposite door using key number ";
  public static final String UI_OPPOSITE_DOOR_LOCKED =
      "It's locked from the opposite room, I'll need door key number ";
  public static final String UI_CHEST_LOCKED = "It's locked, I'll need chest key number ";
  public static final String UI_NO_LOOT = "Got nothing here";
  public static final List<String> UI_GOLD_FOUND =
      Collections.unmodifiableList(Arrays.asList("Found ", ", now I've got ", " gold"));
  public static final String UI_DOOR_KEYS_FOUND = "Found key for door/s number ";
  public static final String UI_CHEST_KEYS_FOUND = "Found key for chest/s number ";
  public static final List<String> UI_FLASHLIGHT_FOUND =
      Collections.unmodifiableList(
          Arrays.asList("Found flashlight batteries with charge of ", ", totaling "));
  public static final String UI_WALL = "Just a wall here :/";
  public static final String UI_RESET = "Resetting...";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String SHOP_TITLE = "Shop";
  public static final String SHOP_DOOR_KEYS = "Door keys numbers: ";
  public static final String SHOP_CHEST_KEYS = "Chest keys numbers: ";
  public static final List<String> SHOP_FLASHLIGHT =
      Collections.unmodifiableList(Arrays.asList("Flashlight with ", "% charge\t"));
  public static final String SHOP_GOLD = FORMAT_TAB + UI_GOLD_FOUND.get(2);
  public static final String SHOP_EMPTY = "They don't currently have any items";
  public static final String SHOP_EXIT = "0: Exit";
  public static final String SHOP_YOU_BOUGHT = "You bought ";
  public static final List<String> SHOP_YOU_BOUGHT_FOR =
      Collections.unmodifiableList(Arrays.asList("For ", UI_GOLD_FOUND.get(2)));
  public static final String SHOP_INSUFFICIENT = "Insufficient gold";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String STATUS_TITLE = "Status";
  public static final String STATUS_GOLD = "Gold: ";
  public static final String STATUS_KEYS = "Keys: ";
  public static final String STATUS_DOOR_KEYS = FORMAT_TAB + "Door Keys:";
  public static final String STATUS_CHEST_KEYS = FORMAT_TAB + FORMAT_TAB + "Chest Keys:";
  public static final String STATUS_FLASHLIGHT = "Flashlight charge:" + FORMAT_TAB;
  public static final List<String> STATUS_REMAINING_TIME =
      Collections.unmodifiableList(Arrays.asList("Remaining time: ", " seconds"));

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String TIMER_ALREADY_SET = "Countdown timer has already been set.";
  public static final List<String> TIMER_START_DIALOG =
      Collections.unmodifiableList(
          Arrays.asList("I have ", " seconds to collect ", " gold, better get to it."));
  public static final List<String> TIMER_60_SECS =
      Collections.unmodifiableList(Arrays.asList("Only one minute left, I still need ", " gold."));
  public static final String TIMER_5_SECS = "I had my chance...";
  public static final String TIMER_DONE = "It's over.";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String CONTROLS_DUPLICATE =
      "One of the controls is duplicate, could not set such control";
  public static final String CONTROLS_DNE = "Invalid batch control command";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final List<String> SERIALIZE_FILE_NAME =
      Collections.unmodifiableList(Arrays.asList("checkpoint", ".ser"));
  public static final String SERIALIZE_DIRECTORY = "src/main/java/game/assets/checkpoints/";
  public static final String SERIALIZE_CHECKPOINT_MADE = "A checkpoint has been made";
  public static final String SERIALIZE_CHECKPOINT_FAILED =
      "Could not find such directory, either the directory "
          + "does not exist or there is a write permission (access control) issue.";
  public static final String SERIALIZE_NO_CHECKPOINTS_FOUND = "No saved checkpoints found";
  public static final String SERIALIZE_EXIT = "0. Exit";
  public static final String SERIALIZE_CHECKPOINT_LOADED = "Loaded checkpoint";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String EXCEPTION_EXPECTED = "1::";
  public static final String EXCEPTION_NO_KEYS = "No keys";
  public static final String EXCEPTION_LINE = "Line number: ";
  public static final String EXCEPTION_JSON_ERR_FILE_NOT_FOUND =
      "Error, settings file was not found; make sure"
          + " that you specified the directory and name correctly (error code 3000)";
  public static final String EXCEPTION_JSON_ERR =
      "Error decoding the JSON file, ensure that the structure is compliant (error code 3001)";

  public static final String EXCEPTION_ILLOGICAL_MANDATE_VALIDATION =
      "Validate the logic of the JSON file map, ";
  public static final String EXCEPTION_ILLOGICAL_REMAINING_TIME =
      EXCEPTION_ILLOGICAL_MANDATE_VALIDATION + "remaining time cannot be less than a minute";
  public static final String EXCEPTION_ILLOGICAL_STARTING_GOLD =
          EXCEPTION_ILLOGICAL_MANDATE_VALIDATION + "initial gold must be less than the objective";
  public static final String EXCEPTION_ILLOGICAL_STARTING_GOLD_NEGATIVE =
          EXCEPTION_ILLOGICAL_MANDATE_VALIDATION + "initial gold cannot be negative";
  public static final String EXCEPTION_ILLOGICAL_GOLD_SUM =
      EXCEPTION_ILLOGICAL_MANDATE_VALIDATION
          + "the total gold in the map may not be sufficient to ensure wining conditions"
          + FORMAT_LINE
          + "Make sure to include the scenario where the user buys all of the store items"
          + FORMAT_LINE
          + "The following equation must be followed: startingGold + allContainerGold - StoreItems >= GoldThreshold";
  public static final String EXCEPTION_ILLOGICAL_SPAWN_ROOM_LIGHTNING =
      EXCEPTION_ILLOGICAL_MANDATE_VALIDATION + "The spawn room cannot be dark";
  public static final List<String> EXCEPTION_ILLOGICAL_CONTAINER_DOOR =
      Collections.unmodifiableList(
          Arrays.asList(
              EXCEPTION_ILLOGICAL_MANDATE_VALIDATION
                  + "A container class raised an exception due to inconsistent mapping\nContainer Type: ",
              FORMAT_LINE + "Reason: A door leads to a non existing room",
              FORMAT_LINE + "Reason: A door cannot lead to the same room",
              FORMAT_LINE + "Reason: A door does not lead to another door",
              FORMAT_LINE + "Reason: A room does not have any doors (orphan)",
              FORMAT_LINE + "Reason: A Key does not correspond to any door",
              FORMAT_LINE + "Reason: A locked door does not have a key",
              FORMAT_LINE + "Expected: ",
              FORMAT_LINE + "Found: "));
  public static final List<String> EXCEPTION_ILLOGICAL_CONTAINER_CHEST =
      Collections.unmodifiableList(
          Arrays.asList(
              EXCEPTION_ILLOGICAL_MANDATE_VALIDATION
                  + "A container class raised an exception due to inconsistent mapping\nContainer Type: ",
              FORMAT_LINE + "Reason: A Key does not correspond to any chest",
              FORMAT_LINE + "Reason: A chest does not have a key",
              FORMAT_LINE + "Expected: ",
              FORMAT_LINE + "Found: "));
  public static final String EXCEPTION_SERIALIZE_ASSIGNING_FAILED = "Internal error detected";
  public static final String EXCEPTION_FILE_NOT_FOUND =
      "Could not find such file, either the file does not exist or there is a read permission (access control) issue.";
  public static final String EXCEPTION_SERIALIZE_FILE_ALTERED =
      "File inconsistency detected, do not manually modify the checkpoint file";
  public static final String EXCEPTION_CLASS_MODIFIED =
      "This checkpoint was saved in a different version of the game";
  public static final String EXCEPTION_OTHERWISE = "Otherwise";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final List<String> TO_STRING_CHEST =
      Collections.unmodifiableList(Arrays.asList(FORMAT_TAB + "ID: ", FORMAT_DIVISION, FORMAT_TAB));
  public static final List<String> TO_STRING_DOOR =
      Collections.unmodifiableList(
          Arrays.asList(
              FORMAT_TAB + "ID: ",
              FORMAT_DIVISION,
              FORMAT_TAB + "to: ",
              FORMAT_TAB + "is locked: "));
  public static final List<String> TO_STRING_STORE =
      Collections.unmodifiableList(
          Arrays.asList(FORMAT_TAB + "price: ", FORMAT_TAB, FORMAT_TAB + "ITEMS: "));
  public static final List<String> TO_STRING_ROOM =
      Collections.unmodifiableList(
          Arrays.asList(
              FORMAT_TAB + "is dark: ",
              FORMAT_TAB + "east: ",
              FORMAT_TAB + "north: ",
              FORMAT_TAB + "west: ",
              FORMAT_TAB + "south: "));
  public static final List<String> TO_STRING_EXCEPTION_INFO_HOLDER =
      Collections.unmodifiableList(
          Arrays.asList(FORMAT_TAB + "is success: ", FORMAT_TAB + "reason: "));
  public static final String TO_STRING_ILLOGICAL_MAPPING =
      FORMAT_TAB
          + "Indicates that the file reading is a success, however, the game logic is vague; "
          + "in other words, the game could be never winnable or the wining conditions are met at the very beginning,"
          + "can also be caused when the map's keys are more/less than the container keys"
          + FORMAT_TAB
          + "type: deserialization exception"
          + FORMAT_TAB
          + "fatal: yes";
  public static final String TO_STRING_TIME_UP =
      FORMAT_TAB
          + "Indicates that the timer reached 0 before the user has completed his mandated tasks"
          + FORMAT_TAB
          + "type: game-ending exception"
          + FORMAT_TAB
          + "fatal: no";
  public static final String TO_STRING_CHEST_KEY = FORMAT_TAB + "which: ";
  public static final String TO_STRING_DOOR_KEY = FORMAT_TAB + "which: ";
  public static final String TO_STRING_FLASHLIGHT = FORMAT_TAB + "charge: ";
  public static final String TO_STRING_GOLD = FORMAT_TAB + "amount: ";
  public static final List<String> TO_STRING_CONTENTS =
      Collections.unmodifiableList(
          Arrays.asList(
              FORMAT_TAB + FORMAT_VERTICAL_BAR,
              FORMAT_VERTICAL_BAR + FORMAT_TAB + FORMAT_VERTICAL_BAR,
              FORMAT_VERTICAL_BAR));
  public static final List<String> TO_STRING_COUNTDOWN =
      Collections.unmodifiableList(Arrays.asList(FORMAT_TAB + "time left: ", FORMAT_DIVISION));
  public static final List<String> TO_STRING_GAME_SETTINGS =
      Collections.unmodifiableList(
          Arrays.asList(FORMAT_TAB + "gold objective: ", FORMAT_TAB + "inventory: "));

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static final String MAP_DIRECTORY = "src/main/java/game/assets/maps/";
  public static final String MAP_SELECT = "Select Map";
  public static final String SERIALIZE_NO_MAPS_FOUND = "No saved maps found";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private StringValues() {}

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
