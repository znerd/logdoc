// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LibraryLogBridgeTest {

    @Before @After
    public void reset() {
        Library.resetLogBridge();
    }

    @Test
    public void testDefaultLogBridgeNotNull() {
        assertNotNull(Library.getLogBridge());
    }
    

    @Test
    public void testDefaultLogBridgeGoesToJul() {
        assertTrue(Library.getLogBridge() instanceof JulLogBridge);
    }

    @Test
    public void testSetLogBridge() {
        LogBridge logBridge = new UnsopLogBridge();
        Library.setLogBridge(logBridge);
        assertEquals(logBridge, Library.getLogBridge());
    }
}
