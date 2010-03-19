// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.znerd.logdoc.internal.InternalLogging;

/**
 * Class that represents the Logdoc library.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class Library {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The version of this library, lazily initialized.
    */
   private static final String VERSION = Library.class.getPackage().getImplementationVersion();
   
   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Retrieves a meta resource and returns it as a <code>URL</code>.
    * 
    * @param path
    *    the path to the meta resource, cannot be <code>null</code>.
    *    
    * @return
    *    the resource as a {@link URL}, never <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>path == null</code>.
    *    
    * @throws NoSuchResourceException
    *    if the resource could not be found.
    */
   static URL getMetaResource(String path)
   throws IllegalArgumentException, NoSuchResourceException {
      
      // Check preconditions
      if (path == null) {
         throw new IllegalArgumentException("path == null");
      }
      
      // Load the resource
      String absPath = "/META-INF/" + path;
      URL        url = Library.class.getResource(absPath);
      
      // Resource not found - this is fatal
      if (url == null) {
         InternalLogging.log(LogLevel.ERROR, "Failed to load resource \"" + absPath + "\".");
         throw new NoSuchResourceException("Failed to load resource \"" + absPath + "\".");
      }
      
      InternalLogging.log(LogLevel.DEBUG, "Loaded \"" + absPath + "\".");
      
      return url;
   }

   /**
    * Retrieves a meta resource and returns it as an <code>InputStream</code>.
    * Calling this function will not trigger initialization of the library.
    * 
    * @param path
    *    the path to the meta resource, cannot be <code>null</code>.
    *    
    * @return
    *    the resource as an {@link InputStream}.
    *
    * @throws IllegalArgumentException
    *    if <code>path == null</code>.
    *    
    * @throws NoSuchResourceException
    *    if the resource could not be found.
    *    
    * @throws IOException
    *    if the stream could not be opened.
    */
   static InputStream getMetaResourceAsStream(String path)
   throws IllegalArgumentException, NoSuchResourceException, IOException {
      return getMetaResource(path).openStream();
   }
   
   /**
    * Utility function that quotes the specified text. For example, when the
    * string <code>Hello "there"</code> is passed as input, then the string
    * <code>"Hello \"there\""</code> is returned as output. When
    * <code>null</code> is passed as input, then the string
    * <code>(null)</code> is returned as output.
    * 
    * @param input
    *    the input text, can be <code>null</code>.
    *    
    * @return
    *    the quoted output string, or <code>(null)</code> if the input string
    *    is <code>null</code>.
    */
   static String quote(String input) {
      if (input == null) {
         return "(null)";
      } else {
         return "\"" + input + '"'; // TODO: Review, perhaps escape input?
      }
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
      return VERSION;
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
