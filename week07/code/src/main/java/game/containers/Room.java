package game.containers;

import game.containers.interfaces.Container;

import static game.assets.StringValues.*;

public class Room<T extends Container> implements java.io.Serializable {
  private static final long serialVersionUID = 7295462279118008713L;

  private final boolean isDark;
  private final T east;
  private final T north;
  private final T west;
  private final T south;

  public Room(boolean isDark, T east, T north, T west, T south) {
    this.isDark = isDark;
    this.east = east;
    this.north = north;
    this.west = west;
    this.south = south;
  }

  public boolean isDark() {
    return isDark;
  }

  public T getEast() {
    return east;
  }


  public T getNorth() {
    return north;
  }


  public T getWest() {
    return west;
  }


  public T getSouth() {
    return south;
  }


  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_ROOM.get(0)
        + isDark
        + TO_STRING_ROOM.get(1)
        + east.toString()
        + TO_STRING_ROOM.get(2)
        + north.toString()
        + TO_STRING_ROOM.get(3)
        + west.toString()
        + TO_STRING_ROOM.get(4)
        + south.toString();
  }
}