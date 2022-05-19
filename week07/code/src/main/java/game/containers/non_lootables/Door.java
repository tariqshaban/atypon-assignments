package game.containers.non_lootables;

import game.Controls;
import game.GameSettings;
import game.Store;
import game.containers.interfaces.Container;
import game.containers.Room;
import game.containers.interfaces.ContainerWithReset;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Lootables;
import game.exceptions.ExceptionInfoHolder;
import game.loot.Contents;
import game.loot.DoorKey;
import org.json.simple.JSONObject;

import java.util.*;

import static game.GameSettings.getRooms;
import static game.IoHandler.println;
import static game.MazeGame.*;
import static game.assets.StringValues.*;

public class Door implements Container, ContainerWithReset, ContainerWithLogicCheck {
  private static final long serialVersionUID = 4207715701373295444L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  private static int noDoors = -1;
  private final int id;
  private final int to;
  private boolean isLocked;

  public Door(int to, boolean isLocked) {
    id = ++noDoors;
    this.to = to;
    this.isLocked = isLocked;
  }

  public int getId() {
    return id;
  }

  public int getTo() {
    return to;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  @Override
  public Container handleContainerJSONContents(JSONObject jsonObject) {
    return new Door(
        jsonToInt(jsonObject, JSON_WHICH_TO) - 1, (boolean) (jsonObject).get(JSON_WHICH_IS_LOCKED));
  }

  @Override
  public void onInteractListener(Contents inventory) {
    if (this.isLocked()) {
      if (GameSettings.getInventory()
          .getDoorKeys()
          .contains(new DoorKey(this.getId()))) { // Implicitly calls equals
        println(UI_DOOR_UNLOCKED + this.getId());
        this.setLocked(false);
      } else println(UI_DOOR_LOCKED + this.getId());
    } else if (isOppositeDoorLocked(GameSettings.getRooms().get(this.getTo()), getFacing())) {
      Door oppositeDoor = getOppositeDoor(GameSettings.getRooms().get(this.getTo()), getFacing());
      if (GameSettings.getInventory()
          .getDoorKeys()
          .contains(new DoorKey(oppositeDoor.getId()))) { // Implicitly calls equals
        println(UI_OPPOSITE_DOOR_UNLOCKED + oppositeDoor.getId());
        oppositeDoor.setLocked(false);
      } else println(UI_OPPOSITE_DOOR_LOCKED + oppositeDoor.getId());
    } else {
      println(FORMAT_NEW_DIALOG);
      setLocation(GameSettings.getRooms().get(this.getTo()));
      setIsFlashlightPermissionGranted(false);
      setLastKnownLitRoom(getLocation());
      drawMap(getLocation(), false);
    }
  }

  private static boolean isOppositeDoorLocked(Room<Container> containerRoom, char facing) {
    return getOppositeDoor(containerRoom, facing).isLocked;
  }

  private static Door getOppositeDoor(Room<Container> containerRoom, char facing) {
    if (facing == Controls.getRightKey()) return ((Door) containerRoom.getWest());
    else if (facing == Controls.getForwardKey()) return ((Door) containerRoom.getSouth());
    else if (facing == Controls.getLeftKey()) return ((Door) containerRoom.getEast());
    if (facing == Controls.getBackwardKey()) return ((Door) containerRoom.getNorth());

    // This return statement will never be reached since it is ensured that there is a door in the
    // next room where the user is facing when interacting with this door
    return (Door) containerRoom.getEast();
  }

  private static boolean isOppositeNotADoor(List<Container> roomsContainers) {
    Container east = roomsContainers.get(0);
    Container north = roomsContainers.get(1);
    Container west = roomsContainers.get(2);
    Container south = roomsContainers.get(3);

    return east instanceof Door
            && !(getRooms().get(((Door) east).getTo()).getWest() instanceof Door)
        || north instanceof Door
            && !(getRooms().get(((Door) north).getTo()).getSouth() instanceof Door)
        || west instanceof Door
            && !(getRooms().get(((Door) west).getTo()).getEast() instanceof Door)
        || south instanceof Door
            && !(getRooms().get(((Door) south).getTo()).getNorth() instanceof Door);
  }

  @Override
  public void onGameResetListener() {
    resetCounter();
  }

  public static void resetCounter() {
    noDoors = 0;
  }

  @Override
  public ExceptionInfoHolder isLogical() {
    List<Container> containerList = new ArrayList<>();

    String message = FORMAT_SPACE;
    String keyRange = (noDoors == 0) ? EXCEPTION_NO_KEYS : EXCEPTION_EXPECTED + noDoors;

    for (Room<Container> room : getRooms()) {
      containerList.add(room.getEast());
      containerList.add(room.getNorth());
      containerList.add(room.getWest());
      containerList.add(room.getSouth());

      if (!(room.getEast() instanceof Door)
          && !(room.getNorth() instanceof Door)
          && !(room.getWest() instanceof Door)
          && !(room.getSouth() instanceof Door)) {
        message =
            EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
                + getClass().getSimpleName()
                + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(4)
                + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(7)
                + getClass().getSimpleName()
                + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(8)
                + room.getEast().getClass().getSimpleName()
                + FORMAT_TAB
                + room.getNorth().getClass().getSimpleName()
                + FORMAT_TAB
                + room.getWest().getClass().getSimpleName()
                + FORMAT_TAB
                + room.getSouth().getClass().getSimpleName();
        return new ExceptionInfoHolder(false, message);
      }
    }

    if (isDoorPointingAtNoRoom(containerList)) {

      message =
          EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(1)
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(7)
              + getRooms().get(0).getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(8)
              + FORMAT_NULL;
      return new ExceptionInfoHolder(false, message);

    } else if (isDoorWayLooping(containerList)) {

      message =
          EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(2);
      return new ExceptionInfoHolder(false, message);

    } else if (isDoorFacesNoDoor(containerList)) {

      message =
          EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(3);
      return new ExceptionInfoHolder(false, message);

    } else if (isKeyMatchesNoDoor(containerList)) {

      message =
          EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(5)
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(7)
              + keyRange
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(8)
              + EXCEPTION_OTHERWISE;
      return new ExceptionInfoHolder(false, message);
    } else if (isLockedDoorWithoutKey(containerList)) {

      message =
          EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_DOOR.get(6);
      return new ExceptionInfoHolder(false, message);
    }

    return new ExceptionInfoHolder(true, message);
  }

  private boolean isDoorPointingAtNoRoom(List<Container> containerList) {
    for (Container container : containerList) {
      if (container instanceof Door && ((Door) container).to >= containerList.size() / 4
          || container instanceof Door && ((Door) container).to < 0) return true;
    }
    return false;
  }

  private boolean isDoorWayLooping(List<Container> containerList) {
    double roomNo = 0;
    for (Container container : containerList) {
      if (container instanceof Door && ((Door) container).to == (int) roomNo) return true;
      roomNo += 0.25;
    }
    return false;
  }

  private boolean isDoorFacesNoDoor(List<Container> containerList) {
    double roomNo = 0;

    for (Container container : containerList) {
      if (container instanceof Door
          && isOppositeNotADoor(containerList.subList((int) roomNo * 4, (int) roomNo * 4 + 4)))
        return true;
      roomNo += 0.25;
    }
    return false;
  }

  private boolean isKeyMatchesNoDoor(List<Container> containerList) {

    for (Container container : containerList) {
      if (container instanceof Lootables
          && !((Lootables) container).getContents().getDoorKeys().isEmpty()
          && Collections.max(
                      ((Lootables) container).getContents().getDoorKeys(),
                      (a, b) -> Float.compare(a.getWhich(), b.getWhich()))
                  .getWhich()
              > noDoors) return true;
    }

    for (Contents contents : Store.getStoreItems().keySet())
      for (DoorKey doorKey : contents.getDoorKeys()) {
        if (doorKey.getWhich() <= 0 || doorKey.getWhich() > noDoors) {
          return true;
        }
      }

    return false;
  }

  private boolean isLockedDoorWithoutKey(List<Container> containerList) {
    Set<Integer> doorKeys = new HashSet<>();
    Set<Integer> doors = new HashSet<>();

    for (Container container : containerList) {
      if (container instanceof Lootables)
        for (DoorKey doorKey : ((Lootables) container).getContents().getDoorKeys())
          doorKeys.add(doorKey.getWhich());

      if (container instanceof Door && ((Door) container).isLocked())
        doors.add(((Door) container).id);
    }

    for (Contents contents : Store.getStoreItems().keySet())
      for (DoorKey doorKey : contents.getDoorKeys()) doorKeys.add(doorKey.getWhich());

    return !doorKeys.equals(doors);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_DOOR.get(0)
        + id
        + TO_STRING_DOOR.get(1)
        + noDoors
        + TO_STRING_DOOR.get(2)
        + to
        + TO_STRING_DOOR.get(3)
        + isLocked;
  }
}