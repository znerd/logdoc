// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal.log4j;

import org.apache.log4j.Level;

/**
 * Abstract base class for <em>logdoc</em> <code>Log</code> classes, specific to Log4J.
 */
public abstract class AbstractLog {

    static {
        int noticeInt = (Level.INFO_INT + Level.WARN_INT) / 2;

        DEBUG = Level.DEBUG;
        INFO = Level.INFO;
        NOTICE = new CustomLevel(noticeInt, "NOTICE", 5);
        WARNING = Level.WARN;
        ERROR = Level.ERROR;
        FATAL = Level.FATAL;
    }

    public static final Level DEBUG, INFO, NOTICE, WARNING, ERROR, FATAL;

    protected AbstractLog() {
    }

    private static final class CustomLevel extends Level {

        private static final long serialVersionUID = 1909887126346631322L;

        private CustomLevel(int value, String name, int syslogEquivalent) {
            super(value, name, syslogEquivalent);
        }
    }
}
