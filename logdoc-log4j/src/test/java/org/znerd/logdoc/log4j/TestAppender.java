// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.log4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class TestAppender extends AppenderSkeleton {
    
    private List<String> messages = new ArrayList<String>();

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(LoggingEvent event) {
        messages.add(event.getRenderedMessage());
    }
    
    @Override
    public void close() {
    }
    
    public List<String> getMessages() {
        return new ArrayList<String>(messages);
    }
}
