// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.bridges.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.logdoc.internal.log4j.CustomLevel;
import org.znerd.util.log.LogLevel;

public final class Log4jLogBridge extends LogBridge {

    private static final Log4jLogBridge SINGLETON_INSTANCE = new Log4jLogBridge();
    private static final Level LOG4J_DEBUG_LEVEL = Level.DEBUG;
    private static final Level LOG4J_INFO_LEVEL = Level.INFO;
    private static final Level LOG4J_NOTICE_LEVEL = new CustomLevel((Level.INFO_INT + Level.WARN_INT) / 2, "NOTICE", 5);
    private static final Level LOG4J_WARNING_LEVEL = Level.WARN;
    private static final Level LOG4J_ERROR_LEVEL = Level.ERROR;
    private static final Level LOG4J_FATAL_LEVEL = Level.FATAL;

    private Log4jLogBridge() {
    }

    public static Log4jLogBridge getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        Logger logger = getLogger(domain, groupId, entryId);
        Priority log4jPriority = toLog4jPriority(level);
        return logger.isEnabledFor(log4jPriority);
    }

    private Priority toLog4jPriority(LogLevel level) {
        if (LogLevel.DEBUG.equals(level)) {
            return LOG4J_DEBUG_LEVEL;
        } else if (LogLevel.INFO.equals(level)) {
            return LOG4J_INFO_LEVEL;
        } else if (LogLevel.NOTICE.equals(level)) {
            return LOG4J_NOTICE_LEVEL;
        } else if (LogLevel.WARNING.equals(level)) {
            return LOG4J_WARNING_LEVEL;
        } else if (LogLevel.ERROR.equals(level)) {
            return LOG4J_ERROR_LEVEL;
        } else {
            return LOG4J_FATAL_LEVEL;
        }
    }

    private Logger getLogger(String domain, String groupId, String entryId) {
        String categoryId = domain + '.' + groupId + '.' + entryId;
        return Logger.getLogger(categoryId);
    }

    @Override
    public void log(String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        Logger logger = getLogger(domain, groupId, entryId);
        Priority log4jPriority = toLog4jPriority(level);
        log(logger, log4jPriority, message, exception);
    }

    private void log(Logger logger, Priority priority, String message, Throwable exception) {
        if (exception == null) {
            logger.log(priority, message);
        } else {
            logger.log(priority, message, exception);
        }
    }
}
