// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

public class TextUtils {

    public static final String quote(String s) {
        return s == null ? "(null)" : "\"" + s + '"';
    }

    public static final String quote(Object o) {
        return o == null ? "(null)" : quote(o.toString());
    }

    private TextUtils() {
    }
}
