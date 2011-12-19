// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.znerd.util.log.LogLevel;

public class PrintWriterLogBridgeTest {

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private PrintWriterLogBridge logBridge;

    @Before
    @After
    public void reset() throws Exception {
        final boolean writeImmediately = true;
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter, writeImmediately);
        logBridge = new PrintWriterLogBridge(printWriter);
    }

    @Test
    public void testPutContextId() {
        String contextId = "blablabla-123146";
        logBridge.putContextId(contextId);
        assertEquals(contextId, logBridge.getContextId());
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
    public void testDefaultLevelIsDebug() {
        assertEquals(LogLevel.DEBUG, logBridge.getLevel());
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

    @Test
    public void testLogOneMessageGoesToPrintWriter() {
        String fqcn = getClass().getName();
        String domain = "org.znerd";
        String groupId = "sample";
        String entryId = "9876";
        LogLevel level = LogLevel.FATAL;
        String message = "Bla";
        Throwable exception = new Error();
        logBridge.logOneMessage(fqcn, domain, groupId, entryId, level, message, exception);

        String outputString = stringWriter.toString();
        assertNotNull(outputString);
        assertTrue(outputString.length() > 0);
    }

    @Test
    public void testLogOneMessageComposedMessage() {
        String fqcn = getClass().getName();
        String domain = "org.znerd";
        String groupId = "sample";
        String entryId = "9876";
        LogLevel level = LogLevel.FATAL;
        String message = "Bla";
        Throwable exception = null;
        logBridge.logOneMessage(fqcn, domain, groupId, entryId, level, message, exception);

        String outputString = stringWriter.toString();
        String expectedComposedMessage = level.name() + " [] " + domain + "." + groupId + '.' + entryId + ' ' + message + System.getProperty("line.separator");
        assertEquals(expectedComposedMessage, outputString);
    }

    @Test
    public void testLogOneMessageComposedMessageWithContextId() {
        String fqcn = getClass().getName();
        String domain = "org.znerd";
        String groupId = "sample";
        String entryId = "9876";
        LogLevel level = LogLevel.FATAL;
        String message = "Bla";
        String contextId = "TEST-CONTEXT-ID-123";
        Throwable exception = null;
        logBridge.putContextId(contextId);
        logBridge.logOneMessage(fqcn, domain, groupId, entryId, level, message, exception);

        String outputString = stringWriter.toString();
        String expectedComposedMessage = level.name() + " [" + contextId + "] " + domain + "." + groupId + '.' + entryId + ' ' + message + System.getProperty("line.separator");
        assertEquals(expectedComposedMessage, outputString);
    }
}
