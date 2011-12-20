// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.util.log.LogLevel;

/**
 * Implementation of a <code>LogBridge</code> that throws an <code>UnsupportedOperationException</code> from each method.
 */
public class UnsopLogBridge implements LogBridge {
    @Override
    public void putContextId(String newContextId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unputContextId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLevel(LogLevel logLevel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LogLevel getLevel() {
        throw new UnsupportedOperationException();
    }
}
