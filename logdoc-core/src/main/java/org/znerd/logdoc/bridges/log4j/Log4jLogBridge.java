// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.bridges.log4j;

import java.util.Map;

import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.logdoc.internal.AbstractTranslationBundle;
import org.znerd.util.log.LogLevel;

public final class Log4jLogBridge extends LogBridge {
    private static final Log4jLogBridge SINGLETON_INSTANCE = new Log4jLogBridge();

    private Log4jLogBridge() {
    }
    
    public static Log4jLogBridge getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public void log(String domain, String groupId, String entryId, LogLevel level, AbstractTranslationBundle translationBundle, Throwable exception, Map<String,Object> parameters) {
        // FIXME
    }
}
