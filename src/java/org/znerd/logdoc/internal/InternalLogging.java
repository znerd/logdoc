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
    * Logs a message at a specified level.
    * 
    * @param level
    *    the {@link LogLevel}, <code>null</code> is equivalent to passing
    *    {@link LogLevel#DEBUG}.
    * 
    * @param message
    *    the message, or <code>null</code>.
    */
   public static void log(LogLevel level, String message) {
      log(level, message, (Throwable) null);
   }
   
   /**
    * Logs a message at a specified level, with an optional exception.
    * 
    * @param level
    *    the {@link LogLevel}, <code>null</code> is equivalent to passing
    *    {@link LogLevel#DEBUG}.
    * 
    * @param message
    *    the message, or <code>null</code> if an empty message should be
    *    logged.
    *    
    * @param exception
    *    the exception to log, or <code>null</code> if there is no exception
    *    to log.
    */
   public static void log(LogLevel level, String message, Throwable exception) {
      if (level == null) {
         level = LogLevel.DEBUG;
      }
      if (message == null) {
         message = "";
      }
      System.err.println(level.name() + ' ' + message);
      if (exception != null) {
         exception.printStackTrace();
      }
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
