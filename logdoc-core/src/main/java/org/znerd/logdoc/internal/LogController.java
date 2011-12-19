// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.UnsupportedLocaleException;

/**
 * Log controller. Can be used by the <code>LogCentral</code> class to set the locale on a specific <code>Log</code> class.
 * <p>
 * Each <code>Log</code> class should create exactly one <code>LogController</code> object, in a class initializer.
 */
public abstract class LogController {

    /**
     * Constructs a new <code>LogController</code> object.
     * 
     * @throws UnsupportedLocaleException
     *             if this <code>LogController</code> does not support the current Locale.
     */
    protected LogController() throws UnsupportedLocaleException {

        // Register this Log with the LogCentral, so that
        // LogCentral.setLocale(String) may call setLocale(String) on this
        // instance
        LogCentral.registerLog(this);
    }

    /**
     * Checks if the specified locale is supported.
     * 
     * @param locale
     *            the locale, not <code>null</code>.
     * @return <code>true</code> if the locale is supported, <code>false</code> if it is not.
     */
    public abstract boolean isLocaleSupported(String locale);

    /**
     * Activates the specified locale.
     * <p>
     * This method should only be called with locales that are supported, according to {@link #isLocaleSupported(String)}. Otherwise the behaviour of this method is unspecified.
     * 
     * @param newLocale
     *            the new locale, not <code>null</code>.
     */
    public abstract void setLocale(String newLocale);
}
