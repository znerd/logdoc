// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.znerd.util.log.LogLevel;

public class JulLogBridgeTest extends AbstractLogBridgeTest {

    private JulTestHandler testHandler;
    private Level originalLevel;
    private boolean originalStackTraceAtMessageLevel;
    private static String DEFAULT_FQCN = JulLogBridgeTest.class.getName();
    private static String DEFAULT_LOG_DOMAIN = "org.znerd";
    private static String DEFAULT_GROUP_ID = "sample";
    private static String DEFAULT_ENTRY_ID = "9876";
    private static LogLevel DEFAULT_LEVEL = LogLevel.FATAL;
    private static String DEFAULT_MESSAGE = "Bla";

    @Override
    protected LogBridge provideLogBridge() {
        return JulLogBridge.getInstance();
    }

    @Before
    public void setUp() {
        testHandler = new JulTestHandler();
        Formatter formatter = new SimpleFormatter();
        testHandler.setFormatter(formatter);

        Logger rootLogger = getRootLogger();
        originalLevel = rootLogger.getLevel();
        rootLogger.setLevel(Level.ALL);
        rootLogger.addHandler(testHandler);

        originalStackTraceAtMessageLevel = Library.isStackTraceAtMessageLevel();
    }

    private Logger getRootLogger() {
        Logger logger = Logger.getLogger(DEFAULT_LOG_DOMAIN);
        Logger parentLogger = logger.getParent();
        while (parentLogger != null) {
            logger = parentLogger;
            parentLogger = logger.getParent();
        }
        return logger;
    }

    @After
    public void tearDown() {
        Library.setStackTraceAtMessageLevel(originalStackTraceAtMessageLevel);
        Logger rootLogger = getRootLogger();
        rootLogger.removeHandler(testHandler);
        rootLogger.setLevel(originalLevel);
    }

    @Test
    public void testLogOneMessage() {
        Throwable exception = null;
        getLogBridge().logOneMessage(DEFAULT_FQCN, DEFAULT_LOG_DOMAIN, DEFAULT_GROUP_ID, DEFAULT_ENTRY_ID, DEFAULT_LEVEL, DEFAULT_MESSAGE, exception);

        LogRecord lastRecord = testHandler.getLastLogRecord();
        assertNotNull(lastRecord);
        assertNull(lastRecord.getThrown());
        String actualMessage = lastRecord.getMessage();
        String expectedMessage = DEFAULT_LEVEL.name() + " [] " + DEFAULT_LOG_DOMAIN + "." + DEFAULT_GROUP_ID + '.' + DEFAULT_ENTRY_ID + ' ' + DEFAULT_MESSAGE;
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLogOneMessageWithContextId() {
        String contextId = "TEST-CONTEXT-ID-123";
        Throwable exception = null;
        getLogBridge().putContextId(contextId);
        try {
            getLogBridge().logOneMessage(DEFAULT_FQCN, DEFAULT_LOG_DOMAIN, DEFAULT_GROUP_ID, DEFAULT_ENTRY_ID, DEFAULT_LEVEL, DEFAULT_MESSAGE, exception);

            LogRecord lastRecord = testHandler.getLastLogRecord();
            assertNotNull(lastRecord);
            assertNull(lastRecord.getThrown());
            String actualMessage = lastRecord.getMessage();
            String expectedMessage = DEFAULT_LEVEL.name() + " [" + contextId + "] " + DEFAULT_LOG_DOMAIN + "." + DEFAULT_GROUP_ID + '.' + DEFAULT_ENTRY_ID + ' ' + DEFAULT_MESSAGE;
            assertEquals(expectedMessage, actualMessage);
        } finally {
            getLogBridge().unputContextId();
        }
    }

    @Test
    public void testLogOneMessageWithException() {
        String contextId = "TEST-CONTEXT-ID-999";
        Throwable exception = new Exception("Grrrreat!");
        getLogBridge().putContextId(contextId);
        try {
            getLogBridge().logOneMessage(DEFAULT_FQCN, DEFAULT_LOG_DOMAIN, DEFAULT_GROUP_ID, DEFAULT_ENTRY_ID, DEFAULT_LEVEL, DEFAULT_MESSAGE, exception);

            LogRecord lastRecord = testHandler.getLastLogRecord();
            assertNotNull(lastRecord);
            assertEquals(exception, lastRecord.getThrown());
            String actualMessage = lastRecord.getMessage();
            String expectedMessage = DEFAULT_LEVEL.name() + " [" + contextId + "] " + DEFAULT_LOG_DOMAIN + "." + DEFAULT_GROUP_ID + '.' + DEFAULT_ENTRY_ID + ' ' + DEFAULT_MESSAGE;
            assertEquals(expectedMessage, actualMessage);
        } finally {
            getLogBridge().unputContextId();
        }
    }
}
