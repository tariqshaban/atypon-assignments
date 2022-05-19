package game.containers.lootables;

import game.MazeGame;
import game.Player;
import game.containers.interfaces.Container;
import game.containers.Room;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.exceptions.ExceptionInfoHolder;
import game.loot.ChestKey;
import game.loot.Contents;
import org.json.simple.JSONObject;

import java.util.*;

import static game.assets.StringValues.*;

public class Chest extends Lootables implements ContainerWithLogicCheck {
  private static final long serialVersionUID = 3969773738288491472L;

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }

  private static final String ILLOGICAL_CONTAINER = "EXCEPTION_ILLOGICAL_CONTAINER_CHEST";
  private static final String TO_STRING = "TO_STRING_CHEST";
  private static int noChests = -1;
  private final int id;

  public Chest() {
    id = ++noChests;
  }

  public int getId() {
    return id;
  }

  @Override
  public Container handleContainerJSONContents(MazeGame mazeGame,  JSONObject jsonObject) {
    Chest chest = new Chest();
    chest.setContents(getContainerContents(jsonObject));
    return chest;
  }

  @Override
  public void onInteractListener(MazeGame mazeGame, Player player, Contents inventory) {
    if (inventory.getChestKeys().contains(new ChestKey(this.getId()))) {
      super.onInteractListener(mazeGame, player, inventory);
    } else player.println(translate("UI_CHEST_LOCKED") + this.getId());
  }

  public static void resetCounter() {
    noChests = 0;
  }

  @Override
  public ExceptionInfoHolder isLogical(MazeGame mazeGame) {
    List<Container> containerList = new ArrayList<>();

    String message;
    String keyRange = (noChests == 0) ? translate("EXCEPTION_NO_KEYS") : translate("EXCEPTION_EXPECTED") + noChests;

    for (Room<Container> room : mazeGame.getRooms()) {
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
                  translate(ILLOGICAL_CONTAINER,0)
                  + getClass().getSimpleName()
                  + translate(ILLOGICAL_CONTAINER,1)
                  + translate(ILLOGICAL_CONTAINER,3)
                  + keyRange
                  + translate(ILLOGICAL_CONTAINER,4)
                  + maxChestKeyInContainer;
          return new ExceptionInfoHolder(false, message);
        }
      }
    }

    for (Contents contents : mazeGame.getStore().getStoreItems().keySet()) {
      if (!contents.getChestKeys().isEmpty()) {

        int maxChestKeyInContainer =
            Collections.max(contents.getChestKeys(), Comparator.comparingInt(ChestKey::getWhich))
                .getWhich();

        if (maxChestKeyInContainer > noChests) {
          message =
                  translate(ILLOGICAL_CONTAINER,0)
                  + getClass().getSimpleName()
                  + translate(ILLOGICAL_CONTAINER,1)
                  + translate(ILLOGICAL_CONTAINER,3)
                  + keyRange
                  + translate(ILLOGICAL_CONTAINER,4)
                  + maxChestKeyInContainer;
          return new ExceptionInfoHolder(false, message);
        }
      }
    }

    resetCounter();

    return getExceptionInfoHolder(mazeGame, containerList);
  }

  private ExceptionInfoHolder getExceptionInfoHolder(MazeGame mazeGame, List<Container> containerList) {
    if (isChestWithoutKey(mazeGame, containerList)) {

      String message =
              translate(ILLOGICAL_CONTAINER,0)
              + getClass().getSimpleName()
              + translate(ILLOGICAL_CONTAINER,2);
      return new ExceptionInfoHolder(false, message);
    }
    return new ExceptionInfoHolder(true, translate("FORMAT_SPACE"));
  }

  private boolean isChestWithoutKey(MazeGame mazeGame, List<Container> containerList) {
    Set<Integer> chestKeys = new HashSet<>();
    Set<Integer> chests = new HashSet<>();

    for (Container container : containerList) {
      if (container instanceof Lootables)
        for (ChestKey chestKey : ((Lootables) container).getContents().getChestKeys())
          chestKeys.add(chestKey.getWhich());

      if (container instanceof Chest) chests.add(((Chest) container).id);
    }

    for (Contents contents : mazeGame.getStore().getStoreItems().keySet())
      for (ChestKey chestKey : contents.getChestKeys()) chestKeys.add(chestKey.getWhich());

    return !chestKeys.equals(chests);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + translate(TO_STRING,0)
        + id
        + translate(TO_STRING,1)
        + noChests
        + translate(TO_STRING,2)
        + getContents().toString();
  }
}