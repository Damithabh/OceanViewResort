package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Centralized logging utility replacing raw System.out.println calls.
 * Uses java.util.logging for structured, level-aware log output.
 * 
 * Why: Production code should never use System.out for diagnostics.
 * A dedicated logger enables configurable log levels, formatting, and
 * future integration with file-based or remote log handlers.
 * 
 * // Learned from
 * https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html
 * 
 * @author Ocean View Resort Dev Team
 */
public class AppLogger {

    private static final Logger ROOT_LOGGER;

    static {
        // Configure the root logger once at class load time
        ROOT_LOGGER = Logger.getLogger("com.oceanview");
        ROOT_LOGGER.setLevel(Level.ALL);

        // Remove default handlers to prevent duplicate output
        ROOT_LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        ROOT_LOGGER.addHandler(handler);
    }

    /**
     * Returns a logger instance scoped to the given class.
     *
     * @param clazz The class requesting a logger
     * @return A configured Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setParent(ROOT_LOGGER);
        return logger;
    }
}
