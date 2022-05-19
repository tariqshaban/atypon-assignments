package game.containers.lootables;

import game.MazeGame;
import game.Player;
import game.containers.interfaces.Container;
import game.loot.*;
import org.json.simple.JSONObject;

import static game.assets.StringValues.*;

public abstract class Lootables implements Container {


    private static final String JSON_WHICH = "JSON_WHICH";
    private static final String JSON_VALUE = "JSON_VALUE";
    private static final String GOLD_FOUND = "UI_GOLD_FOUND";


    private static final long serialVersionUID = 6592507137261779895L;

    private Contents contents = new Contents.Builder().build();

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    @Override
    public void onInteractListener(MazeGame mazeGame, Player player, Contents inventory) {
        attemptLoot(player, this);
    }

    public static Contents getContainerContents(Object jsonContents) {
        int goldAmount = -1;
        int doorKey = -1;
        int chestKey = -1;
        int flashlightCharge = -1;
        if (((JSONObject) jsonContents).get(translate(JSON_WHICH)).equals(translate("JSON_WHICH_GOLD")))
            goldAmount = jsonToInt(jsonContents, translate(JSON_VALUE));
        else if (((JSONObject) jsonContents).get(translate(JSON_WHICH)).equals(translate("JSON_WHICH_DOOR_KEY")))
            doorKey = jsonToInt(jsonContents, translate(JSON_VALUE));
        else if (((JSONObject) jsonContents).get(translate(JSON_WHICH)).equals(translate("JSON_WHICH_CHEST_KEY")))
            chestKey = jsonToInt(jsonContents, translate(JSON_VALUE));
        else if (((JSONObject) jsonContents).get(translate(JSON_WHICH)).equals(translate("JSON_WHICH_FLASHLIGHT")))
            flashlightCharge = jsonToInt(jsonContents, translate(JSON_VALUE));

        return new Contents.Builder()
                .withGold(goldAmount)
                .havingDoorKey(doorKey)
                .havingChestKey(chestKey)
                .containingFlashlight(flashlightCharge)
                .build();
    }

    public static int jsonToInt(Object jsonObject, String location) {
        return ((Long) ((JSONObject) jsonObject).get(location)).intValue();
    }

    public static void attemptLoot(Player player, Lootables lootables) {
        if (lootFailed(player, lootables)) player.println(translate("UI_NO_LOOT"));
    }

    public static boolean lootFailed(Player player, Lootables lootables) {
        if (isLootEmpty(lootables)) return true;

        lootGold(player, lootables);
        lootDoorKey(player, lootables);
        lootChestKey(player, lootables);
        lootFlashLight(player, lootables);

        return false;
    }

    public static boolean isLootEmpty(Lootables lootables) {
        return lootables.getContents().getGold().getAmount() == -1
                && lootables.getContents().getDoorKeys().isEmpty()
                && lootables.getContents().getChestKeys().isEmpty()
                && lootables.getContents().getFlashlight().getCharge() <= 0;
    }

    public static void lootGold(Player player, Lootables lootables) {
        int myGold = player.getInventory().getGold().getAmount();
        int containerGold = lootables.getContents().getGold().getAmount();
        if (containerGold != -1) {
            player.println(
                    translate(GOLD_FOUND,0)
                            + containerGold
                            + translate(GOLD_FOUND,1)
                            + (containerGold + myGold)
                            + translate(GOLD_FOUND,2));
            player.getInventory().getGold().addAmount(containerGold);
            lootables.getContents().getGold().setAmount(-1);
        }
    }

    public static void lootDoorKey(Player player, Lootables lootables) {
        if (!lootables.getContents().getDoorKeys().isEmpty()) {
            player.getInventory().getDoorKeys().addAll(lootables.getContents().getDoorKeys());
            player.print(translate("UI_DOOR_KEYS_FOUND"));
            for (DoorKey key : lootables.getContents().getDoorKeys())
                player.print(key.getWhich() + translate("FORMAT_SPACE"));
            player.println();
            lootables.getContents().getDoorKeys().clear();
        }
    }

    public static void lootChestKey(Player player, Lootables lootables) {
        if (!lootables.getContents().getChestKeys().isEmpty()) {
            player.getInventory().getChestKeys().addAll(lootables.getContents().getChestKeys());
            player.print(translate("UI_CHEST_KEYS_FOUND"));
            for (ChestKey key : lootables.getContents().getChestKeys())
                player.print(key.getWhich() + translate("FORMAT_SPACE"));
            player.println();
            lootables.getContents().getChestKeys().clear();
        }
    }

    public static void lootFlashLight(Player player, Lootables lootables) {
        if (lootables.getContents().getFlashlight().getCharge() > 0) {
            player.getInventory().getFlashlight().addCharge(lootables.getContents().getFlashlight().getCharge());
            player.println(
                    translate("UI_FLASHLIGHT_FOUND",0)
                            + lootables.getContents().getFlashlight().getCharge()
                            + translate("UI_FLASHLIGHT_FOUND",1)
                            + player.getInventory().getFlashlight().getCharge());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}