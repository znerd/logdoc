// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant;

import org.znerd.logdoc.LogDef;

/**
 * An Apache Ant task for generating source files from Logdoc definitions.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogdocCodeTask extends AbstractLogdocTask {

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
   private String _target = "log4j";


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
      def.generateCode(_target, _destDir);
   }
}
