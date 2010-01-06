// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Log definition. Typically read from a <code>log.xml</code> file.
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
         Resolver          resolver = new Resolver();
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
      Element docElem = xml.getDocumentElement();
      boolean isPublic = Boolean.parseBoolean(docElem.getAttribute("public"));
      
      // Parse the locales for the translation bundles
      // Typical is: <translation-bundle locale="en_US" />
      Map<String,Document> translations = new HashMap<String,Document>();
      NodeList elems = docElem.getElementsByTagName("translation-bundle");
      for (int index = 0; index < elems.getLength(); index++) {
         Element elem = (Element) elems.item(index);
         String locale = elem.getAttribute("locale");
 
         // Define the location of the log.xml file
         File tbFile = new File(dir, "translation-bundle-" + locale + ".xml");

         Document tbXML;
         try {

            // Create a validating DOM/XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder domBuilder = factory.newDocumentBuilder();
            Resolver          resolver = new Resolver();
            domBuilder.setEntityResolver(resolver);
            domBuilder.setErrorHandler(new ErrorHandler());

            // Parse the file to produce a DOM/XML object
            tbXML = domBuilder.parse(tbFile);

         } catch (ParserConfigurationException cause) {
            IOException e = new IOException("Failed to parse \"log.xml\" file.");
            e.initCause(cause);
            throw e;
         } catch (SAXException cause) {
            IOException e = new IOException("Failed to parse \"log.xml\" file.");
            e.initCause(cause);
            throw e;
         }
         
         translations.put(locale, tbXML);
      }

      return new LogDef(xml, domainName, isPublic, translations);
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
    * @param isPublic
    *    flag that indicates if the generated code should be considered
    *    public, even outside its own domain/namespace.
    *    
    * @param translations
    *    the translation bundle XML {@link Document}s, indexed by name;
    *    cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>xml == null || domainName == null || locales == null || translations == null</code>.
    */
   private LogDef(Document xml, String domainName, boolean isPublic, Map<String,Document> translations)
   throws IllegalArgumentException {
      
      // Check preconditions
      if (xml == null) {
         throw new IllegalArgumentException("xml == null");
      } else if (domainName == null) {
         throw new IllegalArgumentException("domainName == null");
      } else if (translations == null) {
         throw new IllegalArgumentException("translations == null");
      }

      // Initialize fields
      _xml          = xml;
      _domainName   = domainName;
      _public       = isPublic;
      _translations = translations;
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
   
   /**
    * Flag that indicates if the generated code should be considered
    * accessible even outside its own domain/namespace.
    */
   private final boolean _public;
   
   /**
    * The translation bundles, indexed by name. Never <code>null</code>.
    */
   private final Map<String,Document> _translations;


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
   public void generateCode(File targetDir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      }

      // Perform transformations
      transform(targetDir, "Log");
      transform(targetDir, "TranslationBundle");
      for (String locale : _translations.keySet()) {
         transformForLocale(targetDir, locale);
      }
   }

   private Source getSource() {
      return new DOMSource(_xml);
   }
   
   private Source getTranslationBundleSource(String locale) {
	   return new DOMSource(_translations.get(locale));
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
         String                   xsltPath = "xslt/log_to_" + className + "_java.xslt";
         InputStream            xsltStream = Library.getMetaResourceAsStream(xsltPath);
         StreamSource     xsltStreamSource = new StreamSource(xsltStream);
         TransformerFactory xformerFactory = TransformerFactory.newInstance();
         xformerFactory.setURIResolver(new Resolver());
         Transformer               xformer = xformerFactory.newTransformer(xsltStreamSource);

         // Set the parameters for the template
         xformer.setParameter("package_name", _domainName);
         xformer.setParameter("accesslevel",  _public ? "public" : "protected");

         // Make sure the output directory exists
         String     domainPath = _domainName.replace(".", "/");
         File           outDir = new File(baseDir, domainPath);
         if (! outDir.exists()) {
            boolean outDirCreated = outDir.mkdirs();
            if (! outDirCreated) {
               throw new IOException("Failed to create output directory \"" + outDir.getPath() + "\".");
            }
         } else if (! outDir.isDirectory()) {
            throw new IOException("Path \"" + outDir.getPath() + "\" exists, but it is not a directory.");
         }

         // Declare where the XSLT output should go
         File        outFile = new File(outDir, className + ".java");
         StreamResult result = new StreamResult(outFile);

         // Perform the transformation
         //System.err.println("About to perform XSLT transformation. xsltPath=\"" + xsltPath + "\"; domainName=\"" + _domainName + "\"; domainPath=\"" + domainPath + "\".");
         xformer.transform(getSource(), result);
         
         //System.err.println("Generated file \"" + outFile.getPath() + "\".");

      // Transformer configuration error
      } catch (TransformerConfigurationException cause) {
         throw newIOException("Unable to perform XSLT transformation due to configuration problem.", cause);

      // Transformer error
      } catch (TransformerException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);
      }
   }
   
   private void transformForLocale(File baseDir, String locale)
   throws IOException {

      try {

         // Create an XSLT Transforer
         String                   xsltPath = "xslt/translation-bundle_to_java.xslt";
         InputStream            xsltStream = Library.getMetaResourceAsStream(xsltPath);
         StreamSource     xsltStreamSource = new StreamSource(xsltStream);
         TransformerFactory xformerFactory = TransformerFactory.newInstance();
         xformerFactory.setURIResolver(new Resolver());
         Transformer               xformer = xformerFactory.newTransformer(xsltStreamSource);

         // Set the parameters for the template
         xformer.setParameter("package_name", _domainName);
         xformer.setParameter("accesslevel",  _public ? "public" : "protected");

         // Make sure the output directory exists
         String domainPath = _domainName.replace(".", "/");
         File       outDir = new File(baseDir, domainPath);
         if (! outDir.exists()) {
            boolean outDirCreated = outDir.mkdirs();
            if (! outDirCreated) {
               throw new IOException("Failed to create output directory \"" + outDir.getPath() + "\".");
            }
         } else if (! outDir.isDirectory()) {
            throw new IOException("Path \"" + outDir.getPath() + "\" exists, but it is not a directory.");
         }

         // Declare where the XSLT output should go
         String    className = "TranslationBundle_" + locale;
         File        outFile = new File(outDir, className + ".java");
         StreamResult result = new StreamResult(outFile);

         // Perform the transformation
         System.err.println("About to perform XSLT transformation. xsltPath=\"" + xsltPath + "\"; domainName=\"" + _domainName + "\"; domainPath=\"" + domainPath + "\".");
         xformer.transform(getTranslationBundleSource(locale), result);
         
         System.err.println("Generated file \"" + outFile.getPath() + "\".");

      // Transformer configuration error
      } catch (TransformerConfigurationException cause) {
         throw newIOException("Unable to perform XSLT transformation due to configuration problem.", cause);

      // Transformer error
      } catch (TransformerException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);
      }
   }

   private static class ErrorHandler implements org.xml.sax.ErrorHandler {

      public void error(SAXParseException exception) throws SAXException {
         exception.printStackTrace();
         throw new SAXException(exception);
      }

      public void fatalError(SAXParseException exception) throws SAXException {
         exception.printStackTrace();
         throw new SAXException(exception);
      }

      public void warning(SAXParseException exception) throws SAXException {
         // empty
      }
   }
}
