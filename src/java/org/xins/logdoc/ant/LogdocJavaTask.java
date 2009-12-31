// Copyright 2007-2009, PensioenPage B.V.
package org.xins.logdoc.ant;

import org.apache.tools.ant.BuildException;
import static org.apache.tools.ant.Project.MSG_VERBOSE;

/**
 * An Apache Ant task for generating Java source files from Logdoc
 * definitions.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogdocJavaTask extends AbstractLogdocTask {

   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogdocJavaTask</code> object.
    */
   public LogdocJavaTask() {
      // empty
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The access level.
    */
   private String _accessLevel;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Sets the access level. This is optional, the default is
    * {@link AccessLevel#PACKAGE}.
    */
   public void setAccessLevel(String s) {
      log("Setting \"accessLevel\" to: " + quote(s) + '.', MSG_VERBOSE);
      _accessLevel = s;
   }

   @Override
   protected void executeImpl() throws BuildException {
      // TODO FIXME
   }


   //-------------------------------------------------------------------------
   // Inner classes
   //-------------------------------------------------------------------------

   /**
    * Enumeration type for the different <em>accessLevel</em> options.
    *
    * @author <a href="mailto:ernst@pensioenpage.com">Ernst de Haan</a>
    */
   private enum AccessLevel {

      /**
       * Public: log messages can be generated from outside the package.
       */
      PUBLIC,
         
      /**
       * Package: log message can only be generated from inside the package.
       */
      PACKAGE;
   }
}
