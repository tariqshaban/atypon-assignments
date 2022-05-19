package game.loot;

import static game.assets.StringValues.*;

public class FlashLight implements java.io.Serializable {
  private static final long serialVersionUID = 2314863596349842107L;

  int charge;

  public FlashLight(int charge) {
    this.charge = charge;
  }

  public int getCharge() {
    return charge;
  }

  public void setCharge(int charge) {
    this.charge = charge;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + TO_STRING_FLASHLIGHT + charge;
  }
}