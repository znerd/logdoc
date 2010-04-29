// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.UnsupportedLocaleException;

/**
 * Central class for <em>logdoc</em> logging.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogCentral {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * All registered <code>LogController</code> instances.
    *
    * @see #registerLog(LogController)
    */
   private static LogController[] CONTROLLERS;


   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Forces that the specified class is initialized.
    *
    * @param c
    *    the {@link Class} to initialize, cannot be <code>null</code>.
    *
    * @throws NullPointerException
    *    if <code>c == null</code>.
    */
   public static <T> Class<T> forceInit(Class<T> c) {
      try {
         Class.forName(c.getName(), true, c.getClassLoader());
      } catch (ClassNotFoundException e) {
         throw new AssertionError(e);  // Cannot happen
      }
      return c;
   }

   /**
    * Registers the specified <code>LogController</code>, which represents a
    * <em>logdoc</em> <code>Log</code> class.
    *
    * @param controller
    *    the {@link LogController}, cannot be <code>null</code>.
    *
    * @throws UnsupportedLocaleException
    *    if {@link LogController} does not support the current Locale.
    */
   public static void registerLog(LogController controller)
   throws UnsupportedLocaleException {

      // When the first LogController registers, do one-time initialization
      forceInit(Library.class);

      String locale = Library.getLocale();

      // Set the locale on the controller
      if (controller.isLocaleSupported(locale)) {
         controller.setLocale(locale);

      // Fail if the controller does not support this locale
      } else {
         System.err.println("Locale \"" + locale + "\" is not supported by log controller: " + controller);
         throw new UnsupportedLocaleException(locale);
      }

      // Add it to the list of registered controllers
      if (CONTROLLERS == null) {
         CONTROLLERS = new LogController[] { controller };
      } else {
         int size = CONTROLLERS.length;
         LogController[] temp = new LogController[size + 1];
         System.arraycopy(CONTROLLERS, 0, temp, 0, size);
         temp[size] = controller;
         CONTROLLERS = temp;
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

      // Make sure the locale is supported by all controllers
      int size = (CONTROLLERS == null) ? 0 : CONTROLLERS.length;
      for (int i = 0; i < size; i++) {
         if (! CONTROLLERS[i].isLocaleSupported(newLocale)) {
            throw new UnsupportedLocaleException(newLocale);
         }
      }

      // Change the locale on all controllers
      for (int i = 0; i < size; i++) {
         CONTROLLERS[i].setLocale(newLocale);
      }
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogCentral</code> instance. This constructor is
    * intentionally made <code>private</code>, since no instances should be
    * constructed of this class.
    */
   private LogCentral() {
      // empty
   }
}
