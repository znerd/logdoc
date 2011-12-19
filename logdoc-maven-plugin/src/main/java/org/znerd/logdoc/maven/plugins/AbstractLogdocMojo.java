// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.znerd.logdoc.gen.Generator;
import org.znerd.util.log.Limb;
import org.znerd.util.log.MavenLimb;

abstract class AbstractLogdocMojo extends AbstractMojo {

    protected AbstractLogdocMojo() {
    }

    protected final void sendInternalLoggingThroughMaven() {
        Limb.setLogger(new MavenLimb(getLog()));
    }

    protected final void generate(Generator generator) throws MojoExecutionException {
        try {
            generator.generate();
        } catch (IOException cause) {
            throw new MojoExecutionException("Failed to perform transformation", cause);
        }
    }
}
