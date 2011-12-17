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
        NOTICE = new CustomLevel(noticeInt, "LOG4J_NOTICE_LEVEL", 5);
        WARNING = Level.WARN;
        ERROR = Level.ERROR;
        FATAL = Level.FATAL;
    }

    public static final Level DEBUG, INFO, NOTICE, WARNING, ERROR, FATAL;
    
    protected AbstractLog() {
    }
}
