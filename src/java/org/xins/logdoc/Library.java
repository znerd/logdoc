/*
 * $Id$
 *
 * Copyright 2003-2009 Ernst de Haan
 * See the COPYRIGHT file for redistribution and use restrictions.
 */
package org.xins.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.jar.Attributes;

import org.apache.commons.io.IOUtils;

/**
 * Class that represents the Logdoc library.
 *
 * @version $Revision$ $Date$
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 *
 * @since Logdoc 3.0
 */
public final class Library {

   /**
    * Constructs a new <code>Library</code> object.
    */
   private Library() {
      // empty
   }

   /**
    * Returns the version of this library.
    *
    * @return
    *    the version of this library, for example <code>"3.0"</code>,
    *    or <code>null</code> if unknown.
    */
   public static final String getVersion() {
      InputStream stream = Library.class.getResourceAsStream("/version.txt");
      if (stream == null) {
         System.err.println("File version.txt not found.");
         return null;
      }

      String version;
      try {
         version = IOUtils.toString(stream, "UTF-8").trim();
      } catch (IOException e) {
         System.err.println("Failed to read version.txt file.");
         return null;
      }

      return version;
   }
}
