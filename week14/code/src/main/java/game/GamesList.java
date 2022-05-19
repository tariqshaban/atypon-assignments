package game;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static game.assets.StringValues.*;

public class GamesList {
    private static final List<MazeGame> GAMES_LIST = new ArrayList<>();

    private GamesList() {
    }

    public static synchronized int hostGame() {
        MazeGame mazeGame = new MazeGame();
        GAMES_LIST.add(mazeGame);
        return GAMES_LIST.size() - 1;
    }

    public static Map<String, String> joinGame() {
        LinkedHashMap<String, String> games = new LinkedHashMap<>();
        for (MazeGame mazeGame : GAMES_LIST)
            if (!mazeGame.isStarted())
                games.put(mazeGame.getId(),
                        mazeGame.getPlayers().entrySet().iterator().next().getKey().getName() + "`s room - "
                                + mazeGame.getPlayers().size() + "/" + mazeGame.getCapacity() + " player(s) - "
                                + mazeGame.getMap() + " map");
        return games;
    }

    public static List<String> getMaps() {
        ArrayList<String> mapsList = new ArrayList<>();

        File folder = new File(translate("MAP_DIRECTORY"));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) return mapsList;

        for (File listOfFile : listOfFiles)
            if (listOfFile.isFile())
                mapsList.add(listOfFile.getName().substring(0, listOfFile.getName().indexOf('.')));

        return mapsList;
    }

    public static List<MazeGame> getGamesList() {
        return GAMES_LIST;
    }
}