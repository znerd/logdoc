// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.LogFramework;
import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.util.log.LogLevel;

public final class LogFacade {
    private LogFacade() {
    }

    public static void log(String domain, String groupId, String entryId, LogLevel level, AbstractTranslationBundle translationBundle, Throwable exception) {
        LogBridge logBridge = Library.getLogBridge();
        logBridge.log(domain, groupId, entryId, level, translationBundle, exception);
    }
}
