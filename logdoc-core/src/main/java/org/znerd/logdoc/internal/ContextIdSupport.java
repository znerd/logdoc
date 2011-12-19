// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

public class ContextIdSupport {
    private final ThreadLocal<String> contextIdPerThread = new ThreadLocal<String>();

    public void putContextId(String newContextId) {
        contextIdPerThread.set(newContextId);
    }

    public void unputContextId() {
        contextIdPerThread.set(null);
    }

    public String getContextId() {
        return contextIdPerThread.get();
    }
}
