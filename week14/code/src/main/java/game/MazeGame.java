package game;

import game.containers.*;
import game.containers.interfaces.Container;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Lootables;
import game.exceptions.ExceptionInfoHolder;
import game.exceptions.IllogicalMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static game.IoHandler.printlnSys;
import static game.assets.StringValues.*;

public class MazeGame {

    private final String id;
    private String map = "";

    private final ConcurrentHashMap<Player, Boolean> players = new ConcurrentHashMap<>();

    private boolean isStarted = false;

    private String jsonDirectory;

    private final Countdown countdown = new Countdown(this);
    private final Thread thread = new Thread(countdown);
    private int goldThresholdObjective = 999999;
    private final List<Room<Container>> rooms = new ArrayList<>();
    private final Store store = new Store();

    private int initialGold;
    private boolean isDecoded = false;

    public MazeGame() {
        id = new Random().nextInt(999999999) + "";
    }

    public MazeGame(String id) {
        this.id = id;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public Map<Player, Boolean> getPlayers() {
        return players;
    }

    public int getRemainingTime() {
        return countdown.getRemaining();
    }

    public int getInitialGold() {
        return initialGold;
    }

    public String getId() {
        return id;
    }

    public int getGoldThresholdObjective() {
        return goldThresholdObjective;
    }

    public void setGoldThresholdObjective(int goldThresholdObjectiveParam) {
        goldThresholdObjective = goldThresholdObjectiveParam;
    }

    public List<Room<Container>> getRooms() {
        return rooms;
    }

    public String getMap() {
        return map;
    }

    public Store getStore() {
        return store;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getJsonDirectory() {
        return jsonDirectory;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean runGame(String chosenMap) {
        try {
            decodeJSON(chosenMap);
        } catch (FileNotFoundException e) {
            printlnSys(translate("EXCEPTION_JSON_ERR_FILE_NOT_FOUND"));
            return false;
        } catch (Exception e) {
            printlnSys(translate("EXCEPTION_JSON_ERR"));
            printlnSys(translate("FORMAT_SEPARATOR"));
            printlnSys(e.getMessage());
            printlnSys(translate("EXCEPTION_LINE") + e.getStackTrace()[0].getLineNumber());
            return false;
        }
        try {
            checkMappingLogic();
        } catch (IllogicalMapping e) {
            printlnSys(e.getMessage());
        }
        startGame();
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private synchronized void decodeJSON(String chosenMap) throws FileNotFoundException {
        map = chosenMap;
        if (isDecoded)
            return;

        isDecoded = true;

        jsonDirectory = translate("JSON_DIR") + chosenMap + ".json";

        JSONObject fileJSON = (JSONObject) JSONValue.parse(new FileReader(jsonDirectory));

        JSONArray roomsJSON = (JSONArray) fileJSON.get(translate("JSON_ROOMS"));

        if (countdown.getRemaining() == 0)
            countdown.setStartingCountdown(jsonToInt(fileJSON, translate("JSON_REMAINING_TIME")));
        initialGold = jsonToInt(fileJSON, translate("JSON_INITIAL_GOLD"));
        setGoldThresholdObjective(jsonToInt(fileJSON, translate("JSON_GOLD_THRESHOLD")));

        for (Object room : roomsJSON) {
            boolean isDark = (boolean) ((JSONObject) room).get(translate("JSON_IS_DARK"));

            String eastContainerType = getContainerType(room, translate("JSON_EAST"));
            String northContainerType = getContainerType(room, translate("JSON_NORTH"));
            String westContainerType = getContainerType(room, translate("JSON_WEST"));
            String southContainerType = getContainerType(room, translate("JSON_SOUTH"));

            getRooms()
                    .add(
                            new Room<>(
                                    isDark,
                                    getContainer(((JSONObject) room).get(translate("JSON_EAST")), eastContainerType),
                                    getContainer(((JSONObject) room).get(translate("JSON_NORTH")), northContainerType),
                                    getContainer(((JSONObject) room).get(translate("JSON_WEST")), westContainerType),
                                    getContainer(((JSONObject) room).get(translate("JSON_SOUTH")), southContainerType)));
        }
    }

    public String getContainerType(Object room, String json) {
        JSONObject child = (JSONObject) ((JSONObject) room).get(json);
        return (String) child.get(translate("JSON_TYPE"));
    }

    public int jsonToInt(Object jsonObject, String location) {
        return ((Long) ((JSONObject) jsonObject).get(location)).intValue();
    }

    private Container getContainer(Object roomSide, String flag) throws FileNotFoundException {
        for (Container container : ContainerList.getContainersList().values()) {
            if (container
                    .getClass()
                    .isAssignableFrom(ContainerList.getContainersList().get(flag).getClass())) {
                return container.handleContainerJSONContents(this, (JSONObject) roomSide);
            }
        }
        return ContainerList.getContainersList().entrySet().iterator().next().getValue();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkMappingLogic() throws IllogicalMapping {

        if (countdown.getRemaining() < 60)
            throw new IllogicalMapping(translate("EXCEPTION_ILLOGICAL_REMAINING_TIME"));

        if (initialGold < 0)
            throw new IllogicalMapping(translate("EXCEPTION_ILLOGICAL_STARTING_GOLD_NEGATIVE"));

        if (getGoldThresholdObjective() <= initialGold)
            throw new IllogicalMapping(translate("EXCEPTION_ILLOGICAL_STARTING_GOLD"));

        if (isGoldInsufficient()) throw new IllogicalMapping(translate("EXCEPTION_ILLOGICAL_GOLD_SUM"));

        if (getRooms().get(0).isDark())
            throw new IllogicalMapping(translate("EXCEPTION_ILLOGICAL_SPAWN_ROOM_LIGHTNING"));

        ExceptionInfoHolder exceptionInfoHolder;
        for (ContainerWithLogicCheck containerWithLogicCheck :
                ContainerList.getLogicCheckList().values()) {
            exceptionInfoHolder = containerWithLogicCheck.isLogical(this);
            if (!exceptionInfoHolder.isSuccess())
                throw new IllogicalMapping(exceptionInfoHolder.getReason());
        }
    }

    private boolean isGoldInsufficient() {
        List<Container> containerList = new ArrayList<>();
        int totalGold = initialGold;
        int containerGold;

        for (Room<Container> room : getRooms()) {
            containerList.add(room.getEast());
            containerList.add(room.getNorth());
            containerList.add(room.getWest());
            containerList.add(room.getSouth());

            for (Container container : containerList)
                if (container instanceof Lootables) {
                    containerGold = ((Lootables) container).getContents().getGold().getAmount();
                    if (containerGold != -1) totalGold += containerGold;
                }

            containerList.clear();
        }

        for (int i : store.getStoreItems().values()) totalGold -= i;

        return totalGold < getGoldThresholdObjective();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getCapacity() {
        int availableRooms = 0;
        for (Room<Container> room : rooms)
            if (!room.isDark()) availableRooms++;
        return availableRooms;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void startGame() {
        if (!countdown.isStarted()) thread.start();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + id;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeGame mazeGame = (MazeGame) o;
        return Objects.equals(id, mazeGame.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}