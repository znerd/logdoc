package org.znerd.logdoc.internal.atg;

import org.znerd.util.log.LogLevel;

import atg.nucleus.logging.ApplicationLogging;

/**
 * Abstract base class for <code>Log</code> classes, specific to the ATG logging framework.
 */
public abstract class AbstractLog {

    protected AbstractLog() {
    }

    protected static void log(String category, String id, LogLevel level, String message, Throwable exception) {
        ApplicationLogging logger = null; // TODO

        if (level == LogLevel.NOTICE) {
            message = "NOTICE: " + message;
        } else if (level == LogLevel.FATAL) {
            message = "FATAL: " + message;
        }

        message = id + " " + message;

        if (isLevelEnabled(logger, level)) {
            switch (level) {
                case DEBUG:
                    logger.logDebug(message, exception);
                    break;
                case INFO:
                case NOTICE:
                    logger.logInfo(message, exception);
                    break;
                case ERROR:
                case FATAL:
                    logger.logError(message, exception);
                    break;
                default:
                    // TODO
            }
        }
    }

    private static boolean isLevelEnabled(ApplicationLogging logger, LogLevel level) {
        switch (level) {
            case DEBUG:
                return logger.isLoggingDebug();
            case INFO:
            case NOTICE:
                return logger.isLoggingInfo();
            case ERROR:
            case FATAL:
                return logger.isLoggingError();
            default:
                return true; // TODO
        }
    }
}
