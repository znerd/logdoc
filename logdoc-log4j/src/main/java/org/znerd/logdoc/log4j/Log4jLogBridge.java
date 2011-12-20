// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.znerd.logdoc.AbstractLogBridge;
import org.znerd.util.log.LogLevel;

public final class Log4jLogBridge extends AbstractLogBridge {

    private static final Log4jLogBridge SINGLETON_INSTANCE = new Log4jLogBridge();
    private static final Level LOG4J_DEBUG_LEVEL = Level.DEBUG;
    private static final Level LOG4J_INFO_LEVEL = Level.INFO;
    private static final Level LOG4J_NOTICE_LEVEL = new CustomLog4jLevel((Level.INFO_INT + Level.WARN_INT) / 2, "NOTICE", 5);
    private static final Level LOG4J_WARNING_LEVEL = Level.WARN;
    private static final Level LOG4J_ERROR_LEVEL = Level.ERROR;
    private static final Level LOG4J_FATAL_LEVEL = Level.FATAL;

    private Log4jLogBridge() {
    }

    public static Log4jLogBridge getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public void putContextId(String newContextId) {
        NDC.push(newContextId);
    }

    @Override
    public void unputContextId() {
        NDC.pop();
    }

    @Override
    public String getContextId() {
        String contextId = NDC.peek();
        if (contextId.length() < 1) {
            return null;
        } else {
            return contextId;
        }
    }

    @Override
    public boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        if (! getLevel().isSmallerThanOrEqualTo(level)) {
            return false;
        }

        Logger logger = getLogger(domain, groupId, entryId);
        Level log4jLevel = toLog4jLevel(level);
        return logger.isEnabledFor(log4jLevel);
    }

    private Level toLog4jLevel(LogLevel level) {
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
    public void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        Logger logger = getLogger(domain, groupId, entryId);
        Level log4jLevel = toLog4jLevel(level);
        logger.log(fqcn, log4jLevel, message, exception);
    }
}
