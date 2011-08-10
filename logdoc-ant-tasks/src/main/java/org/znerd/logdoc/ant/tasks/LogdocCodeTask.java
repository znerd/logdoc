// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import java.io.File;

import org.znerd.logdoc.gen.CodeGenerator;
import org.znerd.logdoc.gen.Generator;

/**
 * An Apache Ant task for generating source files from Logdoc definitions.
 */
public final class LogdocCodeTask extends AbstractLogdocTask {
    @Override
    protected Generator createGenerator(File sourceDir, File destDir, boolean overwrite) {
        return new CodeGenerator(sourceDir, destDir, overwrite);
    }
}