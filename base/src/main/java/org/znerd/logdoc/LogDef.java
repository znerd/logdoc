// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

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

/**
 * Log definition. Typically read from a <code>log.xml</code> file.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LogDef {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The <code>Schema</code> for validating log XML files.
    */
   private static final Schema LOG_SCHEMA;

   /**
    * The <code>Schema</code> for validating translation bundle XML files.
    */
   private static final Schema TB_SCHEMA;


   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Initializes this class.
    */
   static {
      String schemaName = "log";
      try {
         LOG_SCHEMA = loadSchema(schemaName);

         schemaName = "translation-bundle";
         TB_SCHEMA = loadSchema(schemaName);
      } catch (Throwable cause) {
         throw new Error("Failed to load LogDef class, because \"" + schemaName + "\" schema could not be loaded.", cause);
      }
   }

   /**
    * Loads a <code>Schema</code>.
    *
    * @return
    *    the loaded {@link Schema}, never <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>name == null</code>.
    *
    * @throws IOException
    *    if the schema could not be loaded due to an I/O error.
    *
    * @throws SAXException
    *    if the schema could not be loaded.
    */
   private static Schema loadSchema(String name)
   throws IllegalArgumentException, IOException, SAXException {

      // Check preconditions
      if (name == null) {
         throw new IllegalArgumentException("name == null");
      }

      // We need a factory first
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      // Create a Source for the XSD file
      String        xsdPath = "xsd/" + name + ".xsd";
      InputStream xsdStream = Library.getMetaResourceAsStream(xsdPath);
      Source      xsdSource = new StreamSource(xsdStream);

      return factory.newSchema(xsdSource);
   }

   /**
    * Validates the specified XML document against the specified schema.
    *
    * @param schema
    *    the {@link Schema} to validate against, cannot be <code>null</code>.
    *
    * @param document
    *    the XML {@link Document} to validate, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>schema == null || document == null</code>.
    *
    * @throws IOException
    *    in case of an I/O error.
    *
    * @throws SAXException
    *    in case the validation encounters an issue.
    */
   private static void validate(Schema schema, Document document)
   throws IllegalArgumentException, IOException, SAXException {

      // Check preconditions
      if (schema == null) {
         throw new IllegalArgumentException("schema == null");
      } else if (document == null) {
         throw new IllegalArgumentException("document == null");
      }

      // Validate
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
   }

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
    *
    * @throws SAXException
    *    if definition(s) could not be validated successfully.
    */
   public static final LogDef loadFromDirectory(File dir)
   throws IllegalArgumentException, IOException, SAXException {
      return new LogDef(dir);
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogDef</code>.
    *
    * @param dir
    *    the directory to load the log definition from,
    *    cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>dir == null</code>.
    *
    * @throws IOException
    *    if the definition(s) could not be loaded.
    *
    * @throws SAXException
    *    if definition(s) could not be validated successfully.
    */
   private LogDef(File dir)
   throws IllegalArgumentException, IOException, SAXException {

      // Check preconditions
      if (dir == null) {
         throw new IllegalArgumentException("dir == null");
      } else if (! dir.isDirectory()) {
         throw new IOException("Path (\"" + dir.getPath() + "\") is not a directory.");
      }
      
      // Create a resolver for the specified input directory
      _resolver = new Resolver(dir);
      
      // Load the log.xml file and validate it
      _xml = _resolver.loadInputDocument("log.xml");
      validate(LOG_SCHEMA, _xml);
      
      // Parse the domain name and determine access level
      Element docElem = _xml.getDocumentElement();
      _domainName     = docElem.getAttribute("domain");
      _public         = Boolean.parseBoolean(docElem.getAttribute("public"));
            
      // Load the translation bundles
      _translations = new HashMap<String,Document>();
      NodeList elems = docElem.getElementsByTagName("translation-bundle");
      for (int index = 0; index < elems.getLength(); index++) {
         Element elem = (Element) elems.item(index);
         String locale = elem.getAttribute("locale");
 
         Document tbXML = _resolver.loadInputDocument("translation-bundle-" + locale + ".xml");
         validate(TB_SCHEMA, tbXML);
         _translations.put(locale, tbXML);
      }

      // Parse the groups and entries
      _groups = parseGroups(docElem);
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The resolver that can resolve input files and XSLT files.
    * Never <code>null</code>.
    */
   private final Resolver _resolver;

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

   /**
    * The groups in this log definition. Never <code>null</code>.
    */
   private final List<Group> _groups;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   public final String getDomainName() {
      return _domainName;
   }

   public final boolean isPublic() {
      return _public;
   }

   public final Map<String,Document> getTranslations() {
      return _translations;
   }

   public final Document getXML() {
      return _xml;
   }

   private final List<Group> parseGroups(Element element) {
      List<Group> groups = new ArrayList<Group>();

      NodeList children = element.getChildNodes();
      int    childCount = (children == null) ? 0 : children.getLength();

      for (int i = 0; i < childCount; i++) {
         Node childNode = children.item(i);

         if (childNode instanceof Element) {
            Element childElement = (Element) childNode;
            if ("group".equals(childElement.getTagName())) {
               Group group    = new Group();
               group._id      = childElement.getAttribute("id");
               group._name    = childElement.getAttribute("name");
               group._entries = parseEntries(childElement);

               groups.add(group);
            }
         }
      }

      return groups;
   }

   private final List<Entry> parseEntries(Element element) {
      List<Entry> entries = new ArrayList<Entry>();

      NodeList children = element.getChildNodes();
      int    childCount = (children == null) ? 0 : children.getLength();

      for (int i = 0; i < childCount; i++) {
         Node childNode = children.item(i);

         if (childNode instanceof Element) {
            Element childElement = (Element) childNode;
            if ("entry".equals(childElement.getTagName())) {
               Entry entry = new Entry();
               entry._id   = childElement.getAttribute("id");

               entries.add(entry);
            }
         }
      }

      return entries;
   }

   /**
    * Generates the HTML documentation for this log definition.
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
   public void generateHtml(File targetDir)
   throws IllegalArgumentException, IOException {

      // Check preconditions
      if (targetDir == null) {
         throw new IllegalArgumentException("targetDir == null");
      }

      transformToHtml(targetDir, "",      "index"     , new HashMap<String,String>());
      transformToHtml(targetDir, "_list", "entry-list", new HashMap<String,String>());

      for (Group group : _groups) {
         String groupID = group._id;
         Map<String,String> xsltParams = new HashMap<String,String>();
         xsltParams.put("group", groupID);
         String stylesheetName = "_group";
         String outName = "group-" + groupID;
         transformToHtml(targetDir, stylesheetName, outName, xsltParams);

         for (Entry entry : group._entries) {
            String entryID = entry._id;
            xsltParams = new HashMap<String,String>();
            xsltParams.put("entry", entryID);
            stylesheetName = "_entry";
            outName = "entry-" + entryID;
            transformToHtml(targetDir, stylesheetName, outName, xsltParams);
         }
      }
   }

   private final void transformToHtml(File targetDir, String stylesheetName, String outName, Map<String,String> xsltParams)
   throws IOException {
      Source      source = getSource();
      String    xsltPath = "log_to" + stylesheetName + "_html.xslt";
      String outFileName = outName + ".html";

      new Xformer(_resolver).transformAndHandleExceptions(source, xsltPath, xsltParams, targetDir, outFileName);
   }

   private Source getSource() {
      return new DOMSource(_xml);
   }
   
   private Source getTranslationBundleSource(String locale) {
	   return new DOMSource(_translations.get(locale));
   }

   private class Group {
      String _id;
      String _name;
      List<Entry> _entries;
   }

   private class Entry {
      String _id;
   }
}
