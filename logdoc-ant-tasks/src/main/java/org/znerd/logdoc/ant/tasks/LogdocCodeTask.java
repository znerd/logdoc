// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import java.io.File;

import org.znerd.logdoc.LoggingFramework;
import org.znerd.logdoc.gen.CodeGenerator;
import org.znerd.logdoc.gen.Generator;
import org.znerd.util.text.TextUtils;

/**
 * An Apache Ant task for generating source files from Logdoc definitions.
 */
public final class LogdocCodeTask extends AbstractLogdocTask {
    @Override
    protected Generator createGenerator(File sourceDir, File destDir) {
        return new CodeGenerator(sourceDir, destDir, _loggingFramework);
    }
    
    public void setLoggingFramework(String loggingFramework) {
        if (TextUtils.isEmpty(loggingFramework)) {
            throw new IllegalArgumentException("loggingFramework is empty");
        }
        _loggingFramework = LoggingFramework.valueOf(loggingFramework.trim().toUpperCase());
    }
    
    private LoggingFramework _loggingFramework = LoggingFramework.LOG4J;
}