package webapp;

import game.GamesList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

@WebServlet(urlPatterns = "/Host")
public class HostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<String> list = GamesList.getMaps();
            StringBuilder mapsText = new StringBuilder();
            for (String string : list)
                mapsText.append("<button id=").append(string).append(" type=button onclick=showSelected('").append(string).append("')>").append(string).append("</button>");


            request.setAttribute("maps", mapsText);
            request.setAttribute("playerName", request.getParameter("playerName"));
            request.getRequestDispatcher("/WEB-INF/views/host.jsp").forward(
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
            request.getRequestDispatcher("/WEB-INF/views/host.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}