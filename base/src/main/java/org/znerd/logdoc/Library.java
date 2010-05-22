// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.znerd.logdoc.internal.InternalLogging;
import org.znerd.logdoc.internal.LogCentral;

/**
 * Class that represents the Logdoc library.
 *
 * <p>This class can be run as a program. When it is, all it does is display
 * the name of this library and the version to standard out.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class Library {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The version of this library, lazily initialized.
    */
   private static final String VERSION = Library.class.getPackage().getImplementationVersion();

   /**
    * The name of the property that specifies which locale should be used.
    */
   private static final String LOG_LOCALE_PROPERTY = "org.znerd.logdoc.locale";

   /**
    * The name of the property that specifies if all stack traces should be
    * displayed at the message level. By default, stack traces are displayed
    * at the <em>DEBUG</em> level.
    */
   private static final String LOG_STACK_TRACE_AT_MESSAGE_LEVEL = "org.znerd.logdoc.stackTraceAtMessageLevel";

   /**
    * The name of the property that specifies the name of the
    * <code>LogFilter</code> class to use.
    */
   private static final String LOG_FILTER_PROPERTY = "org.znerd.logdoc.filterClass";

   /**
    * The default locale used at start-up, if no locale is specified in a
    * system property.
    */
   public static final String DEFAULT_LOCALE = "en_US";

   /**
    * The locale for the logdoc.
    */
   private static String LOCALE = null;

   /**
    * Flag indicating whether the stack trace should be displayed at the same
    * level of the message or not. Default is <code>false</code>.
    */
   private static boolean STACK_TRACE_AT_MESSAGE_LEVEL = false;

   /**
    * The active <code>LogFilter</code> instance.
    */
   private static LogFilter LOG_FILTER;


   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Initializes this class by determining the startup locale and 
    * initializing the log filter.
    */
   static {
      LOCALE = determineStartupLocale();
      initLogFilter();
   }

   /**
    * Determines the start-up locale. If the system property
    * {@link #LOG_LOCALE_PROPERTY} is set to a non-empty value, then this
    * will be returned, otherwise {@link #DEFAULT_LOCALE} is returned.
    *
    * <p>This method is called from
    * {@link #registerLog(AbstractLog.LogController)} as soon as the first
    * {@link AbstractLog.LogController} is registered.
    *
    * @return
    *    the locale to use initially, at start-up.
    */
   private static String determineStartupLocale() {

      // Use the value of the system property, if set...
      String locale = System.getProperty(LOG_LOCALE_PROPERTY);
      if (locale != null && locale.trim().length() > 0) {
         return locale;

      // ...or otherwise fallback to the default locale
      } else {
         return DEFAULT_LOCALE;
      }
   }

   /**
    * Determines which <code>LogFilter</code> class to use, constructs an
    * instance and stores it inside this class
    *
    * <p>If no filter is configured in the system properties (see
    * {@link #LOG_FILTER_PROPERTY}), then a {@link SimpleLogFilter} is
    * used.
    *
    * <p>If a filter is configured in the system properties, but could not be
    * constructed, then a {@link NullLogFilter} is used, to avoid any
    * security issues.
    */
   private static void initLogFilter() {

      // Get the system property
      String s = System.getProperty(LOG_FILTER_PROPERTY);

      // Property is not set, use a SimpleLogFilter
      if (s == null || s.trim().length() < 1) {
         setLogFilter(new SimpleLogFilter());

      // Property is set, use it
      } else {
         setLogFilterByClass(s);
      }
   }

   /**
    * Sets the locale on all <em>logdoc</em> <code>Log</code> classes.
    *
    * @param newLocale
    *    the new locale, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>newLocale == null</code>.
    *
    * @throws UnsupportedLocaleException
    *    if the specified locale is not supported by all registered
    *    <em>logdoc</em> <code>Log</code> classes.
    */
   public static void setLocale(String newLocale)
   throws IllegalArgumentException, UnsupportedLocaleException {

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
    * Sets the locale on all <em>logdoc</em> <code>Log</code> classes to the
    * default locale.
    */
   public static void useDefaultLocale() {
      setLocale(DEFAULT_LOCALE);
   }

   /**
    * Get the locale set in this LogCentral.
    *
    * @return
    *    the locale, e.g. <code>"en_US"</code>;
    *    never <code>null</code>.
    */
   public static String getLocale() {
      return LOCALE;
   }

   /**
    * Enables or disables the display of the stack trace at the same level as
    * the message.
    *
    * @param sameLevel
    *    <code>true</code> if the stack trace should be at the same level,
    *    <code>false</code> if the stack trace should be at DEBUG level.
    */
   public static void setStackTraceAtMessageLevel(boolean sameLevel) {
       STACK_TRACE_AT_MESSAGE_LEVEL = sameLevel;
   }

   /**
    * Indicates whether the stack trace should be displayed at the same level
    * as the message.
    *
    * @return
    *    <code>true</code> if the stack trace should be at the same level,
    *    <code>false</code> if the stack trace should be at DEBUG level.
    */
   public static boolean isStackTraceAtMessageLevel() {
       return STACK_TRACE_AT_MESSAGE_LEVEL;
   }

   /**
    * Set the active <code>LogFilter</code>.
    *
    * @param logFilter
    *    the new {@link LogFilter} to use,
    *    cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>logFilter == null</code>.
    */
   public static synchronized void setLogFilter(LogFilter logFilter)
   throws IllegalArgumentException {

      // Check preconditions
      if (logFilter == null) {
         throw new IllegalArgumentException("logFilter == null");
      }

      InternalLogging.log(LogLevel.INFO, "Set LogFilter to instance of class " + logFilter.getClass().getName() + '.');

      // Store the filter in this class
      LOG_FILTER = logFilter;
   }

   /**
    * Set the active <code>LogFilter</code> by class name. If the parameter is
    * <code>null</code> then an exception is thrown. Otherwise if an instance
    * cannot be constructed, then an instance of class {@link NullLogFilter}
    * is used instead.
    *
    * @param className
    *    the name of the {@link LogFilter} class to use,
    *    cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>className == null</code>.
    */
   public static void setLogFilterByClass(String className)
   throws IllegalArgumentException {

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
         InternalLogging.log(LogLevel.ERROR, "Failed to construct LogFilter of class: " + className + ". Using NullLogFilter.", cause);
         logFilter = new NullLogFilter();
      }

      // Delegate to synchronized setter
      setLogFilter(logFilter);
   }

   /**
    * Returns the active <code>LogFilter</code>.
    *
    * @return
    *    the current {@link LogFilter}, never <code>null</code>.
    */
   public static synchronized LogFilter getLogFilter() {
      return LOG_FILTER;
   }

   /**
    * Retrieves a meta resource and returns it as a <code>URL</code>.
    *
    * @param path
    *    the path to the meta resource, cannot be <code>null</code>.
    *
    * @return
    *    the resource as a {@link URL}, never <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>path == null</code>.
    *
    * @throws NoSuchResourceException
    *    if the resource could not be found.
    */
   static URL getMetaResource(String path)
   throws IllegalArgumentException, NoSuchResourceException {

      // Check preconditions
      if (path == null) {
         throw new IllegalArgumentException("path == null");
      }

      // Load the resource
      String absPath = "/META-INF/" + path;
      URL        url = Library.class.getResource(absPath);

      // Resource not found - this is fatal
      if (url == null) {
         InternalLogging.log(LogLevel.ERROR, "Failed to load resource \"" + absPath + "\".");
         throw new NoSuchResourceException("Failed to load resource \"" + absPath + "\".");
      }

      InternalLogging.log(LogLevel.DEBUG, "Loaded \"" + absPath + "\".");

      return url;
   }

   /**
    * Retrieves a meta resource and returns it as an <code>InputStream</code>.
    * Calling this function will not trigger initialization of the library.
    *
    * @param path
    *    the path to the meta resource, cannot be <code>null</code>.
    *
    * @return
    *    the resource as an {@link InputStream}.
    *
    * @throws IllegalArgumentException
    *    if <code>path == null</code>.
    *
    * @throws NoSuchResourceException
    *    if the resource could not be found.
    *
    * @throws IOException
    *    if the stream could not be opened.
    */
   static InputStream getMetaResourceAsStream(String path)
   throws IllegalArgumentException, NoSuchResourceException, IOException {
      return getMetaResource(path).openStream();
   }

   /**
    * Utility function that quotes the specified text. For example, when the
    * string <code>Hello "there"</code> is passed as input, then the string
    * <code>"Hello \"there\""</code> is returned as output. When
    * <code>null</code> is passed as input, then the string
    * <code>(null)</code> is returned as output.
    *
    * @param input
    *    the input text, can be <code>null</code>.
    *
    * @return
    *    the quoted output string, or <code>(null)</code> if the input string
    *    is <code>null</code>.
    */
   static String quote(String input) {
      if (input == null) {
         return "(null)";
      } else {
         return "\"" + input + '"'; // TODO: Review, perhaps escape input?
      }
   }

   /**
    * Returns the official human-readable name of this library.
    *
    * @return
    *    the name, for example <code>"Logdoc"</code>,
    *    never <code>null</code>.
    */
   public static final String getName() {
      return "Logdoc";
   }

   /**
    * Returns the version of this library.
    *
    * @return
    *    the version of this library, for example <code>"3.0"</code>,
    *    or <code>null</code> if unknown.
    */
   public static final String getVersion() {
      return VERSION;
   }

   /**
    * Prints the name and version of this library.
    */
   public static final void main(String[] args) {
      System.out.println(getName() + " " + getVersion());
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>Library</code> object.
    */
   private Library() {
      // empty
   }
}
