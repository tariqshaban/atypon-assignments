package game.assets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

public class StringValues {

    private static JSONObject fileJSON;

    static {
        try {
            fileJSON = (JSONObject) JSONValue.parse(new FileReader("src/main/java/game/assets/dialogs.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }

    public static String translate(String value) {
        String translatedString = (String) fileJSON.get(value);
        return (translatedString == null) ? value : translatedString;
    }

    public static String translate(String value, int index) {
        String translatedString = (String) ((JSONArray) fileJSON.get(value)).get(index);
        return (translatedString == null) ? value+"."+index : translatedString;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}



