package game.containers;

import game.containers.interfaces.Container;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Chest;
import game.containers.lootables.Mirror;
import game.containers.lootables.Painting;
import game.containers.non_lootables.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static game.assets.StringValues.*;

public class ContainerList {

  private static final String MERCH = "CONTAINER_MERCH";
  private static final String DOOR = "CONTAINER_DOOR";
  private static final String PAINTING = "CONTAINER_PAINTING";
  private static final String MIRROR = "CONTAINER_MIRROR";
  private static final String CHEST = "CONTAINER_CHEST";
  private static final String WALL = "CONTAINER_WALL";


  static final Map<String, Container> containersList = buildContainersList();

  static final Map<String, ContainerWithLogicCheck> logicCheckList = buildLogicCheckList();

  private ContainerList() {}

  private static Map<String, Container> buildContainersList() {
    Map<String, Container> containersListBuilder = new LinkedHashMap<>();
    containersListBuilder.put(translate(MERCH), new Shop());
    containersListBuilder.put(translate(DOOR), new Door(-1, false));
    containersListBuilder.put(translate(PAINTING), new Painting());
    containersListBuilder.put(translate(MIRROR), new Mirror());
    containersListBuilder.put(translate(CHEST), new Chest());
    containersListBuilder.put(translate(WALL), new Wall());
    return containersListBuilder;
  }

  private static Map<String, ContainerWithLogicCheck> buildLogicCheckList() {
    Map<String, ContainerWithLogicCheck> logicCheckListBuilder = new LinkedHashMap<>();
    logicCheckListBuilder.put(
            translate(DOOR), (ContainerWithLogicCheck) containersList.get(translate(DOOR)));
    logicCheckListBuilder.put(
            translate(CHEST), (ContainerWithLogicCheck) containersList.get(translate(CHEST)));
    return logicCheckListBuilder;
  }

  public static Map<String, Container> getContainersList() {
    return containersList;
  }

  public static Map<String, ContainerWithLogicCheck> getLogicCheckList() {
    return logicCheckList;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}






