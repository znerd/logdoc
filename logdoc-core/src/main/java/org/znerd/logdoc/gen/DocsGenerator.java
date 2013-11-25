// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.znerd.logdoc.Library;
import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.NoSuchResourceException;

/**
 * Logdoc documentation generator.
 */
public final class DocsGenerator extends Generator {

    public DocsGenerator(File sourceDir, File destDir) throws IllegalArgumentException {
        super(sourceDir, destDir);
    }

    @Override
    protected void generateImpl(LogDef logDef, File destDir) throws IOException {
        Processor proc = new Processor(logDef, destDir);
        proc.process();
    }

    private static class Processor {

        Processor(LogDef logDef, File destDir) {
            _def = logDef;
            _destDir = destDir;
        }

        private final LogDef _def;
        private final File _destDir;

        void process() throws IOException {
            generateOverviewDoc();
            generateEntryListDoc();
            generateGroupAndEntryDocs();
            generateCssFile();
        }

        private final void generateOverviewDoc() throws IOException {
            Map<String, String> xsltParams = new HashMap<String, String>();
            xsltParams.put("package_name", _def.getDomainName());
            transformToDoc("", "index", xsltParams);
        }

        private final void generateEntryListDoc() throws IOException {
            Map<String, String> xsltParams = new HashMap<String, String>();
            transformToDoc("_list", "entry-list", xsltParams);
        }

        private final void generateGroupAndEntryDocs() throws IOException {
            for (LogDef.Group group : _def.getGroups()) {
                String groupID = group.getID();
                Map<String, String> xsltParams = new HashMap<String, String>();
                xsltParams.put("group", groupID);
                String stylesheetName = "_group";
                String outName = "group-" + groupID;
                transformToDoc(stylesheetName, outName, xsltParams);

                transformEntries(group);
            }
        }

        private final void transformEntries(LogDef.Group group) throws IOException {
            for (LogDef.Entry entry : group.getEntries()) {
                String entryID = entry.getID();
                Map<String, String> xsltParams = new HashMap<String, String>();
                xsltParams.put("package_name", _def.getDomainName());
                xsltParams.put("sourcedir", _def.getSourceDir().getPath());
                xsltParams.put("entry", entryID);
                String stylesheetName = "_entry";
                String outName = "entry-" + entryID;
                transformToDoc(stylesheetName, outName, xsltParams);
            }
        }

        private final void transformToDoc(String stylesheetName, String outName, Map<String, String> xsltParams) throws IOException {
            Source source = new DOMSource(_def.getXML());
            String xsltPath = "log_to" + stylesheetName + "_html.xslt";
            String outFileName = outName + ".html";

            Resolver resolver = _def.createResolver("docs/");
            new Xformer(resolver).transform(source, xsltPath, xsltParams, _destDir, outFileName);
        }

        private final void generateCssFile() throws NoSuchResourceException, IOException {
            String fileName = "style.css";
            InputStream inStream = createCssInputStream(fileName);
            FileOutputStream outStream = createCssOutputStream(fileName);
            writeCssToStream(fileName, inStream, outStream);
        }

        private InputStream createCssInputStream(String fileName) throws IOException {
            InputStream inStream = Library.getMetaResourceAsStream("css/" + fileName);
            return inStream;
        }

        private FileOutputStream createCssOutputStream(String fileName) throws IOException {
            File outFile = new File(_destDir, fileName);
            try {
                return new FileOutputStream(outFile);
            } catch (IOException cause) {
                throw new IOException("Failed to open file \"" + fileName + "\" in output directory \"" + _destDir.getAbsolutePath() + "\".");
            }
        }

        private void writeCssToStream(String fileName, InputStream inStream, FileOutputStream outStream) throws IOException {
            try {
                copy(inStream, outStream);
            } catch (IOException cause) {
                throw new IOException("Failed to write \"" + fileName + "\" to output directory \"" + _destDir.getAbsolutePath() + "\".");
            }
        }

        private void copy(InputStream input, OutputStream output) throws IOException {
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
    }
}
