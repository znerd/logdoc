// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.bridges;

import org.znerd.util.log.LogLevel;

public abstract class LogBridge {
    public abstract boolean shouldLog(String domain, String groupId, String entryId, LogLevel level);

    public abstract void log(String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception);
}
