//mvn tomcat7:run
package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static game.IoHandler.printlnSys;
import static game.IoHandler.stackTraceToString;

@WebServlet(urlPatterns = "/MainMenu")
public class MainMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/mainMenu.jsp").forward(
                    request, response);
        } catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/views/mainMenu.jsp").forward(
                    request, response);
        }  catch (IOException | ServletException e){
            e.printStackTrace();
            printlnSys(stackTraceToString(e));
        }
    }
}