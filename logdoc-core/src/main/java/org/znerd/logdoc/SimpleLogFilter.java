// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Log filter that always returns the original parameter value. This filter provides maximum information and minimum security.
 */
public final class SimpleLogFilter extends LogFilter {

    public SimpleLogFilter() {
    }

    @Override
    public String filter(String logger, String param, String value) {
        return value;
    }

    @Override
    public Object filter(String logger, String param, Object value) {
        return value;
    }
}
