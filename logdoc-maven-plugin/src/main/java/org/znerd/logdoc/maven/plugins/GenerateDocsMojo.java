// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.znerd.logdoc.gen.DocsGenerator;

/**
 * A Maven plugin for generating documentation from Logdoc definitions.
 * 
 * @goal docs
 */
public class GenerateDocsMojo extends AbstractLogdocMojo {

    /**
     * @parameter expression="${basedir}/src/main/logdoc"
     * @required
     */
    private File in;

    /**
     * @parameter expression="${basedir}/target/generated-site/logdoc"
     * @required
     */
    private File out;

    @Override
    public void execute() throws MojoExecutionException {
        sendInternalLoggingThroughMaven();
        generate(new DocsGenerator(in, out));
    }
}
