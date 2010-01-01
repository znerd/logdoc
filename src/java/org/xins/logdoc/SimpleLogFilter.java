// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

/**
 * Log filter that always returns the original parameter value. This filter
 * provides no security at all.
 *
 * @version $Revision: 9034 $ $Date: 2009-04-16 15:53:04 +0200 (do, 16 apr 2009) $
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 *
 * @since Logdoc 3.0
 */
public final class SimpleLogFilter extends LogFilter {

   /**
    * Constructs a new <code>SimpleLogFilter</code>.
    */
   public SimpleLogFilter() {
      // empty
   }

   @Override
   public String filter(String logger, String param, String value)
   throws IllegalArgumentException {
      return value;
   }

   @Override
   public Object filter(String logger, String param, Object value)
   throws IllegalArgumentException {
      return value;
   }
}
