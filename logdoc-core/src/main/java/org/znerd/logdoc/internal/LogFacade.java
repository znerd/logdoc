// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.util.log.LogLevel;

public final class LogFacade {

    // TODO: org.znerd.logdoc.Library.isStackTraceAtMessageLevel() ? org.znerd.util.ExceptionUtils.getRootCause(_exception) : null);

    private LogFacade() {
    }

    public static boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        return Library.getLogBridge().shouldLog(domain, groupId, entryId, level);
    }

    public static void log(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        LogBridge logBridge = Library.getLogBridge();
        logBridge.log(fqcn, domain, groupId, entryId, level, message, exception);
    }
}
