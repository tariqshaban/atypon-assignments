package game;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class IoHandler {

  private static final Logger logger = Logger.getLogger(IoHandler.class.getName());
  private static final Scanner scanner = new Scanner(System.in);

  private static String batch = "";

  private IoHandler() {}

  public static char getUserInput() {
    return scanner.next().toUpperCase().charAt(0);
  }

  public static void println(String string) {
    string = batch + string;
    while (string.contains("\n")) {
      if (string.contains("\n\n")) break;

      String line = string.substring(0, string.indexOf("\n"));
      logger.debug(line);
      string = string.substring(string.indexOf("\n") + 1);
      batch = "";
    }
    logger.debug(string);
    batch = "";
  }

  public static void println() {
    logger.debug(batch);
    batch = "";
  }

  public static void print(String string) {
    batch += string;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}