package game.loot;

import static game.assets.StringValues.*;

public class Gold implements java.io.Serializable {
  private static final long serialVersionUID = 6946382482265401424L;

  int amount;

  public Gold(int amount) {
    this.amount = amount;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + TO_STRING_GOLD + amount;
  }
}