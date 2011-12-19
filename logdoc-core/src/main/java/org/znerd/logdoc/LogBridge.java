// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.util.log.LogLevel;

public abstract class LogBridge {
    public abstract void putContextId(String newContextId);

    public abstract void unputContextId();

    public abstract String getContextId();

    public abstract boolean shouldLog(String domain, String groupId, String entryId, LogLevel level);

    public abstract void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception);

}
