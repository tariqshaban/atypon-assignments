package game;


import game.containers.Room;
import game.containers.interfaces.Container;
import game.containers.lootables.Lootables;
import game.containers.non_lootables.Shop;
import game.loot.ChestKey;
import game.loot.Contents;
import game.loot.DoorKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.Controls.*;
import static game.assets.StringValues.*;

public class Player {

    private static final String TAB = "FORMAT_TAB";
    private static final String SEPARATOR = "FORMAT_SEPARATOR";
    private static final String TIMER_START = "TIMER_START_DIALOG";
    private static final String KILLED = "KILLED";

    private final String id;
    private final String name;
    private MazeGame mazeGame;
    private final Contents inventory = new Contents.Builder().build();
    private final StringBuilder message = new StringBuilder();

    private char command;
    private char facing = getRightKey();
    private Room<Container> location;
    private Room<Container> lastKnownLitRoom;
    private final Random random = new Random();

    private boolean isInitialized = false;

    private int tieBreakerChoice = -1;

    public Player(String name, MazeGame mazeGame) {
        id = new Random().nextInt(999999999) + "";
        this.name = name;
        this.mazeGame = mazeGame;
        getInventory().getGold().setAmount(mazeGame.getInitialGold());
    }

    public void setMazeGame(MazeGame mazeGame) {
        this.mazeGame = mazeGame;
    }

    public Contents getInventory() {
        return inventory;
    }

    public char getFacing() {
        return this.facing;
    }

    public Room<Container> getLocation() {
        return location;
    }

    public void setLocation(Room<Container> location) {
        this.location = location;
    }

    public void setLastKnownLitRoom(Room<Container> lastKnownLitRoom) {
        if (!lastKnownLitRoom.isDark()) this.lastKnownLitRoom = lastKnownLitRoom;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTieBreakerChoice() {
        return tieBreakerChoice;
    }

    public void setTieBreakerChoice(int tieBreakerChoice) {
        this.tieBreakerChoice = tieBreakerChoice;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public void startGame() {
        if (!isInitialized) {
            println(
                    translate(TIMER_START, 0)
                            + mazeGame.getRemainingTime()
                            + translate(TIMER_START, 1)
                            + mazeGame.getGoldThresholdObjective()
                            + translate(TIMER_START, 2));
            location = getRandomLocation();
            lastKnownLitRoom = location;
            isInitialized = true;
            println(translate("UI_AWAITING_COMMAND"));
        }
    }

    private Room<Container> getRandomLocation() {
        int randomLocation;

        while (true) {
            randomLocation = getRandomRoom();

            if (mazeGame.getPlayers().size() >= mazeGame.getRooms().size())
                return mazeGame.getRooms().get(randomLocation);

            if (isRoomClearOfPlayers(mazeGame.getRooms().get(randomLocation)))
                return mazeGame.getRooms().get(randomLocation);
        }
    }

    private int getRandomRoom() {
        int randomRoom;

        while (true) {
            randomRoom = random.nextInt(mazeGame.getRooms().size());
            if (mazeGame.getRooms().get(randomRoom).isDark())
                continue;
            return randomRoom;
        }
    }

    private boolean isRoomClearOfPlayers(Room<Container> randomLocation) {
        for (Player player : mazeGame.getPlayers().keySet())
            if (player.getLocation() != null && player.getLocation().equals(randomLocation))
                return false;
        return true;
    }

    public void move(char inputCommand) {
        command = Character.toUpperCase(inputCommand);
        if (Boolean.TRUE.equals(mazeGame.getPlayers().get(this))) return;
        if (command == getStatusKey()) printStatus(mazeGame);
        else if (command == getExitKey()) kill(this, getNearestContainer().getContents());
        else if (command == getEngageKey()) engagePlayer();
        else if (command == getRockKey()) {
            tieBreakerChoice = 1;
            engagePlayer();
        } else if (command == getPaperKey()) {
            tieBreakerChoice = 2;
            engagePlayer();
        } else if (command == getScissorsKey()) {
            tieBreakerChoice = 3;
            engagePlayer();
        } else if (isClearForManeuver(this, getLocation())) handleManeuver();
        println(translate("UI_AWAITING_COMMAND"));
    }

    private void engagePlayer() {
        Player opponent = null;

        for (Player player : mazeGame.getPlayers().keySet())
            if (getLocation() == player.getLocation() && player != this)
                opponent = player;

        if (opponent != null) {
            if (getInventory().getGold().getAmount() > opponent.getInventory().getGold().getAmount()) {
                kill(opponent, this.getInventory());
                println(opponent.getName() + translate(KILLED));
            } else if (getInventory().getGold().getAmount() < opponent.getInventory().getGold().getAmount()) {
                kill(this, opponent.getInventory());
                opponent.println(getName() + translate(KILLED));
            } else
                triggerTieBreaker(opponent);
        }
    }

    private void triggerTieBreaker(Player opponent) {
        if (tieBreakerChoice == -1) {
            tieBreakerChoice = 0;
        } else if (isEligibleToKill(opponent)) {
            kill(opponent, this.getInventory());
            println(opponent.getName() + translate(KILLED));
        } else if (isEligibleToDie(opponent)) {
            kill(this, opponent.getInventory());
            opponent.println(getName() + translate(KILLED));
        } else if (tieBreakerChoice > 0 && opponent.getTieBreakerChoice() > 0) {
            println(translate("TIE"));
            opponent.println(translate("TIE"));
            tieBreakerChoice = 0;
            opponent.setTieBreakerChoice(0);
        }
    }

    private boolean isEligibleToKill(Player opponent) {
        return tieBreakerChoice == 1 && opponent.getTieBreakerChoice() == 3 || tieBreakerChoice == 2 && opponent.getTieBreakerChoice() == 1 || tieBreakerChoice == 3 && opponent.getTieBreakerChoice() == 2;
    }

    private boolean isEligibleToDie(Player opponent) {
        return tieBreakerChoice == 1 && opponent.getTieBreakerChoice() == 2 || tieBreakerChoice == 2 && opponent.getTieBreakerChoice() == 3 || tieBreakerChoice == 3 && opponent.getTieBreakerChoice() == 1;
    }

    private Lootables getNearestContainer() {
        if (getLocation().getEast() instanceof Lootables)
            return (Lootables) getLocation().getEast();
        if (getLocation().getNorth() instanceof Lootables)
            return (Lootables) getLocation().getNorth();
        if (getLocation().getWest() instanceof Lootables)
            return (Lootables) getLocation().getWest();
        if (getLocation().getSouth() instanceof Lootables)
            return (Lootables) getLocation().getSouth();

        while (true) {
            Room<Container> room = mazeGame.getRooms().get(new Random().nextInt(mazeGame.getRooms().size()));
            if (room.getEast() instanceof Lootables)
                return (Lootables) room.getEast();
            if (room.getNorth() instanceof Lootables)
                return (Lootables) room.getNorth();
            if (room.getWest() instanceof Lootables)
                return (Lootables) room.getWest();
            if (room.getSouth() instanceof Lootables)
                return (Lootables) room.getSouth();
        }
    }

    private void kill(Player victim, Contents beneficiary) {
        int goldPortion = victim.getInventory().getGold().getAmount() / (mazeGame.getPlayers().size() - 1);
        for (Player player : mazeGame.getPlayers().keySet())
            player.getInventory().getGold().addAmount(goldPortion);

        victim.getInventory().getGold().setAmount(0);

        beneficiary.getFlashlight().addCharge(getInventory().getFlashlight().getCharge());
        beneficiary.getChestKeys().addAll(getInventory().getChestKeys());
        beneficiary.getDoorKeys().addAll(getInventory().getDoorKeys());

        mazeGame.getPlayers().put(victim, true);
        victim.setLocation(null);
    }

    public static boolean isClearForManeuver(Player player, Room<Container> location) {
        if (player.isOpponentInRoom()) {
            player.println(translate("DON'T_LEAVE_ENGAGEMENT"));
            return false;
        }


        if (location.isDark() && player.getInventory().getFlashlight().getCharge() <= 0) {
            player.blinded();
            return false;
        }

        if (location.isDark()) {
            player.getInventory().getFlashlight().addCharge(-5);
            player.println(translate("UI_FLASHLIGHT_CHARGE_IS") + player.getInventory().getFlashlight().getCharge());
        }

        return true;
    }

    public boolean isOpponentInRoom() {
        for (Player player1 : mazeGame.getPlayers().keySet())
            if (this.getLocation() == player1.getLocation() && this != player1)
                return true;
        return false;
    }

    private void handleManeuver() {
        if (command == getRightKey()) interactGeneric(location.getEast());
        else if (command == getForwardKey()) interactGeneric(location.getNorth());
        else if (command == getLeftKey()) interactGeneric(location.getWest());
        else if (command == getBackwardKey()) interactGeneric(location.getSouth());
        else println(translate("UI_INVALID_COMMAND"));
    }

    private void blinded() {
        println(translate("UI_BLIND"));
        location = lastKnownLitRoom;
    }

    ///////////////////////////////////////////////////////////

    private void interactGeneric(Container container) {

        facing = command;

        container.onInteractListener(mazeGame, this, getInventory());

    }

    public static boolean isContainerAShop(Player player, char command) {
        if (command == getRightKey()) return player.location.getEast() instanceof Shop;
        else if (command == getForwardKey()) return player.location.getNorth() instanceof Shop;
        else if (command == getLeftKey()) return player.location.getWest() instanceof Shop;
        else if (command == getBackwardKey()) return player.location.getSouth() instanceof Shop;

        return false;
    }

    ///////////////////////////////////////////////////////////

    public List<String> drawMap(Room<Container> location) {
        List<String> images = new ArrayList<>();

        String east = location.getEast().getDisplayName();
        String north = location.getNorth().getDisplayName();
        String west = location.getWest().getDisplayName();
        String south = location.getSouth().getDisplayName();

        images.add("img/" + east + ".png");
        images.add("img/" + north + ".png");
        images.add("img/" + west + ".png");
        images.add("img/" + south + ".png");

        return images;
    }

    ///////////////////////////////////////////////////////////

    public void printStatus(MazeGame mazeGame) {
        println();
        println(translate("STATUS_TITLE"));
        println(translate(SEPARATOR));
        println(
                translate("STATUS_GOLD")
                        + getInventory().getGold().getAmount()
                        + translate("FORMAT_DIVISION")
                        + mazeGame.getGoldThresholdObjective());
        println(translate(SEPARATOR));
        if (!getInventory().getDoorKeys().isEmpty() || !getInventory().getChestKeys().isEmpty()) {
            print(translate("STATUS_KEYS"));
            if (!getInventory().getDoorKeys().isEmpty()) {
                print(translate("STATUS_DOOR_KEYS"));
                for (DoorKey doorKey : getInventory().getDoorKeys())
                    print(translate(TAB) + doorKey.getWhich());
            }
            if (!getInventory().getChestKeys().isEmpty()) {
                println();
                print(translate(TAB));
                print(translate("STATUS_CHEST_KEYS"));
                for (ChestKey chestKey : getInventory().getChestKeys())
                    print(translate(TAB) + chestKey.getWhich());
            }
            println();
            println(translate(SEPARATOR));
        }
        if (getInventory().getFlashlight().getCharge() > 0) {
            print(translate("STATUS_FLASHLIGHT"));
            println(getInventory().getFlashlight().getCharge() + translate("FORMAT_PERCENT"));
            println(translate(SEPARATOR));
        }
        println(translate("STATUS_REMAINING_TIME", 0) + Math.max(mazeGame.getRemainingTime(), 0) + translate("STATUS_REMAINING_TIME", 1));
        println(translate(SEPARATOR));
    }

    public void print(String string) {
        message.append(string);
    }

    public void println(String string) {
        message.append(string).append("<br>");
    }

    public void println() {
        message.append("<br>");
    }

    public void clearBuffer() {
        message.setLength(0);
    }

    public String getMessageBuffer() {
        String bufferedMessage = message.toString();
        message.setLength(0);
        return bufferedMessage;
    }
}