Web-Based Multiplayer Maze Game
==============================

A multiplayer application that implements a maze game on the web with persistent properties.

Assignment Requirements
------------

You are required to build a fully functioning web-based multiplayer World Navigator Game that satisfy the below
requirement.

**Game Requirements:**

* Game rules are the same as before, except that more than one player can play the game
* The game map must have at least 50 rooms, with at least five rooms that have the special "win-the-game" door
* No limit is put on the number of players. Also, all players have names that are shown on the screen.
* Initially, all players must be present in different rooms (selected randomly)
* To ensure fairness, initially, all players must have the same amount of gold (i.e., 20 gold coins). Furthermore, let
  all
  keys in the game worth the same amount (i.e., 10 gold coins) and flashlights are worth 2 gold coins
* After the game starts, no new players are allowed to join the game (i.e., all players must start the game at the same
  time)
* Players play the game simultaneously
* During the game, If two players happen to meet, they must fight and the loser immediately exits the game
* The fighting rules are simple: the player with the more gold wins (substitute 10 gold coins for each key and 2 gold
  coins for each flashlight). In a case of a tie, the two players play rock-paper-scissor to choose a winner.
  Furthermore,
  the winner player obtains all the items of the loser player. However, the gold of the loser is distributed evenly
  between all players.
* The game ends when a player exits a special door, i.e., the game has only one winner.
* A player may give up and exit the game at anytime. In such a case, the player's gold is distributed evenly between all
  players. All items will be put in a chest (or a painting or a mirror) in the room, where the player gave up the game.
  If
  there are no such objects in the room, find another room with an available object and put the items inside it. Another
  optional feature is to introduce "drop on the floor" feature, in which a player who wants to give up throw all of
  his/her items on the floor. Afterwards, another player who happened to walk in into this room will automatically
  obtain
  all the items on the floor.
* The game ends automatically after a specific timeout (with no winner).

**Website requirements:**

* The website must have a convenient GUI that allows the players to log in and play the game
* Other details of the GUI are up to you
* Use AWS to create the game server
* Implement CI/CD pipeline using Jenkins and AWS
* Your website should support high-availability and should be scalable if the load increases on the server
* More than one game can be active at a time
* Suggestion: consider using the MVC model

**Submission Guidelines:**

Submit a compressed file (your name.zip) that contains the following:

* Your source code (i.e., all of your java classes)
* A written report (in pdf format) that describes your source code - the report should cover the following points:
    1. How your code satisfies the clean code principles according to Robert Martin’s “Clean Code” book.
    2. How your code satisfies the effective Java code principles according to Joshua Bloch’s “Effective Java” book.
    3. Discuss the design patterns you used to implement your solution and why.
    4. How your code satisfies the SOLID principles? You can refer to Robert Martin’s “Clean Architecture” book, or any
       other reference.
    5. The styling guide you used
    6. Concurrency issues in your code and how you dealt with that
    7. Data structures you used and why.
    8. How you used DevOps to build your project
    9. How you used containers and orchestration in you project

--------