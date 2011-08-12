// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins.internal;

import org.apache.maven.plugin.logging.Log;
import org.znerd.util.log.Limb;
import org.znerd.util.log.LogLevel;

public class MavenLimb extends Limb {
    public MavenLimb(Log log) {
        _log = log;
    }

    private Log _log;

    protected void logImpl(LogLevel level, String message, Throwable exception) {
        switch (level) {
            case DEBUG:
                _log.debug(message, exception);
                break;
            case INFO:
                _log.debug("INFO: " + message, exception);
                break;
            case NOTICE:
                _log.info(message, exception);
                break;
            case WARNING:
                _log.warn(message, exception);
                break;
            case ERROR:
                _log.error(message, exception);
                break;
            default:
                _log.error("FATAL: " + message, exception);
        }
    }
}
