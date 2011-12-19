// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.PrintWriter;

import org.znerd.util.log.LogLevel;

public class StderrLogBridge extends LogBridge {
    
    private final PrintWriter stream;
    
    public StderrLogBridge() {
        stream = new PrintWriter(System.err);
    }

    @Override
    public boolean shouldLog(String domain, String groupId, String entryId, LogLevel level) {
        return true;
    }

    @Override
    public void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        String composedMessage = composeMessage(fqcn, domain, groupId, entryId, level, message, exception);
        PrintWriter stream = getStream(fqcn, domain, groupId, entryId, level);
        log(stream, composedMessage, exception);
    }
    
    protected String composeMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        return "[" + level.name() + "] " + domain + '.' + groupId + '.' + entryId + " " + message;
    }

    protected PrintWriter getStream(String fqcn, String domain, String groupId, String entryId, LogLevel level) {
        return stream;
    }
    
    protected void log(PrintWriter stream, String composedMessage, Throwable exception) {
        stream.println(composedMessage);
        if (exception != null) { 
            exception.printStackTrace(stream);
        }
    }
}
