package game.loot;

import java.util.concurrent.atomic.AtomicInteger;

import static game.assets.StringValues.*;

public class FlashLight implements java.io.Serializable {
    private static final long serialVersionUID = 2314863596349842107L;

    AtomicInteger charge;

    public FlashLight(int charge) {
        this.charge = new AtomicInteger(charge);
    }

    public int getCharge() {
        return charge.get();
    }

    public void addCharge(int charge) {
        this.charge = new AtomicInteger(this.charge.get() + charge);
        if (this.charge.get() < 0) this.charge = new AtomicInteger(0);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + translate("TO_STRING_FLASHLIGHT") + charge;
    }
}