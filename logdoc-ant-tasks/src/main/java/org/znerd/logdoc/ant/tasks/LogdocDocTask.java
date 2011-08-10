// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import java.io.IOException;

import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.gen.DocGenerator;

/**
 * An Apache Ant task for generating documentation from Logdoc definitions.
 */
public final class LogdocDocTask extends AbstractLogdocTask {

    @Override
    protected void executeImpl(LogDef def) throws IOException {
        DocGenerator generator = new DocGenerator(def, _destDir);
        generator.generate();
    }
}
