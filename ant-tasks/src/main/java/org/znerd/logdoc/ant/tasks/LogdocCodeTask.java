// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import org.apache.tools.ant.BuildException;

import org.znerd.logdoc.LogDef;

/**
 * An Apache Ant task for generating source files from Logdoc definitions.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogdocCodeTask extends AbstractLogdocTask {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The default target, <code>"log4j"</code>.
    */
   private static final String DEFAULT_TARGET = "log4j";


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogdocJavaTask</code> object.
    */
   public LogdocCodeTask() {
      // empty
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The target. By default <code>"log4j"</code>.
    */
   private String _target;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Sets the target for the code generation. The default is 
    * <code>"log4j"</code>.
    *
    * @param newTarget
    *    the new target.
    */
   public void setTarget(String newTarget) {
      _target = newTarget;
   }

   @Override
   protected void executeImpl(LogDef def) throws Exception {

      String target;

      // No target specified, use the default
      if (_target == null || _target.trim().length() < 1) {
         target = DEFAULT_TARGET;

      // Target explicitly specified, check it
      } else {
         target = _target.trim().toLowerCase();
         if (! target.equals("log4j")) {
            throw new BuildException("Unsupported target \"" + _target + "\".");
         }
      }

      // Generate code
      def.generateCode(target, _destDir);
   }
}
