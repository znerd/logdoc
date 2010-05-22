// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal.log4j;

import org.apache.log4j.Level;

import org.znerd.logdoc.UnsupportedLocaleException;

/**
 * Abstract base class for <em>logdoc</em> <code>Log</code> classes.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public abstract class AbstractLog {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------
   
   /**
    * The <em>debug</em> log level.
    */
   public static final Level DEBUG;

   /**
    * The <em>info</em> log level.
    */
   public static final Level INFO;

   /**
    * The <em>notice</em> log level.
    */
   public static final Level NOTICE;

   /**
    * The <em>warning</em> log level.
    */
   public static final Level WARNING;

   /**
    * The <em>error</em> log level.
    */
   public static final Level ERROR;

   /**
    * The <em>fatal</em> log level.
    */
   public static final Level FATAL;
   
   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Initializes this class.
    */
   static {

      // Determine the int value for the NOTICE level
      int noticeInt = (Level.INFO_INT + Level.WARN_INT) / 2;

      // Initialize all the log levels
      DEBUG   = Level.DEBUG;
      INFO    = Level.INFO;
      NOTICE  = new CustomLevel(noticeInt, "NOTICE", 5);
      WARNING = Level.WARN;
      ERROR   = Level.ERROR;
      FATAL   = Level.FATAL;
   }
   
   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------
   
   /**
    * Constructs a new <code>AbstractLog</code> instance.
    */
   protected AbstractLog() {
      // empty
   }

   
   //-------------------------------------------------------------------------
   // Inner classes   
   //-------------------------------------------------------------------------
   
   /**
    * Custom log level.
    *
    * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
    */
   private static final class CustomLevel extends Level {
      
      //----------------------------------------------------------------------
      // Class fields
      //----------------------------------------------------------------------
      
      /**
       * Unique ID used to determine serialization compatibility.
       */
      private static final long serialVersionUID = 1909887126346631322L;

      
      //----------------------------------------------------------------------
      // Constructors
      //----------------------------------------------------------------------
      
      /**
       * Constructs a new <code>CustomLevel</code> object, representing a
       * Log4J level.
       *
       * @param value
       *    the <code>int</code> value for this level.
       *
       * @param name
       *    the name for this level, should not be <code>null</code>.
       *
       * @param syslogEquivalent
       *    the syslog equivalent.
       */
      private CustomLevel(int value, String name, int syslogEquivalent) {
         super(value, name, syslogEquivalent);
      }
   }
}
