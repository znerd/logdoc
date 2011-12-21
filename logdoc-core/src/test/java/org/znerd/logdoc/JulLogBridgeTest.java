// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import static org.junit.Assert.assertEquals;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.znerd.util.log.LogLevel;

public class JulLogBridgeTest extends AbstractLogBridgeTest {

    private TestHandler testHandler;
    private Level originalLevel;
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
        testHandler = new TestHandler();
        Formatter formatter = new SimpleFormatter();
        testHandler.setFormatter(formatter);

        Logger rootLogger = getRootLogger();
        originalLevel = rootLogger.getLevel();
        rootLogger.setLevel(Level.ALL);
        rootLogger.addHandler(testHandler);
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
        Logger rootLogger = getRootLogger();
        rootLogger.removeHandler(testHandler);
        rootLogger.setLevel(originalLevel);
    }

    @Test
    public void testLogOneMessageComposedMessage() {
        Throwable exception = null;
        getLogBridge().logOneMessage(DEFAULT_FQCN, DEFAULT_LOG_DOMAIN, DEFAULT_GROUP_ID, DEFAULT_ENTRY_ID, DEFAULT_LEVEL, DEFAULT_MESSAGE, exception);

        String actualMessage = testHandler.getLastMessage();
        String expectedMessage = DEFAULT_LEVEL.name() + " [] " + DEFAULT_LOG_DOMAIN + "." + DEFAULT_GROUP_ID + '.' + DEFAULT_ENTRY_ID + ' ' + DEFAULT_MESSAGE;
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testLogOneMessageComposedMessageWithContextId() {

        String contextId = "TEST-CONTEXT-ID-123";
        Throwable exception = null;
        getLogBridge().putContextId(contextId);
        try {
            getLogBridge().logOneMessage(DEFAULT_FQCN, DEFAULT_LOG_DOMAIN, DEFAULT_GROUP_ID, DEFAULT_ENTRY_ID, DEFAULT_LEVEL, DEFAULT_MESSAGE, exception);

            String actualMessage = testHandler.getLastMessage();
            String expectedMessage = DEFAULT_LEVEL.name() + " [" + contextId + "] " + DEFAULT_LOG_DOMAIN + "." + DEFAULT_GROUP_ID + '.' + DEFAULT_ENTRY_ID + ' ' + DEFAULT_MESSAGE;
            assertEquals(expectedMessage, actualMessage);
        } finally {
            getLogBridge().unputContextId();
        }
    }
}
