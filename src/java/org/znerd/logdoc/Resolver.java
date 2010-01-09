// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.znerd.logdoc.Library.quote;
import static org.znerd.logdoc.internal.InternalLogging.log;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Entity/URI resolver that can be used during XML parsing and during XSLT
 * transformation.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
class Resolver implements EntityResolver, URIResolver {

   // TODO: Cache the DTD information internally

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The URL to the local <code>log_3_0</code> DTD.
    * Initialized by the class initializer.
    */
   private static final URL LOG_DTD_URL;

   /**
    * The URL to the local <code>translation-bundle_3_0</code> DTD.
    * Initialized by the class initializer.
    */
   private static final URL TRANSLATION_BUNDLE_DTD_URL;


   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Class initializer that reads the DTDs into memory.
    */
   static {
      // TODO: Move this to the Library class

      LOG_DTD_URL = Library.getMetaResource("dtd/log_0_1.dtd");
      if (LOG_DTD_URL == null) {
         throw new Error("Failed to load log_0_1.dtd file.");
      }

      TRANSLATION_BUNDLE_DTD_URL = Library.getMetaResource("dtd/translation-bundle_0_1.dtd");
      if (TRANSLATION_BUNDLE_DTD_URL == null) {
         throw new Error("Failed to load translation-bundle_0_1.dtd file.");
      }
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>Resolver</code> for the specified input
    * directory.
    * 
    * @param dir
    *    the directory containing the input files,
    *    cannot be <code>null</code>.
    * 
    * @throws IllegalArgumentException
    *    if <code>dir == null</code>.
    */
   Resolver(File dir) throws IllegalArgumentException {

      // Check preconditions
      if (dir == null) {
         throw new IllegalArgumentException("dir == null");
      }

      // Initialize object
      _inputDir = dir;
      log(LogLevel.DEBUG, "Created Resolver for input directory " + quote(dir.getAbsolutePath()) + '.');
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The input directory. Never <code>null</code>.
    */
   private final File _inputDir;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   public Document loadInputDocument(String fileName) throws IllegalArgumentException, IOException {

      // Check preconditions
      if (fileName == null) {
         throw new IllegalArgumentException("fileName == null");
      }

      log(LogLevel.DEBUG, "Loading input document " + quote(fileName) + '.');

      File file = new File(_inputDir, fileName);

      try {

         // Create a validating DOM/XML parser
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating(true);

         DocumentBuilder domBuilder = factory.newDocumentBuilder();
         domBuilder.setEntityResolver(this);
         domBuilder.setErrorHandler(new ErrorHandler());

         // Parse the file to produce a DOM/XML object
         return domBuilder.parse(file);

      } catch (ParserConfigurationException cause) {
         throw ExceptionUtils.newIOException("Failed to parse \"" + fileName + "\" file.", cause);
      } catch (SAXException cause) {
         throw ExceptionUtils.newIOException("Failed to parse \"" + fileName + "\" file.", cause);
      }
   }

   public InputSource resolveEntity(String publicId, String systemId)
   throws SAXException, IOException {

      log(LogLevel.INFO, "Resolving location of DTD with public ID " + quote(publicId) + " and system ID " + quote(systemId));

      if ("-//znerd//DTD Logdoc Log 0.1//EN".equals(publicId)) {
         return new InputSource(LOG_DTD_URL.openStream());
      } else if ("-//znerd//DTD Logdoc Translation Bundle 0.1//EN".equals(publicId)) {
         return new InputSource(TRANSLATION_BUNDLE_DTD_URL.openStream());
      } else {
         throw new IOException("Unable to find DTD with public ID \"" + publicId + "\" and system ID \"" + systemId + "\".");
      }
   }

   public Source resolve(String href, String base) throws TransformerException {

      log(LogLevel.INFO, "Resolving href " + quote(href) + " (with base " + quote(base) + ") during XSLT transformation.");

      // Check preconditions
      if (href == null) {
         throw new TransformerException("href == null");

         // XSLT file
      } else if (href.endsWith(".xslt")) {
         String resultURL = "xslt/" + href;
         try {
            return new StreamSource(Library.getMetaResourceAsStream(resultURL));
         } catch (IOException cause) {
            throw new TransformerException("Failed to open meta resource \"" + resultURL + "\".", cause);
         }

         // Input file
      } else if (href.endsWith(".xml")) {
         return new StreamSource(new File(_inputDir, href));

         // Unknown file
      } else {
         throw new TransformerException("File with href \"" + href + "\" is not recognized.");
      }
   }


   //-------------------------------------------------------------------------
   // Inner classes
   //-------------------------------------------------------------------------

   private static class ErrorHandler implements org.xml.sax.ErrorHandler {

      public void error(SAXParseException exception) throws SAXException {
         log(LogLevel.ERROR, "Error during XML parsing.", exception);
         throw new SAXException(exception);
      }

      public void fatalError(SAXParseException exception) throws SAXException {
         log(LogLevel.ERROR, "Fatal error during XML parsing.", exception);
         throw new SAXException(exception);
      }

      public void warning(SAXParseException exception) throws SAXException {
         log(LogLevel.WARNING, "Warning during XML parsing.", exception);
      }
   }
}
