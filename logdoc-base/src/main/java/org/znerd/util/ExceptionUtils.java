// See the COPYRIGHT file for copyright and license information
package org.znerd.util;

/**
 * Utility functions related to exceptions.
 */
public final class ExceptionUtils {

    /**
     * Determines the root cause for the specified exception.
     * 
     * @param exception the exception to determine the root cause for, can be <code>null</code>.
     * @return the root cause exception, can be <code>null</code>.
     */
    public static Throwable getRootCause(Throwable exception) {

        if (exception == null) {
            return null;
        }

        Throwable cause = exception.getCause();
        while (cause != null) {
            exception = cause;
            cause = exception.getCause();
        }

        return exception;
    }

    private ExceptionUtils() {
    }
}
