// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.znerd.logdoc.internal.Resolver;
import org.znerd.util.Preconditions;

/**
 * Log definition. Typically read from a <code>log.xml</code> file.
 */
public final class LogDef {

    private static final Schema LOG_SCHEMA;
    private static final Schema TRANSLATION_BUNDLE_SCHEMA;
    private static final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    
    private final File sourceDir;
    private final Resolver resolver;
    private final Document xml;
    private final String domainName;
    private final boolean publicLog;
    private final Map<String, Document> translations;
    private final List<Group> groups;

    private LogDef(File sourceDir) throws IOException, SAXException {
        Preconditions.checkArgument(sourceDir == null, "sourceDir == null");
        Preconditions.checkArgument(!sourceDir.isDirectory(), "Path (\"" + sourceDir.getPath() + "\") is not a directory.");

        this.sourceDir = sourceDir;
        
        // Create a resolver for the specified input directory
        resolver = new Resolver(sourceDir, "");

        // Load the log.xml file and validate it
        xml = validateXmlFileAgainstSchema(LOG_SCHEMA, "log.xml");

        // Parse the domain name and determine access level
        Element docElem = xml.getDocumentElement();
        domainName = docElem.getAttribute("domain");
        publicLog = Boolean.parseBoolean(docElem.getAttribute("public"));

        // Load the translation bundles
        translations = new HashMap<String, Document>();
        NodeList elems = docElem.getElementsByTagName("translation-bundle");
        for (int index = 0; index < elems.getLength(); index++) {
            Element elem = (Element) elems.item(index);
            String locale = elem.getAttribute("locale");

            Document tbXML = validateXmlFileAgainstSchema(TRANSLATION_BUNDLE_SCHEMA, "translation-bundle-" + locale + ".xml");
            translations.put(locale, tbXML);
        }

        groups = parseGroupsAndContainedEntries(docElem);
    }
    
    static {
        LOG_SCHEMA = loadSchema("log");
        TRANSLATION_BUNDLE_SCHEMA = loadSchema("translation-bundle");
    }

    private static Schema loadSchema(String name) {
        try {
            return loadSchemaImpl(name);
        } catch (Throwable cause) {
            throw new RuntimeException("Failed to load \"" + name + "\" schema.", cause);
        }
    }
    
    private static Schema loadSchemaImpl(String name) throws IOException, SAXException {
        Preconditions.checkArgument(name == null, "name == null");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        Source xsdSource = createXsdSource(name);
        return schemaFactory.newSchema(xsdSource);
    }
    
    private static Source createXsdSource(String name) throws IOException {
        String xsdPath = "xsd/" + name + ".xsd";
        InputStream xsdStream = Library.getMetaResourceAsStream(xsdPath);
        Source xsdSource = new StreamSource(xsdStream);
        return xsdSource;
    }

    private final List<Group> parseGroupsAndContainedEntries(Element element) {
        List<Group> groups = new ArrayList<Group>();
    
        NodeList children = element.getChildNodes();
        int childCount = (children == null) ? 0 : children.getLength();
    
        for (int i = 0; i < childCount; i++) {
            Node childNode = children.item(i);
    
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;
                if ("group".equals(childElement.getTagName())) {
                    Group group = new Group();
                    group._id = childElement.getAttribute("id");
                    group._name = childElement.getAttribute("name");
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
        int childCount = (children == null) ? 0 : children.getLength();
    
        for (int i = 0; i < childCount; i++) {
            Node childNode = children.item(i);
    
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;
                if ("entry".equals(childElement.getTagName())) {
                    Entry entry = new Entry();
                    entry._id = childElement.getAttribute("id");
    
                    entries.add(entry);
                }
            }
        }
    
        return entries;
    }

    public static final LogDef loadFromDirectory(File dir) throws IllegalArgumentException, IOException, SAXException {
        return new LogDef(dir);
    }

    private Document validateXmlFileAgainstSchema(Schema schema, String fileName) throws IOException, SAXException {
        Document document = resolver.loadInputDocument(fileName);
        validateXmlFileAgainstSchema(schema, fileName, document);
        return document;
    }
    
    private void validateXmlFileAgainstSchema(Schema schema, String fileName, Document document) throws IOException, SAXException {
        Preconditions.checkArgument(schema == null, "schema == null");
        Preconditions.checkArgument(document == null, "document == null");

        Validator validator = schema.newValidator();
        DOMSource source = new DOMSource(document);
        try {
            validator.validate(source);
        } catch (SAXException cause) {
            throw new IOException("Failed to validate " + fileName + " against XSD.", cause);
        }
    }

    public final File getSourceDir() {
        return sourceDir;
    }

    public Resolver createResolver(String basePath) {
        return new Resolver(sourceDir, basePath);
    }

    public final Document getXML() {
        return xml;
    }

    public final String getDomainName() {
        return domainName;
    }

    public final boolean isPublic() {
        return publicLog;
    }

    public final Map<String, Document> getTranslations() {
        return translations;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public class Group {
        String _id;
        String _name;
        List<Entry> _entries;

        public String getID() {
            return _id;
        }

        public String getName() {
            return _name;
        }

        public List<Entry> getEntries() {
            return _entries;
        }
    }

    public class Entry {
        String _id;

        public String getID() {
            return _id;
        }
    }
}
