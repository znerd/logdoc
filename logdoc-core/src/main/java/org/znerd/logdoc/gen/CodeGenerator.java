// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.gen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.LoggingFramework;
import org.znerd.util.log.Limb;
import org.znerd.util.log.LogLevel;

/**
 * Code generator. Transforms Logdoc input files to programming code.
 */
public final class CodeGenerator extends Generator {

    public CodeGenerator(File sourceDir, File destDir, LoggingFramework loggingFramework) throws IllegalArgumentException {
        super(sourceDir, destDir);
        _loggingFramework = loggingFramework;
    }
    
    private final LoggingFramework _loggingFramework;

    @Override
    protected void generateImpl(LogDef logDef, File destDir) throws IOException {
        String domainPath = logDef.getDomainName().replace(".", "/");
        File outDir = new File(destDir, domainPath);

        Limb.log(LogLevel.INFO, "Generating code for " + _loggingFramework.name() + " logging framework.");
        Processor processor = new Processor(logDef, outDir, _loggingFramework);
        processor.process();
    }

    static class Processor {

        Processor(LogDef logDef, File outDir, LoggingFramework loggingFramework) {
            _def = logDef;
            _outDir = outDir;
            _target = loggingFramework.name().toLowerCase();
        }

        private final LogDef _def;
        private final File _outDir;
        private final String _target;

        void process() throws IOException {
            transformToCode(_target + '/', "Log");
            transformToCode("", "TranslationBundle");
            for (Map.Entry<String, Document> entry : _def.getTranslations().entrySet()) {
                String locale = entry.getKey();
                Document translationXML = entry.getValue();
                transformToCodeForLocale(locale, translationXML);
            }
        }

        private void transformToCode(String xsltSubDir, String className) throws IOException {
            final Source source = new DOMSource(_def.getXML());
            final String xsltPath = xsltSubDir + "log_to_" + className + "_java" + ".xslt";
            final String outFileName = className + ".java";
            final String domainName = _def.getDomainName();
            final String accessLevel = _def.isPublic() ? "public" : "protected";

            final Map<String, String> xsltParams = new HashMap<String, String>();
            xsltParams.put("package_name", domainName);
            xsltParams.put("accesslevel", accessLevel);

            new Xformer(_def).transform(source, xsltPath, xsltParams, _outDir, outFileName);
        }

        private void transformToCodeForLocale(String locale, Document translationXML) throws IOException {
            final Source source = new DOMSource(translationXML);
            final String xsltPath = "translation-bundle_to_java.xslt";
            final String outFileName = "TranslationBundle_" + locale + ".java";
            final String domainName = _def.getDomainName();
            final String accesslevel = _def.isPublic() ? "public" : "protected";

            final Map<String, String> xsltParams = new HashMap<String, String>();
            xsltParams.put("package_name", domainName);
            xsltParams.put("accesslevel", accesslevel);
            xsltParams.put("locale", locale);

            new Xformer(_def).transform(source, xsltPath, xsltParams, _outDir, outFileName);
        }
    }
}
