// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests that the name of the library is "Logdoc".
 */
public final class LibraryNameTest {
    @Test
    public void testName() {
        assertEquals("Logdoc", Library.getName());
    }
}

