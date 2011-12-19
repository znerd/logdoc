// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.znerd.logdoc.internal.ContextIdSupport;
import org.znerd.util.log.LogLevel;

public class JulLogBridge extends LogBridge {

    private final ContextIdSupport contextIdSupport = new ContextIdSupport();

    @Override
    public void putContextId(String newContextId) {
        contextIdSupport.putContextId(newContextId);
    }

    @Override
    public void unputContextId() {
        contextIdSupport.unputContextId();
    }

    @Override
    public String getContextId() {
        return contextIdSupport.getContextId();
    }

    @Override
    public boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        Logger logger = getLogger(domain, groupId, entryId);
        Level julLevel = toJulLevel(level);
        return logger.isLoggable(julLevel);
    }

    private Logger getLogger(String domain, String groupId, String entryId) {
        return Logger.getLogger(domain + '.' + groupId + '.' + entryId);
    }

    private Level toJulLevel(LogLevel level) {
        if (LogLevel.DEBUG.equals(level)) {
            return Level.FINE;
        } else if (LogLevel.INFO.equals(level)) {
            return Level.INFO;
        } else if (LogLevel.NOTICE.equals(level)) {
            return Level.INFO;
        } else if (LogLevel.WARNING.equals(level)) {
            return Level.WARNING;
        } else {
            return Level.SEVERE;
        }
    }

    @Override
    public void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        final Logger logger = getLogger(domain, groupId, entryId);
        final Level julLevel = toJulLevel(level);
        final String sourceClass = fqcn;
        final String sourceMethod = null;
        logger.logp(julLevel, sourceClass, sourceMethod, message, exception);
    }
}
