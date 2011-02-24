// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import org.znerd.logdoc.internal.InternalLogging;
import org.znerd.logdoc.internal.LogCentral;

/**
 * Unit tests for the <code>Library</code> class.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
public final class LibraryTest {

   /**
    * Tests that the name of the library is "Logdoc".
    */
   @Test
   public void testName() {
      assertEquals("Logdoc", Library.getName());
   }

   /**
    * Tests that the version of the library is valid.
    */
   @Test
   @Ignore
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

   /**
    * Tests modification and retrieval of the <em>locale</em> setting in the 
    * <code>Library</code> class.
    */
   @Test
   public void testLocale() {

      // Test the DEFAULT_LOCALE class field
      assertNotNull("Library.DEFAULT_LOCALE == null",     Library.DEFAULT_LOCALE);
      assertFalse  ("Library.DEFAULT_LOCALE length == 0", Library.DEFAULT_LOCALE.length() < 1);
      assertTrue   ("Invalid value for DEFAULT_LOCALE: \"" + Library.DEFAULT_LOCALE + "\".", Library.DEFAULT_LOCALE.matches("^[a-z]{2}(_[A-Z]{2})?$"));

      // Test the initial value of the <locale>
      String initialLocale = Library.getLocale();
      assertNotNull("getLocale() == null", initialLocale);
      assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());

      // Try changing the locale
      String newLocale = "nl_NL";
      try {
         Library.setLocale(newLocale);
         assertEquals(newLocale, Library.getLocale());
      } finally {
         Library.useDefaultLocale();
      }

      // Make sure the default locale is set again
      // now that we called useDefaultLocale()
      assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());

      // Try setting a null locale (must fail)
      try {
         Library.setLocale(null);
         fail("Expected setLocale(null) to throw an IllegalArgumentException.");
      } catch (IllegalArgumentException e) {
         // as expected
      }

      // Make sure locale is indeed not changed to null
      assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());
   }
}
