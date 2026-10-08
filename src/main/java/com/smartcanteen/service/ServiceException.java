package com.smartcanteen.service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EXCEPTION HANDLING: thrown by the service classes when something goes wrong.
 * The message is always safe to show to the user (no technical details, no stack trace).
 */
public class ServiceException extends Exception {

    public static final String DATABASE_ERROR_MESSAGE = "Something went wrong. Please try again.";

    private static final Logger LOGGER = Logger.getLogger(ServiceException.class.getName());

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Used when a database (SQL) error happens: the technical details go to the server console,
     * the user only sees a short friendly message.
     */
    public static ServiceException fromDatabaseError(Exception cause) {
        LOGGER.log(Level.SEVERE, "Database error", cause);
        return new ServiceException(DATABASE_ERROR_MESSAGE, cause);
    }
}
