// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.znerd.logdoc.gen.CodeGenerator;
import org.znerd.logdoc.gen.DocsGenerator;
import org.znerd.logdoc.gen.Generator;
import org.znerd.util.log.Limb;
import org.znerd.util.log.MavenLimb;

/**
 * A Maven plugin for generating source files and/or documentation from Logdoc definitions.
 * 
 * @goal java
 * @phase generate-sources
 */
public class LogdocMojo extends AbstractMojo {

    /**
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
    private MavenProject project;

    /**
     * @parameter expression="${basedir}/src/main/logdoc"
     * @required
     */
    private File in;

    /**
     * @parameter expression="${basedir}/target/site/logdoc"
     * @required
     */
    private File out;

    /**
     * @parameter expression="${basedir}/target/logdoc-html"
     * @required
     */
    private File docsOut;

    @Override
    public void execute() throws MojoExecutionException {
        sendInternalLoggingThroughMaven();
        generate();
        markGeneratedSourcesForCompilation();
    }

    private void sendInternalLoggingThroughMaven() {
        Limb.setLogger(new MavenLimb(getLog()));
    }

    private void generate() throws MojoExecutionException {
        generate(new CodeGenerator(in, out));
        generate(new DocsGenerator(in, docsOut));
    }

    private void generate(Generator generator) throws MojoExecutionException {
        try {
            generator.generate();
        } catch (IOException cause) {
            throw new MojoExecutionException("Failed to perform transformation", cause);
        }
    }

    private void markGeneratedSourcesForCompilation() {
        project.addCompileSourceRoot(out.getAbsolutePath());
    }
}
