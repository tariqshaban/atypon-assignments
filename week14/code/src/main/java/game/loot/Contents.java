package game.loot;

import java.util.ArrayList;
import java.util.List;

import static game.assets.StringValues.*;

public class Contents implements java.io.Serializable {
  private static final long serialVersionUID = 1095775767073508778L;

  private static final String TO_STRING = "TO_STRING_CONTENTS";

  private final Gold gold;
  private final FlashLight flashlight;
  private final List<DoorKey> doorKeys;
  private final List<ChestKey> chestKeys;

  public static class Builder {
    private Gold gold = new Gold(-1);
    private FlashLight flashlight = new FlashLight(0);
    private final List<DoorKey> doorKeys = new ArrayList<>();
    private final List<ChestKey> chestKeys = new ArrayList<>();

    public Builder withGold(int amount) {
      if (amount > 0) this.gold = new Gold(amount);
      return this;
    }

    public Builder containingFlashlight(int charge) {
      if (charge > 0) this.flashlight = new FlashLight(charge);
      return this;
    }

    public Builder havingDoorKey(int doorKey) {
      if (doorKey > 0) this.doorKeys.add(new DoorKey(doorKey));
      return this;
    }

    public Builder havingChestKey(int chestKey) {
      if (chestKey > 0) this.chestKeys.add(new ChestKey(chestKey));
      return this;
    }

    public Contents build() {
      return new Contents(this);
    }
  }

  private Contents(Builder builder) {
    this.gold = builder.gold;
    this.flashlight = builder.flashlight;
    this.doorKeys = builder.doorKeys;
    this.chestKeys = builder.chestKeys;
  }

  public Gold getGold() {
    return gold;
  }

  public FlashLight getFlashlight() {
    return flashlight;
  }

  public List<DoorKey> getDoorKeys() {
    return doorKeys;
  }

  public List<ChestKey> getChestKeys() {
    return chestKeys;
  }

  public void addDoorKey(int key) {
    doorKeys.add(new DoorKey(key));
  }

  public void addChestKey(int key) {
    chestKeys.add(new ChestKey(key));
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + translate(TO_STRING,0)
            + gold.toString()
            + translate(TO_STRING,1)
            + flashlight.toString()
            + translate(TO_STRING,1)
            + doorKeys.toString()
            + translate(TO_STRING,1)
            + chestKeys.toString()
            + translate(TO_STRING,2);
  }
}