// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks.internal;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.znerd.logdoc.LogLevel;
import org.znerd.logdoc.internal.InternalLogging;

/**
 * Logdoc-internal logger that sends output via the Ant logging mechanism.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public class AntInternalLogging extends InternalLogging {
   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>AntInternalLogging</code> object for the
    * specified Ant task.
    */
   public AntInternalLogging(Task task) {
      if (task == null) {
         throw new IllegalArgumentException("task == null");
      }
      _task = task;
   }
   
   
   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The associated Ant project. Never <code>null</code>.
    */
   private Task _task;
   
   
   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   @Override
   public void logImpl(LogLevel level, String message, Throwable exception) {
      int antLevel = level == LogLevel.DEBUG   ? Project.MSG_DEBUG
                   : level == LogLevel.INFO    ? Project.MSG_VERBOSE
                   : level == LogLevel.NOTICE  ? Project.MSG_INFO
                   : level == LogLevel.WARNING ? Project.MSG_WARN
                   :                             Project.MSG_ERR;

      if (exception == null) {
         _task.log(message, antLevel);
      } else {
         _task.log(message, exception, antLevel);
      }
   }
}
