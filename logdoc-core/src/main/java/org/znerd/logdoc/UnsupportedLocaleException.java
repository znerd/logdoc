// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.util.Preconditions;

/**
 * Exception thrown if a specified locale is not supported by at least one <em>logdoc</em> <code>Log</code> class.
 */
public final class UnsupportedLocaleException extends RuntimeException {

    /**
     * Constructs a new <code>UnsupportedLocaleException</code>.
     * 
     * @param locale the locale, cannot be <code>null</code>.
     */
    public UnsupportedLocaleException(String locale) {
        super("Locale \"" + locale + "\" is not supported.");
        Preconditions.checkArgument(locale == null, "locale == null");
        _locale = locale;
    }

    private final String _locale;

    /**
     * Retrieves the unsupported locale.
     * 
     * @return the unsupported locale, never <code>null</code>.
     */
    public String getLocale() {
        return _locale;
    }

    private static final long serialVersionUID = -991987123777189023L;
}
