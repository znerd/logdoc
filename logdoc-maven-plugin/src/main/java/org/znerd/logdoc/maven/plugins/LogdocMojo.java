// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import org.znerd.logdoc.LogFramework;
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
        generate(new CodeGenerator(_sourceDir, _codeTargetDir, loggingFrameworkEnum()));
        generate(new DocsGenerator(_sourceDir, _docsTargetDir));
    }

    private void generate(Generator generator) throws MojoExecutionException {
        try {
            generator.generate();
        } catch (IOException cause) {
            throw new MojoExecutionException("Failed to perform transformation", cause);
        }
    }
    
    private LogFramework loggingFrameworkEnum() {
        return LogFramework.valueOf(_loggingFramework.toUpperCase());
    }

    private void markGeneratedSourcesForCompilation() {
        _project.addCompileSourceRoot(_codeTargetDir.getAbsolutePath());
    }

    /**
     * @parameter alias="project" default-value="${project}"
     * @readonly
     * @required
     */
    private MavenProject _project;

    /**
     * @parameter alias="in" expression="${basedir}/src/main/logdoc"
     * @required
     */
    private File _sourceDir;

    /**
     * @parameter alias="out" expression="${basedir}/target/site/logdoc"
     * @required
     */
    private File _codeTargetDir;

    /**
     * @parameter alias="docsOut" expression="${basedir}/target/logdoc-html"
     * @required
     */
    private File _docsTargetDir;
    
    /**
     * @parameter alias="loggingFramework" expression="log4j"
     * @required
     */
    private String _loggingFramework;
}
