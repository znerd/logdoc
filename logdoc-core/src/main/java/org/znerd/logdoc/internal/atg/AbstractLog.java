package org.znerd.logdoc.internal.atg;

import org.znerd.util.log.LogLevel;

import atg.nucleus.logging.ApplicationLogging;

/**
 * Abstract base class for <code>Log</code> classes, specific to the ATG logging framework.
 */
public abstract class AbstractLog {

    protected AbstractLog() {
    }

    protected static void log(ApplicationLogging logger, String category, String id, LogLevel level, String message, Throwable exception) {
        String resultMessage = produceResultMessage(id, level, message);
        if (isLevelEnabled(logger, level)) {
            log(logger, level, resultMessage, exception);
        }
    }

    private static String produceResultMessage(String id, LogLevel level, String message) {
        if (level == LogLevel.NOTICE) {
            message = "LOG4J_NOTICE_LEVEL: " + message;
        } else if (level == LogLevel.FATAL) {
            message = "LOG4J_FATAL_LEVEL: " + message;
        }

        message = id + " " + message;
        return message;
    }

    private static boolean isLevelEnabled(ApplicationLogging logger, LogLevel level) {
        switch (level) {
            case DEBUG:
                return logger.isLoggingDebug();
            case INFO:
            case NOTICE:
                return logger.isLoggingInfo();
            default:
                return logger.isLoggingError();
        }
    }

    private static void log(ApplicationLogging logger, LogLevel level, String message, Throwable exception) {
        if (exception == null) {
            logWithoutException(logger, level, message);
        } else {
            logWithException(logger, level, message, exception);
        }
    }

    private static void logWithoutException(ApplicationLogging logger, LogLevel level, String message) {
        switch (level) {
            case DEBUG:
                logger.logDebug(message);
                break;
            case INFO:
            case NOTICE:
                logger.logInfo(message);
                break;
            default:
                logger.logError(message);
        }
    }
    
    private static void logWithException(ApplicationLogging logger, LogLevel level, String message, Throwable exception) {
        switch (level) {
            case DEBUG:
                logger.logDebug(message, exception);
                break;
            case INFO:
            case NOTICE:
                logger.logInfo(message, exception);
                break;
            default:
                logger.logError(message, exception);
        }
    }
}
