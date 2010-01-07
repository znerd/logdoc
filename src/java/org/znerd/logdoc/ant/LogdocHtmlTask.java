// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant;

import org.apache.tools.ant.BuildException;

import org.znerd.logdoc.LogDef;

/**
 * An Apache Ant task for generating web pages from Logdoc definitions.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogdocHtmlTask extends AbstractLogdocTask {

   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogdocHtmlTask</code> object.
    */
   public LogdocHtmlTask() {
      // empty
   }


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   @Override
   protected void executeImpl(LogDef logDef) throws BuildException {
      // TODO FIXME
   }
}
