// See the COPYRIGHT file for copyright and license information
package org.znerd.util.log;

/**
 * Simple logging interface.
 * <p>
 * When <code>null</code> is passed as a {@link LogLevel}, then {@link LogLevel#DEBUG} is assumed.
 * <p>
 * When <code>null</code> is passed as a message, then an empty string is assumed.
 */
public class Limb {

    public static synchronized void log(LogLevel level, String message) {
        log(level, message, (Throwable) null);
    }

    public static synchronized void log(LogLevel level, String message, Throwable exception) {
        if (level == null) {
            level = LogLevel.DEBUG;
        }
        if (message == null) {
            message = "";
        }

        INSTANCE.logImpl(level, message, exception);
    }

    private static Limb INSTANCE = new Limb();

    public static synchronized void setLogger(Limb logger) throws IllegalArgumentException {
        if (logger == null) {
            throw new IllegalArgumentException("logger == null");
        }
        INSTANCE = logger;
    }

    protected void logImpl(LogLevel level, String message, Throwable exception) {
        fallbackLogImpl(level, message, exception);
    }

    private void fallbackLogImpl(LogLevel level, String message, Throwable exception) {
        System.err.println(level.name() + ' ' + message);
        if (exception != null) {
            exception.printStackTrace();
        }
    }

    protected Limb() {
    }
}
