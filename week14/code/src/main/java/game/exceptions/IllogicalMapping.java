package game.exceptions;

import static game.IoHandler.printlnSys;
import static game.assets.StringValues.*;

public class IllogicalMapping extends Exception {
  public IllogicalMapping(String s) {
    super(s);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + translate("TO_STRING_ILLOGICAL_MAPPING");
  }
}