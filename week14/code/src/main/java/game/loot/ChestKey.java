package game.loot;

import java.util.Objects;

import static game.assets.StringValues.*;

public class ChestKey implements java.io.Serializable {
  private static final long serialVersionUID = 2409548198138641500L;

  final int which;

  public ChestKey(int which) {
    this.which = which;
  }

  public int getWhich() {
    return which;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ChestKey) {
      return which == ((ChestKey) o).which;
    } else return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(which);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + translate("TO_STRING_CHEST_KEY") + which;
  }
}