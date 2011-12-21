// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestHandler extends Handler {

    private List<String> messages = new ArrayList<String>();

    @Override
    public void publish(LogRecord logRecord) {
        messages.add(logRecord.getMessage());
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }
    
    public String getLastMessage() {
        int messageCount = messages.size();
        if (messageCount < 1) {
            return null;
        }
        int lastElementIndex = messageCount - 1;
        return messages.get(lastElementIndex);
    }
}
