package game.exceptions;

import static game.IoHandler.println;
import static game.assets.StringValues.*;

public class IllogicalMapping extends Exception {
  public IllogicalMapping(String s) {
    super(s);
    println(s);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + TO_STRING_ILLOGICAL_MAPPING;
  }
}