package com.smartcanteen.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * Runs once when Tomcat starts the application and prints the database status in the console.
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppStartupListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (DBConnection.isDatabaseAvailable()) {
            LOGGER.info("Smart Canteen started. Database connection is OK.");
        } else {
            LOGGER.severe("Smart Canteen started, but the database is NOT reachable. "
                    + "Check DB_URL, DB_USERNAME and DB_PASSWORD (see README.md).");
        }
    }
}
