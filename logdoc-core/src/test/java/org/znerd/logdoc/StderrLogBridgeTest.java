// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.znerd.util.log.LogLevel;

public class StderrLogBridgeTest {

    @Test
    public void testPutContextId() {
        StderrLogBridge logBridge = new StderrLogBridge();
        String contextId = "blablabla-123146";
        logBridge.putContextId(contextId);
        assertEquals(contextId, logBridge.getContextId());
    }

    @Test
    public void testGetContextIdWithoutPut() {
        StderrLogBridge logBridge = new StderrLogBridge();
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testUnputContextIdWithoutPut() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.unputContextId();
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testUnputContextIdAfterPut() {
        StderrLogBridge logBridge = new StderrLogBridge();
        String contextId = "blablabla-902119";
        logBridge.putContextId(contextId);
        logBridge.unputContextId();
        assertNull(logBridge.getContextId());
    }

    @Test
    public void testDefaultLevelIsDebug() {
        StderrLogBridge logBridge = new StderrLogBridge();
        assertEquals(LogLevel.DEBUG, logBridge.getLevel());
    }

    @Test
    public void testShouldLogAtDebug() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.DEBUG);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtInfo() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.INFO);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtNotice() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.NOTICE);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtWarning() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.WARNING);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtError() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.ERROR);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }

    @Test
    public void testShouldLogAtFatal() {
        StderrLogBridge logBridge = new StderrLogBridge();
        logBridge.setLevel(LogLevel.FATAL);

        final String domain = "org.znerd";
        final String groupId = "sample";
        final String entryId = "9011";
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.DEBUG));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.INFO));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.NOTICE));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.WARNING));
        assertFalse(logBridge.shouldLog(domain, groupId, entryId, LogLevel.ERROR));
        assertTrue(logBridge.shouldLog(domain, groupId, entryId, LogLevel.FATAL));
    }
}
