import stanford.karel.SuperKarel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;

public class Homework extends SuperKarel {
    //A manually-set flag that determines whether to navigate Karel to the start point in a simple maze or to just return to the south-west corner
    static final boolean EXPLORE=false;

    //Global variables which is shared by the three functions
    boolean returning=false, iterator=true;
    int beepersPut=0;

    //Variables allocated for the first function
    boolean singularHeight=false, stopWidth=false;
    int cornersVisited=0;

    //To increase performance, if the program failed to play the audio, it will never try again.
    boolean audioFailed=false;
    //A clip object, which is used to load the wav audio file
    Clip clip;
    {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            System.out.println("*********************************************");
            System.out.println("Err: Could Not Play Audio, Permission Denied From The System");
            audioFailed=true;
        }
    }

    //Global variables that stores Karel's position as well as the board's proportions
    int height=1 , width=1, coordinateX=1, coordinateY=1;

    public void run(){
        //Runs an audio file when clicking start
        playSound("Start.wav");
        if(!audioFailed)
            pause(1250);

        //Some maps may set default beepers to 0, which mat cause an error when putting one, hence, it should be set to a high value
        setBeepersInBag(1000);

        if(EXPLORE)
            findStartPoint();
        else
            returnToStartPoint();

        System.out.println("*********************************************");

        //Executes the requirements accordingly
        exeLvl1();
        exeLvl2();
        exeLvl3();

        if (clip.isOpen())
            clip.close();
    }


    //A function that allows Karel to exit a simple maze, then reaching the south-west corner
    //TODO function does not work on some mazes and may not exit it properly
    private void findStartPoint() {
        /*
        //  width       : the width of the box in which Karel is currently in
        //  height      : the height of the box in which Karel is currently in
        //  widthPrev   : the previous width of the box in which Karel was previously in
        //  heightPrev  : the previous height of the box in which Karel was previously in
        //
        //  This function will record these variables and breaks whenever the heightPrev/widthPrev is equal to height/width (but not equal to one)
        //   and then calls the returnToStartPoint() function, returning karel to the default start point
         */
        int width=1, height=1, widthPrev=1, heightPrev=1;
        //A flag which iterates between whether to start counting the height or the width
        boolean countingTurn=false;
        //Ensures that Karel is by a side
        stickToBorder();

        //A loop that runs indefinitely until the mentioned variables are equal, while so, it will attempt to go right it there is an opening in the box
        while(true) {
            if(frontIsClear()) {
                if(rightIsClear() && !backIsBlocked())
                    turnRight();
                if(frontIsClear()) {
                    if(countingTurn) width++;
                    else height++;
                    move();
                }
            } else {
                if(widthPrev==width && heightPrev==height && (width!=1 || height!=1)) {
                    returnToStartPoint();
                    break;
                }
                if(!countingTurn){
                    widthPrev=width;
                    width=1;
                }
                else{
                    heightPrev=height;
                    height=1;
                }
                countingTurn=!countingTurn;
                if(leftIsClear())
                    turnLeft();
                else  turnRight();
            }
        }
    }

    //A function that runs forward until it is blocked, then turns right
    private void stickToBorder(){
        while(true) {
            if(frontIsClear())
                move();
            else {
                turnRight();
                break;
            }
        }
    }

    //A function that allows Karel to return to the south-west and facing east
    private void returnToStartPoint() {
        if(!isHome()) {
            //Adjust Karel's orientation to face west
            while (!facingWest())
                turnLeft();

            //Moves Karel to the utmost west
            while (true) {
                if (frontIsClear())
                    move();
                else {
                    turnLeft();
                    break;
                }
            }

            //Moves Karel to the utmost south
            while (true) {
                if (frontIsClear())
                    move();
                else {
                    turnLeft();
                    break;
                }
            }
        }
    }

    //Checks if Karel is at the default positioning
    private boolean isHome() {
        return facingEast() && rightIsBlocked() && backIsBlocked();
    }



    //Executes the first requirement
    private void exeLvl1() {
        //Sets to default values to make sure that previous runs would not interfere with the results
        width=1;
        height=1;

        //Pick beeper at the first tile
        if(beepersPresent()) {
            playSound("Pick.wav");
            pickBeeper();
        }


        while(true) {
            if(!returning)
                if (!stopWidth) //Flag to indicate that counting the width is done (when Karel turns and front is blocked)
                    width++;
            if(frontIsClear())
                move();
            if(returning)
                removeBeepersReturning();
            else removeBeepersComing();
            addBeepers1();
            if(frontIsBlocked()){
                //Handle 1x8 special case
                if(rightIsBlocked() && backIsBlocked()) {
                    turnLeft();
                    move();
                    if(!returning)
                    removeBeepersComing();
                    addBeepers1();
                //Handles south-east corner
                } else if(!returning && facingEast() && leftIsClear()){
                    turnLeft();
                    move();
                    height++;
                    turnLeft();
                    if(!returning)
                    removeBeepersComing();
                    stopWidth=true;
                    iterator = true;
                //Handles west side
                } else if(!returning && facingWest() && rightIsClear()) {
                    turnRight();
                    move();
                    height++;
                    turnRight();
                    if(!returning)
                    removeBeepersComing();
                    iterator = true;
                //Invoked when Karel is finished of putting beepers and visited all corners
                } else if(!returning) {
                    //Checks if the world has an odd height or not, so that it can determine at which corner it should stop when returning
                    if(facingEast() && leftIsBlocked()) {
                        singularHeight = true;
                        cornersVisited-=3;
                    }
                    //Handle 8x1 and 1x8 special case
                    if(rightIsBlocked() && frontIsBlocked() && leftIsBlocked()) {
                        cornersVisited = 5;
                    }
                    turnAround();
                    returning=true;
                //Handles Karel's movement when facing a blocked side while returning
                } else if(cornersVisited++<3){
                    if(singularHeight)
                        turnLeft();
                    else turnRight();
                //When Karel has returned to the original point, it will turn around
                } else if(cornersVisited<5){
                    turnAround();
                //Finalize
                } else {
                    //Resetting variables and positioning to default
                    if(facingWest())
                    turnAround();
                    else turnLeft();
                    returning=false;
                    iterator=true;
                    cornersVisited=0;
                    stopWidth=false;
                    singularHeight=false;
                    System.out.println("Beepers Put in First Level: \t"+beepersPut);
                    beepersPut=0;

                    //TODO remove bug hotfix and find source
                    if(frontIsBlocked() && rightIsBlocked() && backIsBlocked()) {
                        //noinspection SuspiciousNameCombination
                        height=width;
                        width=1;
                    }
                    break;
                }
                removeBeepersReturning();
            } else if(beepersPresent() && !returning && iterator) {
                playSound("Pick.wav");
                pickBeeper();
            }
        }
    }

    //A function that add beepers for the first requirement
    //Puts only if Karel is by a side
    //Puts only if previous corner does not have a beeper
    private void addBeepers1(){
        //Last block of conditions is for the 1x8 special case
        if(!returning && (rightIsBlocked() || frontIsBlocked() || leftIsBlocked()) && (!rightIsBlocked() || !frontIsBlocked() || !backIsBlocked()))
            if(iterator) {
                if(!beepersPresent()) {
                    playSound("Put.wav");
                    putBeeper();
                    beepersPut++;
                }
                iterator = false;
            } else
                iterator = true;
    }

    //Executes the second requirement
    private void exeLvl2() {
        iterator=false;
        //Put beeper at the first corner
        if(!beepersPresent()) {
            playSound("Put.wav");
            putBeeper();
            beepersPut++;
        }
        while(true) {
            //Keep moving if it is clear
            if(frontIsClear())
            move();

            //Calls remove beepers function depending on the state
            if(returning)
                removeBeepersReturning();
            else removeBeepersComing();

            addBeepers2();
            if(frontIsBlocked()){
                //Handle 1x8 special case
                if(rightIsBlocked() && backIsBlocked()) {
                    turnLeft();
                    addBeepers2();
                    iterator = true;
                    move();
                //Checks is left is clear so Karel wont be blocked, then moves to the next/previous row
                } else if(!returning && facingEast() && leftIsClear() || returning && facingWest() && leftIsClear()){
                    turnLeft();
                    move();
                    turnLeft();
                    if(!returning)
                    removeBeepersComing();
                    addBeepers2();
                //Checks is right is clear so Karel wont be blocked, then moves to the next/previous row
                } else if(!returning && facingWest() && rightIsClear() || returning && facingEast() && rightIsClear()) {
                    turnRight();
                    move();
                    turnRight();
                    if(!returning)
                    removeBeepersComing();
                    addBeepers2();
                //If the previous statements are not satisfied, this means that Karel has iterated through all of the corners, and now its returning
                } else if(!returning) {
                    turnAround();
                    returning=true;
                //Finalize
                } else {
                    //Resetting variables and positioning to default
                    turnAround();
                    returning=false;
                    iterator=true;
                    System.out.println("Beepers Put in Second Level: \t"+beepersPut);
                    beepersPut=0;
                    break;
                }
                //Implicitly checks if it is returning or not
                removeBeepersReturning();
            }
        }
    }

    //A function that add beepers for the second requirement
    //Puts only if previous corner does not have a beeper
    private void addBeepers2(){
        if(!returning)
        if(iterator) {
            if(!beepersPresent()) {
                playSound("Put.wav");
                putBeeper();
                beepersPut++;
            }
            iterator = false;
        } else
            iterator = true;
    }

    //Executes the third requirement
    private void exeLvl3() {
        //Removes beeper at the first corner
        if(beepersPresent()) {
            playSound("Pick.wav");
            pickBeeper();
        }
        while(true) {
            //Keep moving if it is clear
            if(frontIsClear()) {
                move();
                updateCoordinates();
            }

            //Handles Karel's return mechanism
            if(returning){
                //Finalize
                if(coordinateX==1 && coordinateY==1){
                    //Resetting variables and positioning to default
                    turnLeft();
                    if(facingSouth())
                    turnLeft();
                    returning=false;
                    System.out.println("Beepers Put in Third Level: \t"+beepersPut);
                    beepersPut=0;
                    coordinateX=1;
                    coordinateY=1;
                    break;
                //Turn left to start picking up beepers at this column/row
                } else if(isCentered() && (isCentered(coordinateX+1,coordinateY) || isCentered(coordinateX,coordinateY+1))  && beepersPresent() && leftIsClear() && rightIsBlocked())
                    turnLeft();
                //Turn right to start picking up beepers at this column/row
                else if(isCentered() && (isCentered(coordinateX-1,coordinateY) || isCentered(coordinateX,coordinateY-1)) && beepersPresent() && rightIsClear() && leftIsBlocked())
                    turnRight();
                //Go to the next column/row (if it exists)
                else if(frontIsBlocked() && beepersPresent()) {
                    removeBeepersReturning();
                    turnLeft();
                    move();
                    updateCoordinates();
                    turnLeft();
                //Keep moving by the side it the column/row is already swept
                } else if(isCentered() && !beepersPresent() && frontIsBlocked() || frontIsBlocked())
                    turnLeft();
                removeBeepersReturning();
            } else removeBeepersComing3();
            addBeepers3();
            if(frontIsBlocked() && !returning){
                //Handle 1x8 special case
                if(rightIsBlocked() && backIsBlocked()) {
                    turnLeft();
                    move();
                    updateCoordinates();
                    removeBeepersComing3();
                //Checks is left is clear so Karel wont be blocked, then moves to the next/previous row
                } else if(facingEast() && leftIsClear()){
                    turnLeft();
                    move();
                    updateCoordinates();
                    turnLeft();
                    removeBeepersComing3();
                    addBeepers3();
                //Checks is right is clear so Karel wont be blocked, then moves to the next/previous row
                } else if(facingWest() && rightIsClear()) {
                    turnRight();
                    move();
                    updateCoordinates();
                    turnRight();
                    removeBeepersComing3();
                    addBeepers3();
                //If the previous statements are not satisfied, this means that Karel has iterated through all of the corners, and now its returning
                } else {
                    turnAround();
                    returning=true;
                }
                removeBeepersReturning();
            }
        }
    }


    //Handles beepers removal for the third requirement
    private void removeBeepersComing3() {
        if(!returning && beepersPresent() && !isCentered()) {
            playSound("Pick.wav");
            pickBeeper();
        }
    }

    //A function that add beepers for the third requirement
    private void addBeepers3(){
        if(!returning && !beepersPresent() && isCentered()) {
            playSound("Put.wav");
                putBeeper();
                beepersPut++;
            }
    }

    //A function that is required from the third requirement. Returns true if Karel is centered horizontally or vertically, returns false otherwise
    private boolean isCentered() {
        //Function variables that are temporarily used to determine is centered horizontally or vertically
        boolean isXCentered=false, isYCentered=false;

        //width must be at least 3 to be liable as centred or not
        if(width!=1 && width!=2)
            //There will be two columns is the width is even
            if(width%2==0)
                isXCentered=(width/2==coordinateX || width/2+1==coordinateX);
            else
                isXCentered=(width/2+1==coordinateX);

        //height must be at least 3 to be liable as centred or not
        if(height!=1 && height!=2)
            //There will be two rows is the height is even
            if(height%2==0)
                isYCentered=(height/2==coordinateY || height/2+1==coordinateY);
            else
                isYCentered=(height/2+1==coordinateY);

        //A coordinate is said to be centered if it was centered horizontally or vertically
        return isXCentered || isYCentered;
    }

    //An overload to the previous method which does the same functionality, but it is given the coordinates manually
    private boolean isCentered(int karel_x,int karel_y) {
        boolean isXCentered=false, isYCentered=false;
        if(width!=1 && width!=2)
            if(width%2==0)
                isXCentered=(width/2==karel_x || width/2+1==karel_x);
            else
                isXCentered=(width/2+1==karel_x);

        if(height!=1 && height!=2)
            if(height%2==0)
                isYCentered=(height/2==karel_y || height/2+1==karel_y);
            else
                isYCentered=(height/2+1==karel_y);
        return isXCentered || isYCentered;
    }

    //A simple function that updates Karel's position
    private void updateCoordinates() {
        if(facingNorth())
            coordinateY++;
        else if(facingSouth())
            coordinateY--;
        else if(facingEast())
            coordinateX++;
        else if(facingWest())
            coordinateX--;
    }

    //Handles beepers removal for the first/second requirement
    private void removeBeepersComing(){
        if(!returning && !iterator){
            if(beepersPresent()) {
                playSound("Pick.wav");
                pickBeeper();
            }
        }
    }

    //Remove all beepers if Karel is returning
    private void removeBeepersReturning(){
        if(returning && beepersPresent()) {
            playSound("Pick.wav");
            pickBeeper();
        }
    }

    //A function that checks if Karel is blocked behind by turning left and then checking if its left is clear, then turning right. Not visually optimal
    private boolean backIsBlocked() {
        turnLeft();
        if(leftIsBlocked()) {
            turnRight();
            return true;
        }
        turnRight();
        return false;
    }

    //A function which loads the wav audio file into the clip object
    void playSound(String soundFile) {
        if(!audioFailed)
        try {
            if(!clip.isRunning()) {
                File f = new File("./src/" + soundFile);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                if (clip.isOpen())
                    clip.close();
                clip.open(audioIn);
                clip.start();
            }
        } catch(Exception e){
            System.out.println("*********************************************");
            System.out.println("Err: Could Not Play Audio, File Was Not Found");
            audioFailed=true;
        }
    }
}