// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests modification and retrieval of the <em>locale</em> setting in the <code>Library</code> class.
 */
public final class LibraryLocaleTest {

    @Before
    public void setUp() {
        Library.useDefaultLocale();
    }

    @Test
    public void testChangeLocale() {
        String newLocale = "nl_NL";
        Library.setLocale(newLocale);
        assertEquals(newLocale, Library.getLocale());
        Library.useDefaultLocale();
        assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());
    }

    @Test
    public void testDefaultLocaleClassField() {
        assertNotNull("Library.DEFAULT_LOCALE == null", Library.DEFAULT_LOCALE);
        assertFalse("Library.DEFAULT_LOCALE length == 0", Library.DEFAULT_LOCALE.length() < 1);
        assertTrue("Invalid value for DEFAULT_LOCALE: \"" + Library.DEFAULT_LOCALE + "\".", Library.DEFAULT_LOCALE.matches("^[a-z]{2}(_[A-Z]{2})?$"));
    }

    @Test
    public void testInitialLocale() {
        String initialLocale = Library.getLocale();
        assertNotNull("getLocale() == null", initialLocale);
        assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());
    }

    @Test
    public void testSetLocaleToNull() {
        boolean caughtExpectedException = false;
        try {
            Library.setLocale(null);
        } catch (IllegalArgumentException e) {
            caughtExpectedException = true;
        }
        assertTrue("Expected setLocale(null) to throw an IllegalArgumentException.", caughtExpectedException);
        assertEquals(Library.DEFAULT_LOCALE, Library.getLocale());
    }
}
