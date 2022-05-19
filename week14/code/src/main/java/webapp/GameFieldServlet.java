package webapp;

import game.Controls;
import game.GamesList;
import game.MazeGame;
import game.Player;
import game.containers.non_lootables.Shop;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

@WebServlet(urlPatterns = "/GameField")
public class GameFieldServlet extends HttpServlet {

    private static final String MESSAGE = "message";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            if (GamesList.getGamesList().isEmpty()) {
                request.setAttribute(MESSAGE, "INVALID REQUEST");
                request.getRequestDispatcher("/WEB-INF/views/invalidRequest.jsp").forward(
                        request, response);
                return;
            }

            String gameId = request.getParameter("gameID");
            String mapChoice = request.getParameter("mapChoice");
            String playerID = request.getParameter("playerID");
            String playerName = request.getParameter("playerName");
            String command = request.getParameter("command");
            String choice = request.getParameter("choice");
            Player player;
            String message;
            String flashlight = "";
            List<String> images;
            String east;
            String north;
            String west;
            String south;
            String enemy = "";
            String tiebreakerSidebar = "";

            request.setAttribute("gameID", gameId);
            request.setAttribute("mapChoice", mapChoice);
            request.setAttribute("playerID", playerID);
            request.setAttribute("playerName", playerName);

            for (MazeGame mazeGame : GamesList.getGamesList())
                if (mazeGame.getId().equals(gameId)) {

                    if (mazeGame.getPlayers().keySet().stream().noneMatch(u -> u.getId().equals(playerID))) {
                        request.setAttribute(MESSAGE, "INVALID PLAYER ID");
                        request.getRequestDispatcher("/WEB-INF/views/invalidRequest.jsp").forward(
                                request, response);
                        return;
                    }

                    mazeGame.setStarted(true);
                    mazeGame.startGame();
                    player = mazeGame.getPlayers().keySet().stream().filter(u -> u.getId().equals(playerID)).collect(Collectors.toList()).get(0);
                    player.startGame();
                    if (command != null) {
                        if (Player.isContainerAShop(player, command.toUpperCase().charAt(0)) && choice == null) {
                            List<StringBuilder> list = Shop.viewShopItems(mazeGame);
                            StringBuilder shopItemsText = new StringBuilder();
                            int counter = 1;
                            for (StringBuilder string : list) {
                                if (!string.isEmpty())
                                    shopItemsText.append("<button name=choice value=")
                                            .append(counter)
                                            .append(" type=submit>")
                                            .append(string.toString()).append("</button>");
                                counter++;
                            }
                            request.setAttribute("shopItems", shopItemsText);

                        }

                        player.move(command.charAt(0));

                    } else {
                        if (choice != null) {
                            Shop.makeTransaction(mazeGame, player, Integer.parseInt(choice.charAt(0) + ""));
                        } else if (player.isOpponentInRoom() && player.getTieBreakerChoice() == -1) {
                            player.move(Controls.getEngageKey());
                        } else if (player.isOpponentInRoom()) {
                            player.move(Controls.getRandomTieBreakerChoice());
                        }
                    }

                    if (player.getInventory().getGold().getAmount() >= mazeGame.getGoldThresholdObjective()) {
                        player.clearBuffer();
                        player.printStatus(mazeGame);
                        request.setAttribute(MESSAGE, player.getMessageBuffer());
                        request.setAttribute("result", "<h1><font color=\"green\">YOU ARE VICTORIOUS!</font></h1>");
                        request.getRequestDispatcher("/WEB-INF/views/finished.jsp").forward(
                                request, response);
                        return;
                    }

                    if (mazeGame.getRemainingTime() < 0 || Boolean.TRUE.equals(mazeGame.getPlayers().get(player))) {
                        player.clearBuffer();
                        player.printStatus(mazeGame);
                        request.setAttribute(MESSAGE, player.getMessageBuffer());
                        request.setAttribute("result", "<h1><font color=\"red\">YOU HAVE BEEN DEFEATED!</font></h1>\n");
                        request.getRequestDispatcher("/WEB-INF/views/finished.jsp").forward(
                                request, response);
                        return;
                    }

                    if (player.getLocation().isDark())
                        flashlight = "<style> :root {\n" +
                                "  cursor: none;\n" +
                                "  --cursorX: 50vw;\n" +
                                "  --cursorY: 50vh;\n" +
                                "  --z-index: 0;\n" +
                                "}\n" +
                                ":root:before {\n" +
                                "  content: '';\n" +
                                "  display: block;\n" +
                                "  width: 100%;\n" +
                                "  height: 100%;\n" +
                                "  z-index: 5;\n" +
                                "  position: fixed;\n" +
                                "  pointer-events: none;\n" +
                                "  background: radial-gradient(\n" +
                                "    circle 10vmax at var(--cursorX) var(--cursorY),\n" +
                                "    rgba(0,0,0,0) 0%,\n" +
                                "    rgba(0,0,0,.5) 80%,\n" +
                                "    rgba(0,0,0,.95) 100%\n" +
                                "  )\n" +
                                "}</style>" +

                                "<script>function update(e){\n" +
                                "  var x = e.clientX || e.touches[0].clientX\n" +
                                "  var y = e.clientY || e.touches[0].clientY\n" +
                                "\n" +
                                "  document.documentElement.style.setProperty('--cursorX', x + 'px')\n" +
                                "  document.documentElement.style.setProperty('--cursorY', y + 'px')\n" +
                                "}\n" +
                                "\n" +
                                "document.addEventListener('mousemove',update)\n" +
                                "document.addEventListener('touchmove',update)</script>";

                    request.setAttribute("time", mazeGame.getRemainingTime());
                    response.setIntHeader("Refresh", mazeGame.getRemainingTime() + 1);
                    message = player.getMessageBuffer();
                    images = player.drawMap(player.getLocation());

                    for (Player player1 : mazeGame.getPlayers().keySet())
                        if (player.getLocation() == player1.getLocation() && player != player1) {
                            enemy = "<strong style=\"position: absolute; width: 10%; left: 45%; top: 35%; text-align:center;\"><p>" + player1.getName() + "</p></strong>" +
                                    "<button type=\"submit\" class=\"center\" name=\"command\" value=\"k\">" +
                                    "<img class=\"image\" src=\"img/Enemy.png\">" +
                                    "</button>";

                            if (player.getTieBreakerChoice() != -1 || player1.getTieBreakerChoice() != -1) {
                                tiebreakerSidebar = "<div class=\"stickyLeft\" style=\"overflow: auto;\">\n" +
                                        "<div style=\"display:inline-block;\">" +
                                        "<p>Tiebreaker<br>-----------------------</p>" +
                                        "</div>" +

                                        "<button type=\"submit\" class=\"center\" name=\"command\" value=\"1\">" +
                                        "<img src=\"img/Rock.png\" width=\"75\" height=\"75\"></button>" +

                                        "<button type=\"submit\" class=\"center\" name=\"command\" value=\"2\">" +
                                        "<img src=\"img/Paper.png\" width=\"75\" height=\"75\"></button>" +

                                        "<button type=\"submit\" class=\"center\" name=\"command\" value=\"3\">" +
                                        "<img src=\"img/Scissors.png\" width=\"75\" height=\"75\"></button>" +

                                        "</div>";
                            }

                            break;
                        }

                    east = images.get(0);
                    north = images.get(1);
                    west = images.get(2);
                    south = images.get(3);

                    StringBuilder defeatedPlayers = new StringBuilder();
                    for (Map.Entry<Player, Boolean> player1 : mazeGame.getPlayers().entrySet())
                        if (Boolean.TRUE.equals(player1.getValue()))
                            defeatedPlayers.append("<p>").append(player1.getKey().getName()).append("<p>");

                    request.setAttribute("east", east);
                    request.setAttribute("north", north);
                    request.setAttribute("west", west);
                    request.setAttribute("south", south);
                    request.setAttribute("enemy", enemy);
                    request.setAttribute(MESSAGE, message);
                    request.setAttribute("flashlight", flashlight);
                    request.setAttribute("defeatedPlayers", defeatedPlayers.toString());
                    request.setAttribute("tiebreakerSidebar", tiebreakerSidebar);

                    request.getRequestDispatcher("/WEB-INF/views/gameField.jsp").forward(
                            request, response);
                }
        } catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/gameField.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}