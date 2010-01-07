// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

/**
 * Parameter value log filters. Implementations of this abstract class can be
 * used to modify parameters during logging.
 *
 * <p>A typical use is to filter out passwords so these will not be stored in
 * a logging store.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public abstract class LogFilter {
   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogFilter</code>.
    */
   protected LogFilter() {
      // empty
   }

   
   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Filters the specified <code>String</code> value.
    *
    * @param logger
    *    the name of the logger,
    *    for example <code>"org.xins.common.lowlevel.1050"</code>,
    *    cannot be <code>null</code>.
    *
    * @param param
    *    the name of the parameter,
    *    for example <code>"queryString"</code>,
    *    cannot be <code>null</code>.
    *
    * @param value
    *    the original parameter value, can be <code>null</code>.
    *
    * @return
    *    the parameter value to use, possibly modified,
    *    can be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>logger == null || param == null</code>.
    */
   public abstract String filter(String logger, String param, String value)
   throws IllegalArgumentException;

   /**
    * Filters the specified <code>Object</code> value.
    *
    * @param logger
    *    the name of the logger,
    *    for example <code>"org.xins.common.lowlevel.1050"</code>,
    *    cannot be <code>null</code>.
    *
    * @param param
    *    the name of the parameter,
    *    for example <code>"queryString"</code>,
    *    cannot be <code>null</code>.
    *
    * @param value
    *    the original parameter value, can be <code>null</code>.
    *
    * @return
    *    the parameter value to use, possibly modified,
    *    can be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>logger == null || param == null</code>.
    */
   public abstract Object filter(String logger, String param, Object value)
   throws IllegalArgumentException;
}
