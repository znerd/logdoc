// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

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
   
   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * Flag that indicates if this library has been initialized.
    * Initially <code>false</code>.
    */
   private static boolean INITIALIZED;
   
   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Retrieves a meta resource and returns it as a <code>URL</code>.
    * Calling this function will not trigger initialization of the library.
    * 
    * @param path
    *    the path to the meta resource, cannot be <code>null</code>.
    *    
    * @return
    *    the resource as a {@link URL}.
    *    
    * @throws IllegalArgumentException
    *    if <code>path == null</code>.
    */
   public static URL getMetaResource(String path) throws IllegalArgumentException {
      
      // Check preconditions
      if (path == null) {
         throw new IllegalArgumentException("path == null");
      }
      
      // Load the resource
      String absPath = "/META-INF/" + path;
      URL        url = Library.class.getResource(absPath);
      
      // Resource not found - this is fatal
      if (url == null) {
         throw new Error("Failed to load resource \"" + absPath + "\" from class loader.");
      }
      
      return url;
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
    * Initializes this library, if that is not done yet.
    * 
    * @return
    *    <code>true</code> if the library has been initialized on this
    *    thread, just now, or <code>false</code> if it had been initialized
    *    previously, possibly on a different thread. 
    */
   public synchronized static boolean init() {
      if (! INITIALIZED) {
         INITIALIZED = true;
         return true;
      } else {
         return false;
      }
   }
   
   /**
    * Checks if this library has been initialized yet.
    * 
    * @return
    *    <code>true</code> if this library is already initialized,
    *    <code>false</code> if it is not.
    */
   public synchronized static boolean isInitialized() {
      return INITIALIZED;
   }

   /**
    * Prints the name and version of this library.
    */
   public static final void main(String[] args) {
      System.out.println(getName() + " " + getVersion());
   }
   
   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>Library</code> object.
    */
   private Library() {
      // empty
   }
}
