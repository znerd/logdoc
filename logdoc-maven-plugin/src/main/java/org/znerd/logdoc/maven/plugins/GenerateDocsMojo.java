// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.File;
import java.util.Locale;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.znerd.logdoc.gen.DocsGenerator;

/**
 * A Maven plugin for generating documentation from Logdoc definitions.
 * 
 * @goal logdoc-docs
 * @phase site
 */
public class GenerateDocsMojo extends AbstractMavenReport {

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     * @required
     * @readonly
     */
    private Renderer siteRenderer;

    /**
     * @parameter expression="${basedir}/src/main/logdoc"
     * @required
     */
    private File in;

    /**
     * @parameter expression="${basedir}/target/generated-site/logdoc"
     * @required
     */
    private String outputDirectory;
    
    private LogdocMojoSupport support = new LogdocMojoSupport();
    
    @Override
    public void execute() throws MojoExecutionException {
        support.sendInternalLoggingThroughMaven(getLog());
        File effectiveOutputDirectory = getReportOutputDirectory();
        support.generate(new DocsGenerator(in, effectiveOutputDirectory));
    }

    @Override
    public String getOutputName() {
        return "logdoc";
    }

    @Override
    public String getName(Locale locale) {
        return "Logdoc";
    }

    @Override
    public String getDescription(Locale locale) {
        return "Logdoc log documentation.";
    }

    @Override
    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    @Override
    protected String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    protected MavenProject getProject() {
        return project;
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        throw new Error();
    }

    @Override
    public boolean isExternalReport() {
        return false;
    }
}
