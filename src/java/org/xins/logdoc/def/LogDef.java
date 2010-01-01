// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc.def;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
    *    if <code>dir == null</code>, or if it is not a directory.
    *
    * @throws IOException
    *    if the definition could not be loaded.
    */
   public static final LogDef loadFromDirectory(File dir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (dir == null) {
         throw new IllegalArgumentException("dir == null");
      } else if (! dir.isDirectory()) {
         throw new IllegalArgumentException("Path (\"" + dir.getPath() + "\") is not a directory.");
      }

      // Define the location of the log.xml file
      File file = new File(dir, "log.xml");

      Document xml;
      try {

         // Create a validating DOM/XML parser
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating(true);

         DocumentBuilder domBuilder = factory.newDocumentBuilder();
         LogdocResolver    resolver = new LogdocResolver();
         domBuilder.setEntityResolver(resolver);
         domBuilder.setErrorHandler(new ErrorHandler());

         // Parse the file to produce a DOM/XML object
         xml = domBuilder.parse(file);

      } catch (ParserConfigurationException cause) {
         IOException e = new IOException("Failed to parse \"log.xml\" file.");
         e.initCause(cause);
         throw e;
      } catch (SAXException cause) {
         IOException e = new IOException("Failed to parse \"log.xml\" file.");
         e.initCause(cause);
         throw e;
      }
      
      // Parse the domain name
      String domainName = xml.getDocumentElement().getAttribute("domain");

      return new LogDef(xml, domainName);
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogDef</code>.
    *
    * @param xml
    *    the XML {@link Document} to construct this object from,
    *    cannot be <code>null</code>.
    *    
    * @param domainName
    *    the domain name, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>xml == null || domainName == null</code>.
    */
   private LogDef(Document xml, String domainName) throws IllegalArgumentException {

      // Check preconditions
      if (xml == null) {
         throw new IllegalArgumentException("xml == null");
      } else if (domainName == null) {
         throw new IllegalArgumentException("domainName == null");
      }

      // Initialize fields
      _xml        = xml;
      _domainName = domainName;
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The source file as a DOM document. Never <code>null</code>.
    */
   private final Document _xml;
   
   /**
    * The domain name. Never <code>null</code>.
    */
   private final String _domainName;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Generates the Java code for this log definition.
    *
    * @param targetDir
    *    the target directory to create the Java source files in,
    *    cannot be <code>null</code>, and must be an existent writable
    *    directory.
    *
    * @throws IllegalArgumentException
    *    if <code>targetDir == null</code>.
    *
    * @throws IOException
    *    if the Java code could not be generated.
    */
   public void generateJavaCode(File targetDir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      }

      // Perform transformations
      transform(targetDir, "Log");
      transform(targetDir, "TranslationBundle");
   }

   private Source getSource() {
      return new DOMSource(_xml);
   }

   private IOException newIOException(String detail, Throwable cause) {
      IOException e = new IOException(detail);
      e.initCause(cause);
      return e;
   }

   private void transform(File baseDir, String className)
   throws IOException {

      try {

         // Create an XSLT Transforer
         String                   xsltPath = "/META-INF/xslt/log_to_" + className + "_java.xslt";
         InputStream            xsltStream = getClass().getResourceAsStream(xsltPath);
         StreamSource     xsltStreamSource = new StreamSource(xsltStream);
         TransformerFactory xformerFactory = TransformerFactory.newInstance();
         xformerFactory.setURIResolver(new LogdocResolver());
         Transformer               xformer = xformerFactory.newTransformer(xsltStreamSource);

         // TODO: Set the parameters for the template

         // Define where the output should go
         File               outDir = new File(baseDir, _domainName.replace("\\.", "/"));
         File              outFile = new File(outDir, className + ".java");
         StreamResult streamResult = new StreamResult(outFile);

         // Perform the transformation
         System.err.println("About to perform XSLT transformation.");
         xformer.transform(getSource(), streamResult);

      // Transformer configuration error
      } catch (TransformerConfigurationException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);

      // Transformer error
      } catch (TransformerException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);
      }
   }
   
   private static class ErrorHandler implements org.xml.sax.ErrorHandler {

      public void error(SAXParseException exception) throws SAXException {
         throw new SAXException(exception);
      }

      public void fatalError(SAXParseException exception) throws SAXException {
         throw new SAXException(exception);
      }

      public void warning(SAXParseException exception) throws SAXException {
         // TODO
      }
   }
}
