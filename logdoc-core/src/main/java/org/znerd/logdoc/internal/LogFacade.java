// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.util.log.LogLevel;

public final class LogFacade {
    private LogFacade() {
    }

    public static boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        return Library.getLogBridge().shouldLog(domain, groupId, entryId, level);
    }

    public static void log(String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        LogBridge logBridge = Library.getLogBridge();
        logBridge.log(domain, groupId, entryId, level, message, exception);
    }
}
