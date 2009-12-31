/* Copyright 2009, Ernst de Haan */
package org.xins.logdoc.def;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

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
    * @param packageName
    *    the name of the package for the Java code,
    *    cannot be <code>null</code>.
    *
    * @param accessLevel
    *    the access level for the Java code, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>targetDir == null || packageName == null || accessLevel == null</code>
    *    or if <code>packageName</code> is not a valid Java package name.
    *
    * @throws IOException
    *    if the Java code could not be generated.
    */
   public void generateJavaCode(File targetDir, String packageName, AccessLevel accessLevel)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      } else if (packageName == null) {
         throw new IllegalArgumentException("packageName == null");
      } else if (accessLevel == null) {
         throw new IllegalArgumentException("packageName == null");
      } else if (! packageName.matches("[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)")) {
         throw new IllegalArgumentException("Invalid package name \"" + packageName + "\".");
      }

      // Perform transformations
      transform(targetDir, packageName, "Log",               accessLevel);
      transform(targetDir, packageName, "TranslationBundle", accessLevel);
   }

   private StreamSource getStreamSource() {
      return null; // TODO FIXME
   }

   private IOException newIOException(String detail, Throwable cause) {
      IOException e = new IOException(detail);
      e.initCause(cause);
      return e;
   }

   private void transform(File baseDir, String packageName, String className, AccessLevel accessLevel)
   throws IOException {

      try {

         // Create an XSLT Transforer
         String                   xsltPath = "/xslt/log_to_" + className + "_java.xslt";
         InputStream            xsltStream = getClass().getResourceAsStream(xsltPath);
         StreamSource     xsltStreamSource = new StreamSource(xsltStream);
         TransformerFactory xformerFactory = TransformerFactory.newInstance();
         Transformer               xformer = xformerFactory.newTransformer(xsltStreamSource);

         // Define where the output should go
         File               outDir = new File(baseDir, packageName.replace("\\.", "/"));
         File              outFile = new File(outDir, className + ".java");
         StreamResult streamResult = new StreamResult(outFile);

         // Perform the transformation
         xformer.transform(getStreamSource(), streamResult);

      // Transformer configuration error
      } catch (TransformerConfigurationException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);

      // Transformer error
      } catch (TransformerException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);
      }
   }


   //-------------------------------------------------------------------------
   // Inner classes
   //-------------------------------------------------------------------------

   /**
    * Enumeration type for the different <em>accessLevel</em> options.
    *
    * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
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
