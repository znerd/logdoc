// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests that the version of the library is valid.
 */
public final class LibraryVersionTest {

    @Test
    @Ignore
    public void testVersion() {
        final String version = Library.getVersion();
        final String pattern = "^0|[1-9][0-9]*(\\.0|[1-9][0-9]*)(-SNAPSHOT)?$";

        assertVersionNotNullOrEmpty(version);
        validateVersionPattern(pattern);
        assertVersionMatchesPattern(version, pattern);
    }

    private void assertVersionNotNullOrEmpty(final String version) {
        assertNotNull("Library.getVersion() == null", version);
        assertTrue("Library.getVersion() has length 0.", version.length() > 0);
    }

    private void validateVersionPattern(final String pattern) {
        assertTrue("0".matches(pattern));
        assertTrue("0.0".matches(pattern));
        assertTrue("1.0".matches(pattern));
        assertTrue("11.0".matches(pattern));
        assertTrue("11.1.2.3.4.5.6.7.8.9".matches(pattern));
        assertTrue("3.4.5.6.79-SNAPSHOT".matches(pattern));
        assertFalse("a0".matches(pattern));
        assertFalse("0a".matches(pattern));
        assertFalse("11.00.1.2".matches(pattern));
    }

    private void assertVersionMatchesPattern(final String version, final String pattern) {
        assertTrue(version.matches(pattern));
    }
}
