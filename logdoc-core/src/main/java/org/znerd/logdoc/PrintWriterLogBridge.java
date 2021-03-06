// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.PrintWriter;

import org.znerd.logdoc.internal.ContextIdSupport;
import org.znerd.util.log.LogLevel;

public class PrintWriterLogBridge extends AbstractLogBridge {
    private final PrintWriter stream;
    private final ContextIdSupport contextIdSupport = new ContextIdSupport();

    public PrintWriterLogBridge(PrintWriter stream) {
        this.stream = stream;
    }

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
        switch (getLevel()) {
            case DEBUG:
                return true;
            case INFO:
                return level != LogLevel.DEBUG;
            case NOTICE:
                return level != LogLevel.DEBUG && level != LogLevel.INFO;
            case WARNING:
                return level != LogLevel.DEBUG && level != LogLevel.INFO && level != LogLevel.NOTICE;
            case ERROR:
                return level == LogLevel.ERROR || level == LogLevel.FATAL;
            default:
                return level == LogLevel.FATAL;
        }
    }

    @Override
    public void logOneMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        String composedMessage = composeMessage(fqcn, domain, groupId, entryId, level, message, exception);
        PrintWriter stream = getStream(fqcn, domain, groupId, entryId, level);
        log(stream, composedMessage, exception);
    }

    protected String composeMessage(String fqcn, String domain, String groupId, String entryId, LogLevel level, String message, Throwable exception) {
        String composedMessage = level.name() + " [";
        String contextId = getContextId();
        if (contextId != null) {
            composedMessage += contextId;
        }
        composedMessage += "] " + domain + '.' + groupId + '.' + entryId + " " + message;
        return composedMessage;
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
