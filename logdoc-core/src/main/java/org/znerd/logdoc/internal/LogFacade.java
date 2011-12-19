// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.LogBridge;
import org.znerd.util.log.LogLevel;

public final class LogFacade {

    private LogFacade() {
    }

    public static void putContextId(String newContextId) {
        Library.getLogBridge().putContextId(newContextId);
    }

    public static void unputContextId() {
        Library.getLogBridge().unputContextId();
    }

    public static String getContextId() {
        return Library.getLogBridge().getContextId();
    }

    public static boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        return Library.getLogBridge().shouldLog(domain, groupId, entryId, level);
    }

    public static void log(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message) {
        Throwable exception = null;
        log(fqcn, domain, groupId, entryId, level, message, exception);
    }

    public static void log(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        LogBridge logBridge = Library.getLogBridge();
        if (!(LogLevel.DEBUG.equals(level) || Library.isStackTraceAtMessageLevel() || exception == null)) {
            logBridge.logOneMessage(fqcn, domain, groupId, entryId, level, message, null);
            logBridge.logOneMessage(fqcn, domain, groupId, entryId, LogLevel.DEBUG, message, exception);
        } else {
            logBridge.logOneMessage(fqcn, domain, groupId, entryId, level, message, exception);
        }
    }
}
