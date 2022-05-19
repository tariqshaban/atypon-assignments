package game.containers;

import game.containers.interfaces.Container;
import game.containers.interfaces.ContainerWithReset;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Chest;
import game.containers.lootables.Mirror;
import game.containers.lootables.Painting;
import game.containers.non_lootables.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static game.assets.StringValues.*;

public class ContainerList {

  static final Map<String, Container> containersList = buildContainersList();

  static final Map<String, ContainerWithReset> gameResetList = buildGameResetList();

  static final Map<String, ContainerWithLogicCheck> logicCheckList = buildLogicCheckList();

  private ContainerList() {}

  private static Map<String, Container> buildContainersList() {
    Map<String, Container> containersListBuilder = new LinkedHashMap<>();
    containersListBuilder.put(CONTAINER_MERCH, new Shop());
    containersListBuilder.put(CONTAINER_DOOR, new Door(-1, false));
    containersListBuilder.put(CONTAINER_PAINTING, new Painting());
    containersListBuilder.put(CONTAINER_MIRROR, new Mirror());
    containersListBuilder.put(CONTAINER_CHEST, new Chest());
    containersListBuilder.put(CONTAINER_WALL, new Wall());
    return containersListBuilder;
  }

  private static Map<String, ContainerWithReset> buildGameResetList() {
    Map<String, ContainerWithReset> gameResetListBuilder = new LinkedHashMap<>();
    gameResetListBuilder.put(CONTAINER_DOOR, (ContainerWithReset) containersList.get(CONTAINER_DOOR));
    gameResetListBuilder.put(CONTAINER_CHEST, (ContainerWithReset) containersList.get(CONTAINER_CHEST));
    return gameResetListBuilder;
  }

  private static Map<String, ContainerWithLogicCheck> buildLogicCheckList() {
    Map<String, ContainerWithLogicCheck> logicCheckListBuilder = new LinkedHashMap<>();
    logicCheckListBuilder.put(
            CONTAINER_DOOR, (ContainerWithLogicCheck) containersList.get(CONTAINER_DOOR));
    logicCheckListBuilder.put(
            CONTAINER_CHEST, (ContainerWithLogicCheck) containersList.get(CONTAINER_CHEST));
    return logicCheckListBuilder;
  }

  public static Map<String, Container> getContainersList() {
    return containersList;
  }

  public static Map<String, ContainerWithReset> getGameResetList() {
    return gameResetList;
  }

  public static Map<String, ContainerWithLogicCheck> getLogicCheckList() {
    return logicCheckList;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}