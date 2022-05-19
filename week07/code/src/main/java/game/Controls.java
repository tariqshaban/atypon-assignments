package game;

import java.util.ArrayList;
import java.util.List;

import static game.IoHandler.println;
import static game.assets.StringValues.*;

public class Controls {
  private static char forward = 'W';
  private static char right = 'D';
  private static char left = 'A';
  private static char backward = 'S';
  private static char status = 'T';
  private static char map = 'M';
  private static char reset = 'R';
  private static char affirmative = 'Y';
  private static char negative = 'N';
  private static char checkpoint = 'C';
  private static char load = 'L';

  private Controls() {}

  public static char getForwardKey() {
    return forward;
  }

  public static void setForwardKey(char forward) {
    if (isNotDuplicate(forward)) Controls.forward = forward;
  }

  public static char getRightKey() {
    return right;
  }

  public static void setRightKey(char right) {
    if (isNotDuplicate(right)) Controls.right = right;
  }

  public static char getLeftKey() {
    return left;
  }

  public static void setLeftKey(char left) {
    if (isNotDuplicate(left)) Controls.left = left;
  }

  public static char getBackwardKey() {
    return backward;
  }

  public static void setBackwardKey(char backward) {
    if (isNotDuplicate(backward)) Controls.backward = backward;
  }

  public static char getStatusKey() {
    return status;
  }

  public static void setStatusKey(char status) {
    if (isNotDuplicate(status)) Controls.status = status;
  }

  public static char getMapKey() {
    return map;
  }

  public static void setMapKey(char map) {
    Controls.map = map;
  }

  public static char getResetKey() {
    return reset;
  }

  public static void setResetKey(char reset) {
    Controls.reset = reset;
  }

  public static char getAffirmativeKey() {
    return affirmative;
  }

  public static void setAffirmativeKey(char affirmative) {
    if (isNotDuplicate(affirmative)) Controls.affirmative = affirmative;
  }

  public static char getNegativeKey() {
    return negative;
  }

  public static void setNegativeKey(char negative) {
    if (isNotDuplicate(negative)) Controls.negative = negative;
  }

  public static char getCheckpointKey() {
    return checkpoint;
  }

  public static void setCheckpointKey(char checkpoint) {
    Controls.checkpoint = checkpoint;
  }

  public static char getLoadKey() {
    return load;
  }

  public static void setLoadKey(char load) {
    Controls.load = load;
  }

  private static boolean isNotDuplicate(char checkedCharacter) {
    if (forward == checkedCharacter
        || right == checkedCharacter
        || left == checkedCharacter
        || backward == checkedCharacter
        || map == checkedCharacter
        || reset == checkedCharacter
        || status == checkedCharacter
        || affirmative == checkedCharacter
        || negative == checkedCharacter
        || checkpoint == checkedCharacter
        || load == checkedCharacter) {
      println(CONTROLS_DUPLICATE);
      return false;
    }
    return true;
  }

  private static boolean exists(char batchControls) {
    return batchControls == forward
        || batchControls == right
        || batchControls == left
        || batchControls == backward
        || batchControls == map
        || batchControls == reset
        || batchControls == status
        || batchControls == affirmative
        || batchControls == negative
        || batchControls == checkpoint
        || batchControls == load
        || Character.isDigit(batchControls);
  }

  static List<Character> getValidControls(List<Character> batchControls) {
    ArrayList<Character> removedCharacters = new ArrayList<>();
    for (char control : batchControls) {
      if (!exists(Character.toUpperCase(control))) {
        println(CONTROLS_DNE);
        removedCharacters.add(control);
      }
    }
    batchControls.removeAll(removedCharacters);
    return batchControls;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}