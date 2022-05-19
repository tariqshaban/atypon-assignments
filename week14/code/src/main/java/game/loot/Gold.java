package game.loot;

import java.util.concurrent.atomic.AtomicInteger;

import static game.assets.StringValues.*;

public class Gold implements java.io.Serializable {
    private static final long serialVersionUID = 6946382482265401424L;

    AtomicInteger amount;

    public Gold(int amount) {
        this.amount = new AtomicInteger(amount);
    }

    public int getAmount() {
        return amount.get();
    }

    public void addAmount(int amount) {
        this.amount = new AtomicInteger(this.amount.get() + amount);
        if (this.amount.get() < 0) this.amount = new AtomicInteger(0);
    }

    public void setAmount(int amount) {
        this.amount = new AtomicInteger(amount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + translate("TO_STRING_GOLD") + amount;
    }
}