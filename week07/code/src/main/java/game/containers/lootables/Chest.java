package game.containers.lootables;

import game.Store;
import game.containers.interfaces.ContainerWithReset;
import game.containers.interfaces.Container;
import game.containers.Room;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.exceptions.ExceptionInfoHolder;
import game.loot.ChestKey;
import game.loot.Contents;
import org.json.simple.JSONObject;

import java.util.*;

import static game.GameSettings.*;
import static game.IoHandler.println;
import static game.assets.StringValues.*;

public class Chest extends Lootables implements ContainerWithReset, ContainerWithLogicCheck {
  private static final long serialVersionUID = 3969773738288491472L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  private static int noChests = -1;
  private final int id;

  public Chest() {
    id = ++noChests;
  }

  public int getId() {
    return id;
  }

  @Override
  public Container handleContainerJSONContents(JSONObject jsonObject) {
    Chest chest = new Chest();
    chest.setContents(getContainerContents(jsonObject));
    return chest;
  }

  @Override
  public void onInteractListener(Contents inventory) {
    if (inventory.getChestKeys().contains(new ChestKey(this.getId()))) {
      super.onInteractListener(inventory);
    } else println(UI_CHEST_LOCKED + this.getId());
  }

  public static void resetCounter() {
    noChests = 0;
  }

  @Override
  public void onGameResetListener() {
    resetCounter();
  }

  @Override
  public ExceptionInfoHolder isLogical() {
    List<Container> containerList = new ArrayList<>();

    String message;
    String keyRange = (noChests == 0) ? EXCEPTION_NO_KEYS : EXCEPTION_EXPECTED + noChests;

    for (Room<Container> room : getRooms()) {
      containerList.add(room.getEast());
      containerList.add(room.getNorth());
      containerList.add(room.getWest());
      containerList.add(room.getSouth());
    }

    for (Container container : containerList) {

      if (container instanceof Lootables
          && !((Lootables) container).getContents().getChestKeys().isEmpty()) {

        int maxChestKeyInContainer =
            Collections.max(
                    ((Lootables) container).getContents().getChestKeys(),
                    Comparator.comparingInt(ChestKey::getWhich))
                .getWhich();

        if (maxChestKeyInContainer > noChests) {

          message =
              EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(0)
                  + getClass().getSimpleName()
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(1)
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(3)
                  + keyRange
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(4)
                  + maxChestKeyInContainer;
          return new ExceptionInfoHolder(false, message);
        }
      }
    }

    for (Contents contents : Store.getStoreItems().keySet()) {
      if (!contents.getChestKeys().isEmpty()) {

        int maxChestKeyInContainer =
            Collections.max(contents.getChestKeys(), Comparator.comparingInt(ChestKey::getWhich))
                .getWhich();

        if (maxChestKeyInContainer > noChests) {
          message =
              EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(0)
                  + getClass().getSimpleName()
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(1)
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(3)
                  + keyRange
                  + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(4)
                  + maxChestKeyInContainer;
          return new ExceptionInfoHolder(false, message);
        }
      }
    }

    return getExceptionInfoHolder(containerList);
  }

  private ExceptionInfoHolder getExceptionInfoHolder(List<Container> containerList) {
    if (isChestWithoutKey(containerList)) {

      String message =
          EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(0)
              + getClass().getSimpleName()
              + EXCEPTION_ILLOGICAL_CONTAINER_CHEST.get(2);
      return new ExceptionInfoHolder(false, message);
    }
    return new ExceptionInfoHolder(true, FORMAT_SPACE);
  }

  private boolean isChestWithoutKey(List<Container> containerList) {
    Set<Integer> chestKeys = new HashSet<>();
    Set<Integer> chests = new HashSet<>();

    for (Container container : containerList) {
      if (container instanceof Lootables)
        for (ChestKey chestKey : ((Lootables) container).getContents().getChestKeys())
          chestKeys.add(chestKey.getWhich());

      if (container instanceof Chest) chests.add(((Chest) container).id);
    }

    for (Contents contents : Store.getStoreItems().keySet())
      for (ChestKey chestKey : contents.getChestKeys()) chestKeys.add(chestKey.getWhich());

    return !chestKeys.equals(chests);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_CHEST.get(0)
        + id
        + TO_STRING_CHEST.get(1)
        + noChests
        + TO_STRING_CHEST.get(2)
        + getContents().toString();
  }
}