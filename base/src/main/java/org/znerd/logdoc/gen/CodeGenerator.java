// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.gen;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.internal.Resolver;

/**
 * Code generator.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class CodeGenerator {

   /**
    * Generates the Java code for the specified log definition.
    *
    * @param def
    *    the {@link LogDef} object, cannot be <code>null</code>.
    *
    * @param target
    *    the target to generate code for, e.g. <code>"log4j"</code> or
    *    <code>"slf4j"</code>, cannot be <code>null</code>.
    *
    * @param targetDir
    *    the target directory to create the Java source files in,
    *    cannot be <code>null</code>, and must be an existent writable
    *    directory.
    *
    * @throws IllegalArgumentException
    *    if <code>def == null || target == null || targetDir == null</code>.
    *
    * @throws IOException
    *    if the Java code could not be generated.
    */
   public void generateCode(LogDef def, String target, File targetDir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (target == null) {
         throw new IllegalArgumentException("target == null");
      } else if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      }

      // Perform transformations
      transformToJava(def, target + '/', targetDir, "Log");
      transformToJava(def, "",           targetDir, "TranslationBundle");
      for (Map.Entry<String,Document> entry : def.getTranslations().entrySet()) {
         String           locale = entry.getKey();
         Document translationXML = entry.getValue();
         transformToJavaForLocale(def, targetDir, locale, translationXML);
      }
   }

   private void transformToJava(LogDef def, String xsltSubDir, File targetDir, String className)
   throws IOException {

      Source      source = new DOMSource(def.getXML());
      String    xsltPath = xsltSubDir + "log_to_" + className + "_java" + ".xslt";
      String  domainPath = def.getDomainName().replace(".", "/");
      File        outDir = new File(targetDir, domainPath);
      String outFileName = className + ".java";

      Map<String,String> xsltParams = new HashMap<String,String>();
      xsltParams.put("package_name", def.getDomainName());
      xsltParams.put("accesslevel",  def.isPublic() ? "public" : "protected");

      new Xformer(def).transformAndHandleExceptions(source, xsltPath, xsltParams, outDir, outFileName);
   }

   private void transformToJavaForLocale(LogDef def, File targetDir, String locale, Document translationXML)
   throws IOException {

      Source      source = new DOMSource(translationXML);
      String    xsltPath = "translation-bundle_to_java.xslt";
      String outFileName = "TranslationBundle_" + locale + ".java";

      Map<String,String> xsltParams = new HashMap<String,String>();
      xsltParams.put("package_name", def.getDomainName());
      xsltParams.put("accesslevel",  def.isPublic() ? "public" : "protected");
      xsltParams.put("locale",       locale);

      new Xformer(def).transformAndHandleExceptions(source, xsltPath, xsltParams, targetDir, outFileName);
   }
}
