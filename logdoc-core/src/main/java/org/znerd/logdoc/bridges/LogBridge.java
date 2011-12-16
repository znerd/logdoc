// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.bridges;

import java.util.Map;
import org.znerd.logdoc.internal.AbstractTranslationBundle;
import org.znerd.util.log.LogLevel;

public abstract class LogBridge {
    public abstract void log(String domain, String groupId, String entryId, LogLevel level, AbstractTranslationBundle translationBundle, Throwable exception, Map<String,Object> parameters);
}
