// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import org.znerd.logdoc.LogLevel;

/**
 * Logdoc-internal logging.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public class InternalLogging {
   
   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The singleton instance. Never <code>null</code>.
    */
   private static InternalLogging INSTANCE;
   
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
   public static synchronized void log(LogLevel level, String message, Throwable exception) {
      if (level == null) {
         level = LogLevel.DEBUG;
      }
      if (message == null) {
         message = "";
      }

      INSTANCE.logImpl(level, message, exception);
   }
   
   /**
    * Sets the <code>InternalLogging</code> instance to use.
    * 
    * @param logger
    *    the instance to use, cannot be <code>null</code>.
    *    
    * @throws IllegalArgumentException
    *    if <code>logger == null</code>.
    */
   public static synchronized void setLogger(InternalLogging logger)
   throws IllegalArgumentException {
      if (logger == null) {
         throw new IllegalArgumentException("logger == null");
      }
      INSTANCE = logger;
   }


   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>InternalLogging</code> object.
    */
   protected InternalLogging() {
      // empty
   }
   
   
   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

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
   public void logImpl(LogLevel level, String message, Throwable exception) {
      System.err.println(level.name() + ' ' + message);
      if (exception != null) {
         exception.printStackTrace();
      }      
   }
}
