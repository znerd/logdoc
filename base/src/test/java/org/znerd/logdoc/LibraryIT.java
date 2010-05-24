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
 * Integration tests for the <code>Library</code> class.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LibraryIT {

   /**
    * Tests that the version of the library is valid.
    */
   @Test
   public void testVersion() {

      // Library version not null and not empty
      assertNotNull("Library.getVersion() == null", Library.getVersion());
      assertTrue("Library.getVersion() has length 0.", Library.getVersion().length() > 0);

      // First test the pattern
      String pattern = "^0|[1-9][0-9]*(\\.0|[1-9][0-9]*)(-SNAPSHOT)?$";
      assertTrue("0".matches(pattern));
      assertTrue("0.0".matches(pattern));
      assertTrue("1.0".matches(pattern));
      assertTrue("11.0".matches(pattern));
      assertTrue("11.1.2.3.4.5.6.7.8.9".matches(pattern));
      assertTrue("3.4.5.6.79-SNAPSHOT".matches(pattern));
      assertFalse("a0".matches(pattern));
      assertFalse("0a".matches(pattern));
      assertFalse("11.00.1.2".matches(pattern));

      // ...then actually use the pattern to test the version string
      assertTrue(Library.getVersion().matches("^0|[1-9][0-9]*(\\.0|[1-9][0-9]*)(-SNAPSHOT)?$"));
   }
}
