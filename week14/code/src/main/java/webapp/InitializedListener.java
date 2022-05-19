package webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static game.IoHandler.printlnSys;
import static game.assets.StringValues.translate;


public class InitializedListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        //Notification that the servlet context is about to be shut down.
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        printlnSys(translate("ESTABLISHED"));
    }
}