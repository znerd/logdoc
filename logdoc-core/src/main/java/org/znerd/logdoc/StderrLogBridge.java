// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.PrintWriter;

public final class StderrLogBridge extends PrintWriterLogBridge {
    public StderrLogBridge() {
        super(new PrintWriter(System.err));
    }
}
