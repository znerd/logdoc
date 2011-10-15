// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Exception that indicates that a meta data resource could not be found.
 */
public class NoSuchResourceException extends RuntimeException {

    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    private static final long serialVersionUID = 8394805671837180263L;
}
