// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.PrintWriter;

public class StderrLogBridge extends PrintWriterLogBridge {

    private static final StderrLogBridge SINGLETON_INSTANCE = new StderrLogBridge();

    private StderrLogBridge() {
        super(new PrintWriter(System.err));
    }

    public static final StderrLogBridge getInstance() {
        return SINGLETON_INSTANCE;
    }
}
