Maze Game
==============================

A single-player application that implements a maze game on the console with persistent properties.

Assignment Requirements
------------

* The idea is to write a console game where the user issues commands to navigate and act on a map.
* The map is made of a graph of Rooms.
* Each room can be thought of as a logical square and has four walls, a wall can have a painting, a chest, a
  mirror, a door, a seller or is just a plain wall. The player starts at the <Start> room and is facing one of the four
  walls and if the map specifies, the player can start with an initial amount of gold.
* Commands:
    * Navigation commands: left, right, forward, backward, player status (gives which direction the player is
      facing North, South, West, East and the amount of gold and items that he has)
    * Forward and backward will only move through open doors and left and right will change the orientation of
      the player.
    * look: this command returns the following (Does not work if the room is dark):
        * Dark, if the room is not lit.
        * Door, if the room is lit and the player is facing a door.
        * Wall, if the room is lit and the player is facing an empty wall.
        * Chest, if the room is lit and the player is facing a chest.
        * Painting, if the room is lit and the player is facing a painting.
        * Seller, if the room is lit and the player is facing a Seller.
        * You See a silhouette of you if the room is lit and the player is facing a mirror.
    * Check: this command takes these arguments:
        * Mirror: will work only if facing a mirror, if a key is hidden behind the mirror will acquire the named
          key “The <name> key was acquired”.
        * Painting: will work only if facing a painting, if a key is hidden behind the painting will acquire the
          named key “The <name> key was acquired”.
        * Chest: will work only if facing a chest, if the chest is closed “chest closed <name> key is needed
          to unlock”, if it is open the items inside the chest are listed and looted/acquired and could contain
          (named key, flashlight, or N gold where N is either a random number or a specific number set by
          the map).
        * Door: will work only if facing a door, if the door is locked “Door is locked, <name> key is needed
          to unlock” and if it is open “Door is open”.
    * Open, will open a door if the player is facing a doo, and it is unlocked and if the door is open “nothing
      happens” or if locked “<named> key required to unlock”
    * Trade, will work only if the player is facing a seller, will list the seller’s available items (including possibly
      a flashlight and specifically named keys with prices in gold, the items of the seller are specified by the
      map). The subcommands of Trade are:
        * Buy <item>: if enough gold is with the player will “<item> bought and acquired” otherwise, the
          seller will say “return when you have enough gold”
        * Sell <Item>: The seller will have a price list of any item type that can be on the map and will offer
          that amount for any item that you have. You can buy back items at the same price.
        * List: will list seller items again.
        * Finish Trade, will exit the trade mode.
    * Use flashlight: will turn it on if it is off and vice versa and a dark room will become lit if the flashlight is
      on.
    * Use <name> Key: will open if a door/chest requires the <name> key to open, and it is locked and will lock
      the door/chest if it is open.
    * SwitchLights, if the room has lights it will turn them on if they are off and vice versa, if the lights are on
      the room should not be dark and if they are off it must be dark (Note not all rooms have lights and if they
      do not have lights they do require a flashlight to illuminate (for the look command to work)).
    * quit: will exit the game and lose.
    * Restart: will reset the game to the initial state for the player to restart over the game, even in the middle
      of the game.
* If the user exits a special door representing the Maze exit point, they win.
* If the timer of the map (time given is a property of the given map) elapses without exiting the maze it is a loss.

Note: how maps are specified:

1. Maps can be preconfigured manually using a maps file with a certain format (a tool could be used to generate
   the files as well)
2. The rooms can be generated on the fly as the player navigates (but if the game is restarted in that case the
   already generated rooms should remain).

Include a README containing details of how to run the game.


--------