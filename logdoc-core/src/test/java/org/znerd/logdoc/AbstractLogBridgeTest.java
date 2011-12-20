// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.znerd.util.log.LogLevel;

public abstract class AbstractLogBridgeTest {

    private static final String DOMAIN = "org.znerd";
    private static final String GROUP_ID = "sample";
    private static final String ENTRY_ID = "9011";
    
    private LogBridge logBridge;

    @Before
    public final void initLogBridge() throws Exception {
        logBridge = provideLogBridge();
    }

    protected abstract LogBridge provideLogBridge();

    protected LogBridge getLogBridge() {
        return logBridge;
    }

    @Test
    public void testPutContextId() {
        String contextId = "blablabla-123146";
        try {
            logBridge.putContextId(contextId);
            assertEquals(contextId, logBridge.getContextId());
        } finally {
            logBridge.unputContextId();
        }
    }

    @Test
    public void testGetContextIdWithoutPut() {
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testUnputContextIdWithoutPut() {
        logBridge.unputContextId();
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testUnputContextIdAfterPut() {
        String contextId = "blablabla-902119";
        logBridge.putContextId(contextId);
        logBridge.unputContextId();
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testSetLevelShouldNotAcceptNull() {
        boolean caughtExpectedError = false;
        try {
            logBridge.setLevel(null);
        } catch (IllegalArgumentException cause) {
            caughtExpectedError = true;
        }
        assertTrue(caughtExpectedError);
    }

    @Test
    public void testShouldLogAtDebug() {
        logBridge.setLevel(LogLevel.DEBUG);
        assertEquals(LogLevel.DEBUG, logBridge.getLevel());
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtInfo() {
        logBridge.setLevel(LogLevel.INFO);
        assertEquals(LogLevel.INFO, logBridge.getLevel());
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtNotice() {
        logBridge.setLevel(LogLevel.NOTICE);
        assertEquals(LogLevel.NOTICE, logBridge.getLevel());
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtWarning() {
        logBridge.setLevel(LogLevel.WARNING);
        assertEquals(LogLevel.WARNING, logBridge.getLevel());
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtError() {
        logBridge.setLevel(LogLevel.ERROR);
        assertEquals(LogLevel.ERROR, logBridge.getLevel());
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtFatal() {
        logBridge.setLevel(LogLevel.FATAL);
        assertEquals(LogLevel.FATAL, logBridge.getLevel());
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.NOTICE));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.WARNING));
        assertFalse(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(DOMAIN, GROUP_ID, ENTRY_ID, LogLevel.FATAL));
    }
}
