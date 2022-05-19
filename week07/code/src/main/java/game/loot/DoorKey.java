package game.loot;

import java.util.Objects;

import static game.assets.StringValues.*;

public class DoorKey implements java.io.Serializable {
  private static final long serialVersionUID = 3041654785548004192L;

  final int which;

  public DoorKey(int which) {
    this.which = which;
  }

  public int getWhich() {
    return which;
  }

  @Override
  public boolean equals(Object o){
    if(o instanceof DoorKey){
      return this.which==((DoorKey) o).which;
    } else
      return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(which);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+TO_STRING_DOOR_KEY+which;
  }
}