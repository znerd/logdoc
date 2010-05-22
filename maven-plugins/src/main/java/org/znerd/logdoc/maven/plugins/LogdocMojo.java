// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Says "Hi" to the user.
 *
 * @goal generateLogdocSourceFiles
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public class LogdocMojo extends AbstractMojo {

   @Override
   public void execute() throws MojoExecutionException {
      getLog().info("Hello, world.");
   }
}

