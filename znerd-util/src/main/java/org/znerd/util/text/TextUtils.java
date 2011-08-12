// See the COPYRIGHT file for copyright and license information
package org.znerd.util.text;

public class TextUtils {

    public static final String quote(Object o) {
        return o == null ? "(null)" : quote(o.toString());
    }

    private TextUtils() {
    }
}
