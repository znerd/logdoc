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

/**
 * Code generator. Transforms Logdoc input files to programming code.
 */
public final class CodeGenerator extends Generator {

    public CodeGenerator(File sourceDir, File destDir, boolean overwrite) throws IllegalArgumentException {
        super(sourceDir, destDir, overwrite);
    }

    private final String _target = "log4j";

    @Override
    protected void generateImpl(LogDef logDef, File destDir, boolean overwrite) throws IOException {

        String domainPath = logDef.getDomainName().replace(".", "/");
        File outDir = new File(destDir, domainPath);

        transformToCode(logDef, outDir, _target + '/', "Log");
        transformToCode(logDef, outDir, "", "TranslationBundle");
        for (Map.Entry<String, Document> entry : logDef.getTranslations().entrySet()) {
            String locale = entry.getKey();
            Document translationXML = entry.getValue();
            transformToCodeForLocale(logDef, outDir, locale, translationXML);
        }
    }

    private void transformToCode(LogDef logDef, File outDir, String xsltSubDir, String className) throws IOException {

        final Source source = new DOMSource(logDef.getXML());
        final String xsltPath = xsltSubDir + "log_to_" + className + "_java" + ".xslt";
        final String outFileName = className + ".java";
        final String domainName = logDef.getDomainName();
        final String accessLevel = logDef.isPublic() ? "public" : "protected";

        final Map<String, String> xsltParams = new HashMap<String, String>();
        xsltParams.put("package_name", domainName);
        xsltParams.put("accesslevel", accessLevel);

        new Xformer(logDef).transform(source, xsltPath, xsltParams, outDir, outFileName);
    }

    private void transformToCodeForLocale(LogDef logDef, File outDir, String locale, Document translationXML) throws IOException {

        final Source source = new DOMSource(translationXML);
        final String xsltPath = "translation-bundle_to_java.xslt";
        final String outFileName = "TranslationBundle_" + locale + ".java";
        final String domainName = logDef.getDomainName();
        final String accesslevel = logDef.isPublic() ? "public" : "protected";

        final Map<String, String> xsltParams = new HashMap<String, String>();
        xsltParams.put("package_name", domainName);
        xsltParams.put("accesslevel", accesslevel);
        xsltParams.put("locale", locale);

        new Xformer(logDef).transform(source, xsltPath, xsltParams, outDir, outFileName);
    }
}
