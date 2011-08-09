// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

/**
 * Log filter that always returns an empty string. This filter provides
 * maximum security (and minimum information).
 *
 * <p>An empty string is returned instead of plain <code>null</code>, to avoid
 * any {@link NullPointerException} issues.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class NullLogFilter extends LogFilter {

   /**
    * Constructs a new <code>NullLogFilter</code>.
    */
   public NullLogFilter() {
      // empty
   }

   @Override
   public String filter(String logger, String param, String value)
   throws IllegalArgumentException {
      return "";
   }

   @Override
   public Object filter(String logger, String param, Object value)
   throws IllegalArgumentException {
      return "";
   }
}
