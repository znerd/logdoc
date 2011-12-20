// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.util.log.LogLevel;

public interface LogBridge {
    void putContextId(String newContextId);

    void unputContextId();

    String getContextId();

    boolean shouldLog(String domain, String groupId, String entryId, LogLevel level);

    void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception);

    void setLevel(LogLevel logLevel);
    
    LogLevel getLevel();
}
