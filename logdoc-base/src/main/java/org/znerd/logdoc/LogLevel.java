// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Enumeration type for log levels.
 */
public enum LogLevel {

    /**
     * Level for debugging messages, useful for for developers only. This is the only level that may contain implementation details that are not exposed outside individual functions.
     */
    DEBUG,

    /**
     * Level for informational messages. Typically not important to operational managers, except in cases where a problem is being traced or if behavior is being investigated.
     */
    INFO,

    /**
     * Level for informational messages that should typically be noticed by operational managers.
     */
    NOTICE,

    /**
     * Level for warning messages. Should be noticed, but typically require no immediate action based on this individual message only, although this kind of messages can indicate a problem that should
     * be fixed.
     */
    WARNING,

    /**
     * Level for error messages. Indicates an error that should be fixed. However, it does not keep the whole application from functioning.
     */
    ERROR,

    /**
     * Level for fatal error messages. Indicates an error that keeps the whole application from functioning.
     */
    FATAL;
}
