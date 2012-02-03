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
import org.znerd.util.log.Limb;
import org.znerd.util.log.LogLevel;

/**
 * Code generator that transforms Logdoc input files to programming code.
 */
public final class CodeGenerator extends Generator {
    public CodeGenerator(File sourceDir, File destDir) {
        super(sourceDir, destDir);
    }

    @Override
    protected void generateImpl(LogDef logDef, File destDir) throws IOException {
        String packageName = logDef.getPackageName();
        String packagePath = packageName.replace(".", "/");
        File outDir = new File(destDir, packagePath);

        Limb.log(LogLevel.INFO, "Generating code.");
        Processor processor = new Processor(logDef, outDir);
        processor.process();
    }

    static class Processor {
        private final LogDef def;
        private final File outDir;

        Processor(LogDef logDef, File outDir) {
            this.def = logDef;
            this.outDir = outDir;
        }

        void process() throws IOException {
            transformToCode("Log");
            transformToCode("TranslationBundle");
            for (Map.Entry<String, Document> entry : def.getTranslations().entrySet()) {
                String locale = entry.getKey();
                Document translationXML = entry.getValue();
                transformToCodeForLocale(locale, translationXML);
            }
        }

        private void transformToCode(String className) throws IOException {
            final Source source = new DOMSource(def.getXML());
            final String xsltPath = "log_to_" + className + "_java" + ".xslt";
            final String outFileName = className + ".java";
            final String domainName = def.getDomainName();
            final String packageName = def.getPackageName();
            final String accessLevel = def.isPublic() ? "public" : "protected";

            final Map<String, String> xsltParams = new HashMap<String, String>();
            xsltParams.put("domain_name", domainName);
            xsltParams.put("package_name", packageName);
            xsltParams.put("accesslevel", accessLevel);

            new Xformer(def, "code/").transform(source, xsltPath, xsltParams, outDir, outFileName);
        }

        private void transformToCodeForLocale(String locale, Document translationXML) throws IOException {
            final Source source = new DOMSource(translationXML);
            final String xsltPath = "translation-bundle_to_java.xslt";
            final String outFileName = "TranslationBundle_" + locale + ".java";
            final String domainName = def.getDomainName();
            final String packageName = def.getPackageName();
            final String accesslevel = def.isPublic() ? "public" : "protected";

            final Map<String, String> xsltParams = new HashMap<String, String>();
            xsltParams.put("domain_name", domainName);
            xsltParams.put("package_name", packageName);
            xsltParams.put("accesslevel", accesslevel);
            xsltParams.put("locale", locale);

            new Xformer(def, "code/").transform(source, xsltPath, xsltParams, outDir, outFileName);
        }
    }
}
