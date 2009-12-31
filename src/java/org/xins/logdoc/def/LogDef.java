/* Copyright 2009, Ernst de Haan */
package org.xins.logdoc.def;

import java.io.File;
import java.io.IOException;

/**
 * Log definition. Typically read from a <code>log.xml</code> file with one or
 * more associated translation bundles, see {@link TranslationBundleDef}.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 *
 * @since Logdoc 3.0
 */
public final class LogDef {

   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Loads a log definition from a specified directory.
    *
    * @param dir
    *    the directory to load the log definition from,
    *    cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>dir == null</code>.
    *
    * @throws IOException
    *    if the definition could not be loaded.
    */
   public static final LogDef loadFromDirectory(File dir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (dir == null) {
         throw new IllegalArgumentException("dir == null");
      }

      return null; // TODO FIXME
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogDef</code>.
    */
   private LogDef() {
      // empty
   }
}
