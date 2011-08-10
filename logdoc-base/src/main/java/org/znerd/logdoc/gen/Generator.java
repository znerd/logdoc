// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.gen;

import java.io.IOException;

/**
 * Abstract base class for generators.
 */
public abstract class Generator {
    
    public final void generate() throws IllegalArgumentException, IOException {
        generateImpl();
    }
    
    protected abstract void generateImpl() throws IOException;

    protected Generator() {
    }
}
