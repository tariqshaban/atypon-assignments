package game.containers.non_lootables;

import game.*;
import game.containers.interfaces.Container;
import game.containers.Room;
import game.containers.interfaces.ContainerWithLogicCheck;
import game.containers.lootables.Lootables;
import game.exceptions.ExceptionInfoHolder;
import game.loot.Contents;
import game.loot.DoorKey;
import org.json.simple.JSONObject;

import java.util.*;

import static game.assets.StringValues.*;
import static game.containers.lootables.Lootables.jsonToInt;

public class Door implements Container, ContainerWithLogicCheck {
    private static final long serialVersionUID = 4207715701373295444L;

    private static final String ILLOGICAL_CONTAINER = "EXCEPTION_ILLOGICAL_CONTAINER_DOOR";
    private static final String TO_STRING = "TO_STRING_DOOR";
    private static final String TAB = "FORMAT_TAB";

    @Override
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    private static int noDoors = -1;
    private final int id;
    private final int to;
    private boolean isLocked;

    public Door(int to, boolean isLocked) {
        id = ++noDoors;
        this.to = to;
        this.isLocked = isLocked;
    }

    public int getId() {
        return id;
    }

    public int getTo() {
        return to;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public Container handleContainerJSONContents(MazeGame mazeGame, JSONObject jsonObject) {
        return new Door(
                jsonToInt(jsonObject, translate("JSON_WHICH_TO")) - 1, (boolean) (jsonObject).get(translate("JSON_WHICH_IS_LOCKED")));
    }

    @Override
    public void onInteractListener(MazeGame mazeGame, Player player, Contents inventory) {
        if (this.isLocked()) {
            if (player.getInventory()
                    .getDoorKeys()
                    .contains(new DoorKey(this.getId()))) { // Implicitly calls equals
                player.println(translate("UI_DOOR_UNLOCKED") + this.getId());
                this.setLocked(false);
            } else player.println(translate("UI_DOOR_LOCKED") + this.getId());
        } else if (isOppositeDoorLocked(mazeGame.getRooms().get(this.getTo()), player.getFacing())) {
            Door oppositeDoor = getOppositeDoor(mazeGame.getRooms().get(this.getTo()), player.getFacing());
            if (player.getInventory()
                    .getDoorKeys()
                    .contains(new DoorKey(oppositeDoor.getId()))) { // Implicitly calls equals
                player.println(translate("UI_OPPOSITE_DOOR_UNLOCKED") + oppositeDoor.getId());
                oppositeDoor.setLocked(false);
            } else player.println(translate("UI_OPPOSITE_DOOR_LOCKED") + oppositeDoor.getId());
        } else if (fightExists(mazeGame, mazeGame.getRooms().get(this.getTo()))) {
            player.println(translate("UI_OPPOSITE_DOOR_UNLOCKED"));
        } else if (Player.isClearForManeuver(player, mazeGame.getRooms().get(this.getTo()))) {
            player.setLocation(mazeGame.getRooms().get(this.getTo()));
            player.setLastKnownLitRoom(player.getLocation());
        }
    }

    private static boolean isOppositeDoorLocked(Room<Container> containerRoom, char facing) {
        return getOppositeDoor(containerRoom, facing).isLocked;
    }

    private static boolean fightExists(MazeGame mazeGame, Room<Container> room) {
        int playersNo = 0;
        for (Player player : mazeGame.getPlayers().keySet()) {
            if (player.getLocation() == room)
                playersNo++;
        }
        return playersNo > 1;
    }

    private static Door getOppositeDoor(Room<Container> containerRoom, char facing) {
        if (facing == Controls.getRightKey()) return ((Door) containerRoom.getWest());
        else if (facing == Controls.getForwardKey()) return ((Door) containerRoom.getSouth());
        else if (facing == Controls.getLeftKey()) return ((Door) containerRoom.getEast());
        if (facing == Controls.getBackwardKey()) return ((Door) containerRoom.getNorth());

        // This return statement will never be reached since it is ensured that there is a door in the
        // next room where the user is facing when interacting with this door
        return (Door) containerRoom.getEast();
    }

    private static boolean isOppositeNotADoor(MazeGame mazeGame, List<Container> roomsContainers) {
        Container east = roomsContainers.get(0);
        Container north = roomsContainers.get(1);
        Container west = roomsContainers.get(2);
        Container south = roomsContainers.get(3);

        return east instanceof Door
                && !(mazeGame.getRooms().get(((Door) east).getTo()).getWest() instanceof Door)
                || north instanceof Door
                && !(mazeGame.getRooms().get(((Door) north).getTo()).getSouth() instanceof Door)
                || west instanceof Door
                && !(mazeGame.getRooms().get(((Door) west).getTo()).getEast() instanceof Door)
                || south instanceof Door
                && !(mazeGame.getRooms().get(((Door) south).getTo()).getNorth() instanceof Door);
    }

    public static void resetCounter() {
        noDoors = 0;
    }

    @Override
    public ExceptionInfoHolder isLogical(MazeGame mazeGame) {
        List<Container> containerList = new ArrayList<>();

        String message = translate("FORMAT_SPACE");
        String keyRange = (noDoors == 0) ? translate("EXCEPTION_NO_KEYS") : translate("EXCEPTION_EXPECTED") + noDoors;

        for (Room<Container> room : mazeGame.getRooms()) {
            containerList.add(room.getEast());
            containerList.add(room.getNorth());
            containerList.add(room.getWest());
            containerList.add(room.getSouth());

            if (!(room.getEast() instanceof Door)
                    && !(room.getNorth() instanceof Door)
                    && !(room.getWest() instanceof Door)
                    && !(room.getSouth() instanceof Door)) {
                message =
                        translate(ILLOGICAL_CONTAINER, 0)
                                + getClass().getSimpleName()
                                + translate(ILLOGICAL_CONTAINER, 4)
                                + translate(ILLOGICAL_CONTAINER, 7)
                                + getClass().getSimpleName()
                                + translate(ILLOGICAL_CONTAINER, 8)
                                + room.getEast().getClass().getSimpleName()
                                + translate(TAB)
                                + room.getNorth().getClass().getSimpleName()
                                + translate(TAB)
                                + room.getWest().getClass().getSimpleName()
                                + translate(TAB)
                                + room.getSouth().getClass().getSimpleName();
                return new ExceptionInfoHolder(false, message);
            }
        }

        if (isDoorPointingAtNoRoom(containerList)) {

            message =
                    translate(ILLOGICAL_CONTAINER, 0)
                            + getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 1)
                            + translate(ILLOGICAL_CONTAINER, 7)
                            + mazeGame.getRooms().get(0).getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 8)
                            + translate("FORMAT_NULL");
            return new ExceptionInfoHolder(false, message);

        } else if (isDoorWayLooping(containerList)) {

            message =
                    translate(ILLOGICAL_CONTAINER, 0)
                            + getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 2);
            return new ExceptionInfoHolder(false, message);

        } else if (isDoorFacesNoDoor(mazeGame, containerList)) {

            message =
                    translate(ILLOGICAL_CONTAINER, 0)
                            + getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 3);
            return new ExceptionInfoHolder(false, message);

        } else if (isKeyMatchesNoDoor(mazeGame, containerList)) {

            message =
                    translate(ILLOGICAL_CONTAINER, 0)
                            + getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 5)
                            + translate(ILLOGICAL_CONTAINER, 7)
                            + keyRange
                            + translate(ILLOGICAL_CONTAINER, 8)
                            + translate("EXCEPTION_OTHERWISE");
            return new ExceptionInfoHolder(false, message);
        } else if (isLockedDoorWithoutKey(mazeGame, containerList)) {

            message =
                    translate(ILLOGICAL_CONTAINER, 0)
                            + getClass().getSimpleName()
                            + translate(ILLOGICAL_CONTAINER, 6);
            return new ExceptionInfoHolder(false, message);
        }

        resetCounter();

        return new ExceptionInfoHolder(true, message);
    }

    private boolean isDoorPointingAtNoRoom(List<Container> containerList) {
        for (Container container : containerList) {
            if (container instanceof Door && ((Door) container).to >= containerList.size() / 4
                    || container instanceof Door && ((Door) container).to < 0) return true;
        }
        return false;
    }

    private boolean isDoorWayLooping(List<Container> containerList) {
        double roomNo = 0;
        for (Container container : containerList) {
            if (container instanceof Door && ((Door) container).to == (int) roomNo) return true;
            roomNo += 0.25;
        }
        return false;
    }

    private boolean isDoorFacesNoDoor(MazeGame mazeGame, List<Container> containerList) {
        double roomNo = 0;

        for (Container container : containerList) {
            if (container instanceof Door
                    && isOppositeNotADoor(mazeGame, containerList.subList((int) roomNo * 4, (int) roomNo * 4 + 4)))
                return true;
            roomNo += 0.25;
        }
        return false;
    }

    private boolean isKeyMatchesNoDoor(MazeGame mazeGame, List<Container> containerList) {

        for (Container container : containerList) {
            if (container instanceof Lootables
                    && !((Lootables) container).getContents().getDoorKeys().isEmpty()
                    && Collections.max(
                    ((Lootables) container).getContents().getDoorKeys(),
                    (a, b) -> Float.compare(a.getWhich(), b.getWhich()))
                    .getWhich()
                    > noDoors) return true;
        }

        for (Contents contents : mazeGame.getStore().getStoreItems().keySet())
            for (DoorKey doorKey : contents.getDoorKeys())
                if (doorKey.getWhich() <= 0 || doorKey.getWhich() > noDoors)
                    return true;


        return false;
    }

    private boolean isLockedDoorWithoutKey(MazeGame mazeGame, List<Container> containerList) {
        Set<Integer> doorKeys = new HashSet<>();
        Set<Integer> doors = new HashSet<>();

        for (Container container : containerList) {
            if (container instanceof Lootables)
                for (DoorKey doorKey : ((Lootables) container).getContents().getDoorKeys())
                    doorKeys.add(doorKey.getWhich());

            if (container instanceof Door && ((Door) container).isLocked())
                doors.add(((Door) container).id);
        }

        for (Contents contents : mazeGame.getStore().getStoreItems().keySet())
            for (DoorKey doorKey : contents.getDoorKeys()) doorKeys.add(doorKey.getWhich());

        return !doorKeys.equals(doors);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + translate(TO_STRING, 0)
                + id
                + translate(TO_STRING, 1)
                + noDoors
                + translate(TO_STRING, 2)
                + to
                + translate(TO_STRING, 3)
                + isLocked;
    }
}