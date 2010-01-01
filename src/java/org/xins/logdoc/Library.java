// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

import java.io.InputStream;
import java.io.IOException;

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
    * Returns the official human-readable name of this library.
    *
    * @return
    *    the name, for example <code>"Logdoc"</code>,
    *    never <code>null</code>.
    */
   public static final String getName() {
      return "Logdoc";
   }

   /**
    * Returns the version of this library.
    *
    * @return
    *    the version of this library, for example <code>"3.0"</code>,
    *    or <code>null</code> if unknown.
    */
   public static final String getVersion() {
      InputStream stream = Library.class.getResourceAsStream("/META-INF/version.txt");
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

   /**
    * Prints the name and version of this library.
    */
   public static final void main(String[] args) {
      System.out.println(getName() + " " + getVersion());
   }
}
