// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.znerd.logdoc.internal.LogCentral;
import org.znerd.util.log.Limb;
import org.znerd.util.log.LogLevel;

/**
 * Class that represents the Logdoc library.
 * <p>
 * This class can be run as a program. When it is, all it does is print the name of this library and, if known, the version to standard out.
 */
public final class Library {

    private static final String VERSION = Library.class.getPackage().getImplementationVersion();

    private static final String LOG_LOCALE_PROPERTY = "org.znerd.logdoc.locale";

    private static final String LOG_STACK_TRACE_AT_MESSAGE_LEVEL = "org.znerd.logdoc.stackTraceAtMessageLevel";

    private static final String LOG_FILTER_PROPERTY = "org.znerd.logdoc.filterClass";

    /**
     * The default locale used at start-up, if no locale is specified in a system property.
     */
    public static final String DEFAULT_LOCALE = "en_US";

    private static String LOCALE = null;

    private static boolean STACK_TRACE_AT_MESSAGE_LEVEL = false;

    private static LogFilter LOG_FILTER;

    static {
        LOCALE = determineStartupLocale();
        initLogFilter();
    }

    private static String determineStartupLocale() {
        String locale = System.getProperty(LOG_LOCALE_PROPERTY);
        if (locale != null && locale.trim().length() > 0) {
            return locale;
        } else {
            return DEFAULT_LOCALE;
        }
    }

    private static void initLogFilter() {
        String s = System.getProperty(LOG_FILTER_PROPERTY);
        if (s == null || s.trim().length() < 1) {
            setLogFilter(new SimpleLogFilter());
        } else {
            setLogFilterByClass(s);
        }
    }

    /**
     * Sets the locale on all <em>logdoc</em> <code>Log</code> classes.
     * 
     * @param newLocale the new locale, cannot be <code>null</code>.
     * @throws IllegalArgumentException if <code>newLocale == null</code>.
     * @throws UnsupportedLocaleException if the specified locale is not supported by all registered <em>logdoc</em> <code>Log</code> classes.
     */
    public static void setLocale(String newLocale) throws IllegalArgumentException, UnsupportedLocaleException {

        // Check preconditions
        if (newLocale == null) {
            throw new IllegalArgumentException("newLocale == null");
        }

        // Short-circuit if the new locale equals the current one
        if (newLocale.equals(LOCALE)) {
            return;
        }

        // Pass it on - this can fail with an UnsupportedLocaleException
        LogCentral.setLocale(newLocale);

        // Store the new setting
        LOCALE = newLocale;
    }

    /**
     * Sets the locale on all <em>logdoc</em> <code>Log</code> classes to the default locale.
     */
    public static void useDefaultLocale() {
        setLocale(DEFAULT_LOCALE);
    }

    /**
     * Get the locale set in this LogCentral.
     * 
     * @return the locale, e.g. <code>"en_US"</code>; never <code>null</code>.
     */
    public static String getLocale() {
        return LOCALE;
    }

    /**
     * Enables or disables the display of the stack trace at the same level as the message.
     * 
     * @param sameLevel <code>true</code> if the stack trace should be at the same level, <code>false</code> if the stack trace should be at DEBUG level.
     */
    public static void setStackTraceAtMessageLevel(boolean sameLevel) {
        STACK_TRACE_AT_MESSAGE_LEVEL = sameLevel;
    }

    /**
     * Indicates whether the stack trace should be displayed at the same level as the message.
     * 
     * @return <code>true</code> if the stack trace should be at the same level, <code>false</code> if the stack trace should be at DEBUG level.
     */
    public static boolean isStackTraceAtMessageLevel() {
        return STACK_TRACE_AT_MESSAGE_LEVEL;
    }

    /**
     * Set the active <code>LogFilter</code>.
     * 
     * @param logFilter the new {@link LogFilter} to use, cannot be <code>null</code>.
     * @throws IllegalArgumentException if <code>logFilter == null</code>.
     */
    public static synchronized void setLogFilter(LogFilter logFilter) throws IllegalArgumentException {

        // Check preconditions
        if (logFilter == null) {
            throw new IllegalArgumentException("logFilter == null");
        }

        Limb.log(LogLevel.INFO, "Set LogFilter to instance of class " + logFilter.getClass().getName() + '.');

        // Store the filter in this class
        LOG_FILTER = logFilter;
    }

    /**
     * Set the active <code>LogFilter</code> by class name. If the parameter is <code>null</code> then an exception is thrown. Otherwise if an instance cannot be constructed, then an instance of class
     * {@link NullLogFilter} is used instead.
     * 
     * @param className the name of the {@link LogFilter} class to use, cannot be <code>null</code>.
     * @throws IllegalArgumentException if <code>className == null</code>.
     */
    public static void setLogFilterByClass(String className) throws IllegalArgumentException {

        // Check preconditions
        if (className == null) {
            throw new IllegalArgumentException("className == null");
        }

        // Construct an instance
        LogFilter logFilter;
        try {
            logFilter = (LogFilter) Class.forName(className).newInstance();

            // Instance construction failed, eclipse potential issues using a
            // NullLogFilter
        } catch (Throwable cause) {
            Limb.log(LogLevel.ERROR, "Failed to construct LogFilter of class: " + className + ". Using NullLogFilter.", cause);
            logFilter = new NullLogFilter();
        }

        // Delegate to synchronized setter
        setLogFilter(logFilter);
    }

    /**
     * Returns the active <code>LogFilter</code>.
     * 
     * @return the current {@link LogFilter}, never <code>null</code>.
     */
    public static synchronized LogFilter getLogFilter() {
        return LOG_FILTER;
    }

    /**
     * Retrieves a meta resource and returns it as a <code>URL</code>.
     * 
     * @param path the path to the meta resource, cannot be <code>null</code>.
     * @return the resource as a {@link URL}, never <code>null</code>.
     * @throws IllegalArgumentException if <code>path == null</code>.
     * @throws NoSuchResourceException if the resource could not be found.
     */
    static URL getMetaResource(String path) throws IllegalArgumentException, NoSuchResourceException {

        // Check preconditions
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }

        // Load the resource
        String absPath = "/META-INF/" + path;
        URL url = Library.class.getResource(absPath);

        // Resource not found - this is fatal
        if (url == null) {
            Limb.log(LogLevel.ERROR, "Failed to load resource \"" + absPath + "\".");
            throw new NoSuchResourceException("Failed to load resource \"" + absPath + "\".");
        }

        Limb.log(LogLevel.DEBUG, "Loaded \"" + absPath + "\".");

        return url;
    }

    /**
     * Retrieves a meta resource and returns it as an <code>InputStream</code>. Calling this function will not trigger initialization of the library.
     * 
     * @param path the path to the meta resource, cannot be <code>null</code>.
     * @return the resource as an {@link InputStream}.
     * @throws IllegalArgumentException if <code>path == null</code>.
     * @throws NoSuchResourceException if the resource could not be found.
     * @throws IOException if the stream could not be opened.
     */
    public static InputStream getMetaResourceAsStream(String path) throws IllegalArgumentException, NoSuchResourceException, IOException {
        return getMetaResource(path).openStream();
    }

    /**
     * Returns the official human-readable name of this library.
     * 
     * @return the name, for example <code>"Logdoc"</code>, never <code>null</code>.
     */
    public static final String getName() {
        return "Logdoc";
    }

    /**
     * Returns the version of this library.
     * 
     * @return the version of this library, for example <code>"3.0"</code>, or <code>null</code> if unknown.
     */
    public static final String getVersion() {
        return VERSION;
    }

    public static final void main(String[] args) {
        if (VERSION == null) {
            System.out.println(getName());
        } else {
            System.out.println(getName() + " " + getVersion());
        }
    }

    private Library() {
        // empty
    }
}
