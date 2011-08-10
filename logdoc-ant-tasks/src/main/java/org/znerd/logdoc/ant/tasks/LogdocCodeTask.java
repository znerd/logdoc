// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.gen.CodeGenerator;

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

      String target = (_target == null || _target.trim().length() < 1) ? DEFAULT_TARGET : _target;

      CodeGenerator generator = new CodeGenerator(def, target, _destDir);
      generator.generate();
   }
}
