// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.LogLevel;

/**
 * Logdoc-internal logging.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class InternalLogging {
   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Logs a debug message.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void debug(String message) {
      log(LogLevel.DEBUG, message);
   }

   /**
    * Logs an informational message.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void info(String message) {
      log(LogLevel.INFO, message);
   }
   
   /**
    * Logs an informational message that should typically be noticed.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void notice(String message) {
      log(LogLevel.NOTICE, message);
   }
   
   /**
    * Logs a warning message.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void warn(String message) {
      log(LogLevel.WARNING, message);
   }

   /**
    * Logs an error message.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void error(String message) {
      log(LogLevel.ERROR, message);
   }

   /**
    * Logs a fatal error message.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void fatal(String message) {
      log(LogLevel.FATAL, message);
   }
   
   private static void log(LogLevel level, String message) {
      System.err.println("" + level.name().charAt(0) + ' ' + message);
   }

   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>InternalLogging</code> object.
    */
   private InternalLogging() {
      // empty
   }
}
