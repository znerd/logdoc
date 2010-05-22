// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import static org.junit.Assert.*;

import org.znerd.logdoc.internal.InternalLogging;
import org.znerd.logdoc.internal.LogCentral;

/**
 * Tests the <code>Library</code> class.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LibraryTest {

   @Test
   public void testLocale() {
      String initialLocale = Library.getLocale();
      assertNotNull("getLocale() == null", initialLocale);
   }
}
