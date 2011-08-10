// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import java.io.IOException;

import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.gen.CodeGenerator;

/**
 * An Apache Ant task for generating source files from Logdoc definitions.
 */
public final class LogdocCodeTask extends AbstractLogdocTask {

    public void setTarget(String newTarget) {
        _target = newTarget;
    }

    private String _target = DEFAULT_TARGET;
    private static final String DEFAULT_TARGET = "log4j";

    @Override
    protected void executeImpl(LogDef def) throws IOException {
        CodeGenerator generator = new CodeGenerator(def, _target, _destDir);
        generator.generate();
    }
}
