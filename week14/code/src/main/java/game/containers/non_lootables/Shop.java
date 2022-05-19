package game.containers.non_lootables;

import game.Player;
import game.containers.interfaces.Container;
import game.loot.ChestKey;
import game.loot.Contents;
import game.loot.DoorKey;
import game.MazeGame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static game.assets.StringValues.*;
import static game.containers.lootables.Lootables.getContainerContents;
import static game.containers.lootables.Lootables.jsonToInt;

public class Shop implements Container {
    private static final long serialVersionUID = 7457365249711164690L;

    private static final String FLASHLIGHT = "SHOP_FLASHLIGHT";
    private static final String TAB = "FORMAT_TAB";

    @Override
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    @Override
    public Container handleContainerJSONContents(MazeGame mazeGame, JSONObject jsonObject) throws FileNotFoundException {
        if (mazeGame.getStore().getStoreItems().isEmpty()) {
            final JSONObject fileJSON =
                    (JSONObject) JSONValue.parse(new FileReader(mazeGame.getJsonDirectory()));
            final JSONArray jsonArray = (JSONArray) fileJSON.get(translate("JSON_MERCHANT_STORE"));
            for (Object deal : jsonArray) {
                Contents contents = getContainerContents(deal);
                if (contentIsNotEmpty(contents))
                    mazeGame.getStore().getStoreItems().put(contents, jsonToInt(deal, translate("JSON_PRICE")));
            }
        }
        return new Shop();
    }

    private boolean contentIsNotEmpty(Contents contents) {
        return !contents.getDoorKeys().isEmpty()
                || !contents.getChestKeys().isEmpty()
                || contents.getGold().getAmount() > 0
                || contents.getFlashlight().getCharge() > 0;
    }

    @Override
    public void onInteractListener(MazeGame mazeGame, Player player, Contents inventory) {
        player.println(translate("SHOP_TITLE"));
    }

    public static List<StringBuilder> viewShopItems(MazeGame mazeGame) {
        List<StringBuilder> items = new ArrayList<>();
        int itemsViewed = 0;
        for (Contents item : mazeGame.getStore().getStoreItems().keySet()) {

            items.add(new StringBuilder());

            if (mazeGame.getStore().getStoreItems().get(item) == -1)
                continue;

            items.get(items.size() - 1).append(itemsViewed + 1).append(translate("FORMAT_COLON"));
            itemsViewed++;

            if (!item.getDoorKeys().isEmpty())
                for (DoorKey dKey : item.getDoorKeys())
                    items.get(items.size() - 1).append(translate("SHOP_DOOR_KEYS")).append(dKey.getWhich()).append(translate(TAB)).append(translate(TAB)).append(translate(TAB));
            if (!item.getChestKeys().isEmpty())
                for (ChestKey cKey : item.getChestKeys())
                    items.get(items.size() - 1).append(translate("SHOP_CHEST_KEYS")).append(cKey.getWhich()).append(translate("FORMAT_SPACE")).append(translate(TAB)).append(translate(TAB));
            if (item.getFlashlight().getCharge() > 0)
                items.get(items.size() - 1).append(translate(FLASHLIGHT, 0)).append(item.getFlashlight().getCharge()).append(translate(FLASHLIGHT, 1));
            items.get(items.size() - 1).append(translate(TAB)).append(translate(TAB)).append(mazeGame.getStore().getStoreItems().get(item)).append(translate("SHOP_GOLD"));
        }

        items.add(new StringBuilder());
        items.get(items.size() - 1).append(translate("SHOP_EXIT"));

        return items;
    }

    public static synchronized void makeTransaction(MazeGame mazeGame, Player player, int choice) {
        if (choice - 1 == mazeGame.getStore().getStoreItems().size()) {
            player.println(translate("UI_AWAITING_COMMAND"));
            return;
        }
        Contents chosenItem = (Contents) mazeGame.getStore().getStoreItems().keySet().toArray()[choice - 1];
        int chosenItemPrice = (Integer) mazeGame.getStore().getStoreItems().values().toArray()[choice - 1];

        if (chosenItemPrice == -1) {
            player.print(translate("SHOP_ITEM_GONE"));
            return;
        }

        if (chosenItemPrice <= player.getInventory().getGold().getAmount()) {
            buyItem(mazeGame, player, chosenItem, chosenItemPrice);
            player.println(translate("SHOP_YOU_BOUGHT_FOR", 0) + chosenItemPrice + translate("SHOP_YOU_BOUGHT_FOR", 1));
        } else
            player.println(translate("SHOP_INSUFFICIENT"));
    }

    private static void buyItem(MazeGame mazeGame, Player player, Contents chosenItem, int chosenItemPrice) {
        mazeGame.getStore().getStoreItems().put(chosenItem, -1);
        player.getInventory().getGold().addAmount(-chosenItemPrice);

        player.print(translate("SHOP_YOU_BOUGHT"));

        if (!chosenItem.getDoorKeys().isEmpty())
            for (DoorKey dKey : chosenItem.getDoorKeys()) {
                player.println(translate("SHOP_DOOR_KEYS") + dKey.getWhich());
                player.getInventory().addDoorKey(dKey.getWhich());
            }
        if (!chosenItem.getChestKeys().isEmpty())
            for (ChestKey cKey : chosenItem.getChestKeys()) {
                player.println(translate("SHOP_CHEST_KEYS") + cKey.getWhich());
                player.getInventory().addChestKey(cKey.getWhich());
            }
        if (chosenItem.getFlashlight().getCharge() > 0) {
            player.print(
                    translate(FLASHLIGHT, 0)
                            + chosenItem.getFlashlight().getCharge()
                            + translate(FLASHLIGHT, 1));
            player.getInventory().getFlashlight().addCharge(chosenItem.getFlashlight().getCharge());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}