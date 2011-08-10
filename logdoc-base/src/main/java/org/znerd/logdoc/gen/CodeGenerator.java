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

    /**
     * Creates a new <code>CodeGenerator</code> for the specified log definition.
     * 
     * @param def the {@link LogDef} object, cannot be <code>null</code>.
     * @param target the target to generate code for, e.g. <code>"log4j"</code> or <code>"slf4j"</code>, cannot be <code>null</code>.
     * @param targetDir the target directory to create the Java source files in, cannot be <code>null</code>.
     * @throws IllegalArgumentException if <code>def == null || target == null || targetDir == null</code>.
     */
    public CodeGenerator(LogDef def, String target, File targetDir, boolean overwrite) throws IllegalArgumentException {
        if (def == null) {
            throw new IllegalArgumentException("def == null");
        } else if (target == null) {
            throw new IllegalArgumentException("target == null");
        } else if (targetDir == null) {
            throw new IllegalArgumentException("targetDir == null");
        }

        _def = def;
        _target = target;
        _targetDir = targetDir;
        _overwrite = overwrite;
    }

    private final LogDef _def;
    private final String _target;
    private final File _targetDir;
    private final boolean _overwrite;

    /**
     * Creates a new <code>CodeGenerator</code> for the specified log definition without overwriting the target files.
     * 
     * @param def the {@link LogDef} object, cannot be <code>null</code>.
     * @param target the target to generate code for, e.g. <code>"log4j"</code> or <code>"slf4j"</code>, cannot be <code>null</code>.
     * @param targetDir the target directory to create the Java source files in, cannot be <code>null</code>.
     * @throws IllegalArgumentException if <code>def == null || target == null || targetDir == null</code>.
     * @deprecated Use {@link CodeGenerator(LogDef,String,File,boolean)} instead.
     */
    @Deprecated
    public CodeGenerator(LogDef def, String target, File targetDir) throws IllegalArgumentException {
        this(def, target, targetDir, false);
    }

    @Override
    public void generateImpl() throws IOException {

        String domainPath = _def.getDomainName().replace(".", "/");
        File outDir = new File(_targetDir, domainPath);

        transformToCode(outDir, _target + '/', "Log");
        transformToCode(outDir, "", "TranslationBundle");
        for (Map.Entry<String, Document> entry : _def.getTranslations().entrySet()) {
            String locale = entry.getKey();
            Document translationXML = entry.getValue();
            transformToCodeForLocale(outDir, locale, translationXML);
        }
    }

    private void transformToCode(File outDir, String xsltSubDir, String className) throws IOException {

        final Source source = new DOMSource(_def.getXML());
        final String xsltPath = xsltSubDir + "log_to_" + className + "_java" + ".xslt";
        final String outFileName = className + ".java";
        final String domainName = _def.getDomainName();
        final String accessLevel = _def.isPublic() ? "public" : "protected";

        final Map<String, String> xsltParams = new HashMap<String, String>();
        xsltParams.put("package_name", domainName);
        xsltParams.put("accesslevel", accessLevel);

        new Xformer(_def).transform(source, xsltPath, xsltParams, outDir, outFileName);
    }

    private void transformToCodeForLocale(File outDir, String locale, Document translationXML) throws IOException {

        final Source source = new DOMSource(translationXML);
        final String xsltPath = "translation-bundle_to_java.xslt";
        final String outFileName = "TranslationBundle_" + locale + ".java";
        final String domainName = _def.getDomainName();
        final String accesslevel = _def.isPublic() ? "public" : "protected";

        final Map<String, String> xsltParams = new HashMap<String, String>();
        xsltParams.put("package_name", domainName);
        xsltParams.put("accesslevel", accesslevel);
        xsltParams.put("locale", locale);

        new Xformer(_def).transform(source, xsltPath, xsltParams, outDir, outFileName);
    }
}
