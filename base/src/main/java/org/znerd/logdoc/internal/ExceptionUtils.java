// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import java.io.IOException;

/**
 * Utility functions related to exceptions.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class ExceptionUtils {
   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Determines the root cause for the specified exception.
    *
    * @param exception
    *    the exception to determine the root cause for, can be
    *    <code>null</code>.
    *
    * @return
    *    the root cause exception, can be <code>null</code>.
    */
   public static Throwable getRootCause(Throwable exception) {

      // Check preconditions
      if (exception == null) {
         return null;
      }

      // Get the root cause of the exception
      Throwable cause = exception.getCause();
      while (cause != null) {
         exception = cause;
         cause = exception.getCause();
      }

      return exception;
   }

   /**
    * Creates a new <code>IOException</code> with the specified cause
    * exception. This is a utility function that is useful in Java 1.5, since
    * an {@link IOException} constructor that accepts both a detail message
    * and a cause exception is not available until Java 6.
    * 
    * @param detail
    *    the detail message, can be <code>null</code>.
    *    
    * @param cause
    *    the cause exception, or <code>null</code> if none.
    *    
    * @return
    *    the new {@link IOException}, never <code>null</code>.
    */
   public static IOException newIOException(String detail, Throwable cause) {
      IOException e = new IOException(detail);
      if (cause != null) {
         e.initCause(cause);
      }
      return e;
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>ExceptionUtils</code> object.
    */
   private ExceptionUtils() {
      // empty
   }
}
