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

@WebServlet(urlPatterns = "/LobbyHost")
public class LobbyHostServlet extends HttpServlet {

    private static final String GAME_ID = "gameID";
    private static final String PLAYER_ID = "playerID";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            String gameID;
            String mapChoice = request.getParameter("mapChoice");
            String playerID = "";
            String playerName = request.getParameter("playerName");
            List<String> players = new ArrayList<>();
            boolean filled = request.getParameter(GAME_ID) != null;

            if (request.getParameter(GAME_ID) == null)
                gameID = GamesList.getGamesList().get(GamesList.hostGame()).getId();
            else
                gameID = request.getParameter(GAME_ID);

            if (gameID != null)
                for (MazeGame mazeGame : GamesList.getGamesList())
                    if (mazeGame.getId().equals(gameID) && !mazeGame.isStarted()) {

                        if (request.getParameter(GAME_ID) == null && !mazeGame.runGame(mapChoice)) {
                            GamesList.getGamesList().remove(mazeGame);
                            for (Player player : mazeGame.getPlayers().keySet())
                                player.setMazeGame(null);
                            request.setAttribute("message", "THE MAP IS CORRUPT");
                            request.getRequestDispatcher("/WEB-INF/views/invalidRequest.jsp").forward(
                                    request, response);
                            return;
                        }

                        if (request.getParameter(PLAYER_ID) == null) {
                            Player player = new Player(playerName, mazeGame);
                            playerID = player.getId();
                            mazeGame.getPlayers().put(player, false);
                        } else playerID = request.getParameter(PLAYER_ID);
                        for (Player playerInstance : mazeGame.getPlayers().keySet())
                            players.add(playerInstance.getName());
                    }

            StringBuilder playersText = new StringBuilder();
            for (String string : players)
                playersText.append("<p>").append(string).append("</p>");


            request.setAttribute(GAME_ID, gameID);
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
            } else request.getRequestDispatcher("/WEB-INF/views/lobbyHost.jsp").forward(
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
            request.getRequestDispatcher("/WEB-INF/views/lobbyHost.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}