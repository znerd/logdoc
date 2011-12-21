// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.znerd.logdoc.gen.Generator;
import org.znerd.util.log.Limb;
import org.znerd.util.log.MavenLimb;

class LogdocMojoSupport {

    public void sendInternalLoggingThroughMaven(Log log) {
        Limb.setLogger(new MavenLimb(log));
    }

    public void generate(Generator generator) throws MojoExecutionException {
        try {
            generator.generate();
        } catch (IOException cause) {
            throw new MojoExecutionException("Failed to process Logdoc definitions.", cause);
        }
    }
}
