package webapp;

import game.GamesList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

@WebServlet(urlPatterns = "/Join")
public class JoinServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            Map<String, String> list = GamesList.joinGame();
            StringBuilder gamesText = new StringBuilder();
            for (Map.Entry<String, String> entry : list.entrySet())
                gamesText.append("<input id='").append(entry.getKey()).append("' type='submit' value='").append(entry.getValue()).append("' onclick=join('").append(entry.getKey()).append("')>");
            request.setAttribute("games", gamesText);
            request.setAttribute("playerName", request.getParameter("playerName"));
            request.getRequestDispatcher("/WEB-INF/views/join.jsp").forward(
                    request, response);
        }  catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/join.jsp").forward(
                    request, response);
        }  catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}