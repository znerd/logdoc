// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

public class TextUtils {
    
    /**
     * Returns a quoted version of the specified string, or <code>"(null)"</code> if the argument is <code>null</code>.
     * 
     * @param s the character string, can be <code>null</code>, e.g. <code>"foo bar"</code>.
     * @return the quoted string, e.g. <code>"\"foo bar\""</code>, or <code>"(null)"</code> if the argument is <code>null</code>.
     */
    public static final String quote(String s) {
        return s == null ? "(null)" : "\"" + s + '"';
    }

    /**
     * Returns a quoted version string representation, or <code>"(null)"</code> if the argument is <code>null</code>.
     * 
     * @param o the object, can be <code>null</code>.
     * @return the quoted string representation of the specified object, e.g. <code>"\"foo bar\""</code>, or <code>"(null)"</code> if the argument is <code>null</code>.
     */
    public static final String quote(Object o) {
        return o == null ? "(null)" : quote(o.toString());
    }

    private TextUtils() {
    }
}
