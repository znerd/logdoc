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
 * Documentation generator.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class DocGenerator {

   /**
    * Generates the HTML documentation for the specified log definition.
    *
    * @param def
    *    the {@link LogDef} object, cannot be <code>null</code>.
    *
    * @param targetDir
    *    the target directory to create the HTML documentation files in,
    *    cannot be <code>null</code>, and must be an existent writable
    *    directory.
    *
    * @throws IllegalArgumentException
    *    if <code>targetDir == null</code>.
    *
    * @throws IOException
    *    if the HTML documentation files could not be generated.
    */
   public void generateHtml(LogDef def, File targetDir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      } else if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      }

      transformToHtml(def, targetDir, "",      "index"     , new HashMap<String,String>());
      transformToHtml(def, targetDir, "_list", "entry-list", new HashMap<String,String>());

      for (LogDef.Group group : def.getGroups()) {
         String groupID = group.getID();
         Map<String,String> xsltParams = new HashMap<String,String>();
         xsltParams.put("group", groupID);
         String stylesheetName = "_group";
         String outName = "group-" + groupID;
         transformToHtml(def, targetDir, stylesheetName, outName, xsltParams);

         for (LogDef.Entry entry : group.getEntries()) {
            String entryID = entry.getID();
            xsltParams = new HashMap<String,String>();
            xsltParams.put("entry", entryID);
            stylesheetName = "_entry";
            outName = "entry-" + entryID;
            transformToHtml(def, targetDir, stylesheetName, outName, xsltParams);
         }
      }
   }

   private final void transformToHtml(LogDef def, File targetDir, String stylesheetName, String outName, Map<String,String> xsltParams)
   throws IOException {
      Source      source = new DOMSource(def.getXML());
      String    xsltPath = "log_to" + stylesheetName + "_html.xslt";
      String outFileName = outName + ".html";

      new Xformer(def).transformAndHandleExceptions(source, xsltPath, xsltParams, targetDir, outFileName);
   }
}
