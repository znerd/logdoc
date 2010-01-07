// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

/**
 * Log filter that always returns the original parameter value. This filter
 * provides no security at all.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class SimpleLogFilter extends LogFilter {

   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>SimpleLogFilter</code>.
    */
   public SimpleLogFilter() {
      // empty
   }

   
   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

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
