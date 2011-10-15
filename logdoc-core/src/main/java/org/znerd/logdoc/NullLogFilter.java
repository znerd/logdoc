// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Log filter that always returns an empty string. This filter provides minimum information and maximum security.
 * <p>
 * An empty string is returned instead of plain <code>null</code>, to avoid any {@link NullPointerException} issues.
 */
public final class NullLogFilter extends LogFilter {

    public NullLogFilter() {
    }

    @Override
    public String filter(String logger, String param, String value) {
        return "";
    }

    @Override
    public Object filter(String logger, String param, Object value) {
        return "";
    }
}
