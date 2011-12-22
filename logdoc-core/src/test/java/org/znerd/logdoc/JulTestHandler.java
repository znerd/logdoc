// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JulTestHandler extends Handler {

    private List<LogRecord> logRecords = new ArrayList<LogRecord>();

    @Override
    public void publish(LogRecord logRecord) {
        logRecords.add(clone(logRecord));
    }
    
    private static LogRecord clone(LogRecord logRecord) {
        LogRecord duplicate = new LogRecord(logRecord.getLevel(), logRecord.getMessage());
        duplicate.setLoggerName(logRecord.getLoggerName());
        duplicate.setMillis(logRecord.getMillis());
        duplicate.setParameters(logRecord.getParameters()); // TODO: Review
        duplicate.setResourceBundle(logRecord.getResourceBundle());
        duplicate.setResourceBundleName(logRecord.getResourceBundleName());
        duplicate.setSequenceNumber(logRecord.getSequenceNumber());
        duplicate.setSourceClassName(logRecord.getSourceClassName());
        duplicate.setSourceMethodName(logRecord.getSourceMethodName());
        duplicate.setThreadID(logRecord.getThreadID());
        duplicate.setThrown(logRecord.getThrown());
        return duplicate;
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }

    public LogRecord getLastLogRecord() {
        int recordCount = logRecords.size();
        if (recordCount < 1) {
            return null;
        }
        int lastElementIndex = recordCount - 1;
        return logRecords.get(lastElementIndex);
    }
}
