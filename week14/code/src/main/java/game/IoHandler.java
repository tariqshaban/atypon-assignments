package game;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class IoHandler {

  private static final Logger logger = Logger.getLogger(IoHandler.class.getName());

  private static String batch = "";

  private IoHandler() {}

  public static void printlnSys(String string) {
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

  public static void printlnSys() {
    logger.debug(batch);
    batch = "";
  }

  public static String stackTraceToString(Exception e){
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    return sw.toString();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}