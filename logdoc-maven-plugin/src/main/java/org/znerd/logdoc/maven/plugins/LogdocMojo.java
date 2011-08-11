// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.znerd.logdoc.gen.CodeGenerator;
import org.znerd.logdoc.gen.DocGenerator;
import org.znerd.logdoc.gen.Generator;
import org.znerd.logdoc.internal.InternalLogging;
import org.znerd.logdoc.maven.plugins.internal.MavenInternalLogging;

/**
 * An Maven plugin for generating source files and/or documentation from Logdoc definitions.
 * 
 * @goal java
 * @phase generate-sources
 */
public class LogdocMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException {
        sendInternalLoggingThroughMaven();
        generate();
        markGeneratedSourcesForCompilation();
    }

    private void sendInternalLoggingThroughMaven() {
        InternalLogging.setLogger(new MavenInternalLogging(getLog()));
    }

    private void generate() throws MojoExecutionException {
        generate(new CodeGenerator(_sourceDir, _destDir));
        generate(new DocGenerator(_sourceDir, _docsDestDir));
    }
    
    private void generate(Generator generator) throws MojoExecutionException {
        try {
            generator.generate();
        } catch (IOException cause) {
            throw new MojoExecutionException("Failed to perform transformation", cause);
        }
    }

    private void markGeneratedSourcesForCompilation() {
        _project.addCompileSourceRoot(_destDir.getAbsolutePath());
    }

    /**
     * @parameter name="project" default-value="${project}"
     * @readonly
     * @required
     */
    private MavenProject _project;

    /**
     * @parameter name="in" expression="${basedir}/src/logdoc"
     * @required
     */
    private File _sourceDir;

    /**
     * @parameter name="out" expression="${basedir}/target/generated-sources/logdoc"
     * @required
     */
    private File _destDir;
    
    /**
     * @parameter name="docsOut" expression="${basedir}/target/logdoc-html"
     * @required
     */
     private File _docsDestDir;
}
