package webapp;

import game.GamesList;
import game.MazeGame;
import game.Player;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

@WebServlet(urlPatterns = "/Lobby")
public class LobbyServlet extends HttpServlet {

    private static final String PLAYER_ID = "playerID";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            String gameID = request.getParameter("gameID");
            String playerID = "";
            String mapChoice = request.getParameter("mapChoice");
            String playerName = request.getParameter("playerName");
            List<String> players = new ArrayList<>();
            boolean filled = request.getParameter(PLAYER_ID) != null;

            if (gameID != null) {

                if (!GamesList.getGamesList().contains(new MazeGame(gameID))) {
                    request.setAttribute("message", "ROOM DOES NOT EXIST OR HAS BEEN ABANDONED");
                    request.getRequestDispatcher("/WEB-INF/views/invalidRequest.jsp").forward(
                            request, response);
                    return;
                }

                for (MazeGame mazeGame : GamesList.getGamesList())
                    if (mazeGame.getId().equals(gameID) && mazeGame.getCapacity() < mazeGame.getPlayers().size()) {
                        request.setAttribute("message", "LOBBY IS ALREADY FULL");
                        request.getRequestDispatcher("/WEB-INF/views/invalidRequest.jsp").forward(
                                request, response);
                        return;
                    } else if (mazeGame.getId().equals(gameID) && !mazeGame.isStarted()) {
                        if (request.getParameter(PLAYER_ID) == null) {
                            Player player = new Player(playerName, mazeGame);
                            playerID = player.getId();
                            mazeGame.getPlayers().put(player, false);
                            mapChoice = mazeGame.getMap();
                        } else playerID = request.getParameter(PLAYER_ID);
                        for (Player playerInstance : mazeGame.getPlayers().keySet())
                            players.add(playerInstance.getName());
                        break;
                    } else if (mazeGame.getId().equals(gameID) && mazeGame.isStarted()) {
                        request.getRequestDispatcher("GameField").forward(
                                request, response);
                        return;
                    }
            }

            StringBuilder playersText = new StringBuilder();
            for (String string : players)
                playersText.append("<p>").append(string).append("</p>");

            request.setAttribute("gameID", gameID);
            request.setAttribute("mapChoice", mapChoice);
            request.setAttribute(PLAYER_ID, playerID);
            request.setAttribute("playerName", playerName);
            request.setAttribute("players", playersText);

            if (!filled) {
                response.sendRedirect(request.getServletPath()
                        + "?gameID=" + gameID + "&"
                        + "mapChoice=" + mapChoice + "&"
                        + "playerID=" + playerID + "&"
                        + "playerName=" + playerName);
            } else request.getRequestDispatcher("/WEB-INF/views/lobby.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/lobby.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}