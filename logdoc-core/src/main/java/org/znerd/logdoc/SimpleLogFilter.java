// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Log filter that always returns the original parameter value. This filter provides no security at all (but maximum information).
 */
public final class SimpleLogFilter extends LogFilter {

    /**
     * Constructs a new <code>SimpleLogFilter</code>.
     */
    public SimpleLogFilter() {
        // empty
    }

    @Override
    public String filter(String logger, String param, String value) throws IllegalArgumentException {
        return value;
    }

    @Override
    public Object filter(String logger, String param, Object value) throws IllegalArgumentException {
        return value;
    }
}
