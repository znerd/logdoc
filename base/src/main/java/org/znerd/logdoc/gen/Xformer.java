// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.gen;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.LogLevel;
import org.znerd.logdoc.Resolver;

import static org.znerd.logdoc.internal.ExceptionUtils.newIOException;
import static org.znerd.logdoc.internal.InternalLogging.log;

class Xformer {

   Xformer(LogDef logDef) {
      _resolver = logDef.getResolver();
   }

   final Resolver _resolver;

   final void transformAndHandleExceptions(Source source, String xsltPath, Map<String,String> xsltParams, File outDir, String outFileName) throws IOException {
      try {
         transform(source, xsltPath, xsltParams, outDir, outFileName);
      } catch (TransformerConfigurationException cause) {
         throw newIOException("Unable to perform XSLT transformation due to configuration problem.", cause);
      } catch (TransformerException cause) {
         throw newIOException("Failed to perform XSLT transformation.", cause);
      }
   }

   private final void transform(Source source, String xsltPath, Map<String,String> xsltParams, File outDir, String outFileName) throws TransformerConfigurationException, TransformerException, IOException {

      Transformer xformer = createTransformer(xsltPath);
      setTransformerParameters(xformer, xsltParams);
      assertOutputDirectory(outDir);

      File        outFile = new File(outDir, outFileName);
      StreamResult result = new StreamResult(outFile);

      log(LogLevel.INFO, "Generating file \"" + outFile.getPath() + "\" using stylesheet \"" + xsltPath + "\".");
      xformer.transform(source, result);
      log(LogLevel.INFO, "Generated file \"" + outFile.getPath() + "\" using stylesheet \"" + xsltPath + "\".");
   }

   private final Transformer createTransformer(String xsltPath) throws TransformerConfigurationException, IOException {
      TransformerFactory xformerFactory = TransformerFactory.newInstance();
      xformerFactory.setURIResolver(_resolver);

      InputStream        xsltStream = Library.getMetaResourceAsStream("xslt/" + xsltPath);
      StreamSource xsltStreamSource = new StreamSource(xsltStream);

      return xformerFactory.newTransformer(xsltStreamSource);
   }

   private final void setTransformerParameters(Transformer xformer, Map<String,String> params) {
      for (String key : params.keySet()) {
         xformer.setParameter(key, params.get(key));
      }
   }

   private final void assertOutputDirectory(File outDir) throws IOException {
      if (! outDir.exists()) {
         boolean outDirCreated = outDir.mkdirs();
         if (! outDirCreated) {
            throw new IOException("Failed to create output directory \"" + outDir.getPath() + "\".");
         }
      } else if (! outDir.isDirectory()) {
         throw new IOException("Path \"" + outDir.getPath() + "\" exists, but it is not a directory.");
      }
   }
}
