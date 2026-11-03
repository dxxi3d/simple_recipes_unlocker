package dxxi3d.simple_recipes_unlocker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SharedConstants.MOD_NAME);

    private ModLogger() {
    }

    public static void sendInitialized() {
        LOGGER.info("Mod initialized!");
    }

    public static void info(String message, Object... arguments) {
        LOGGER.info(message, arguments);
    }

    public static void debug(String message, Object... arguments) {
        LOGGER.debug(message, arguments);
    }

    public static void warn(String message, Object... arguments) {
        LOGGER.warn(message, arguments);
    }

    public static void error(String message, Object... arguments) {
        LOGGER.error(message, arguments);
    }
}
