// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.znerd.logdoc.AbstractLogBridgeTest;
import org.znerd.logdoc.LogBridge;

public class Log4jLogBridgeTest extends AbstractLogBridgeTest {
    
    private TestAppender testAppender;
    private Level originalLevel;

    @Before
    public void setUp() {
        testAppender = new TestAppender();
        Logger rootLogger = Logger.getRootLogger();
        assertLog4jInitialized(rootLogger);
        originalLevel = rootLogger.getLevel();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(testAppender);
    }

    private void assertLog4jInitialized(Logger rootLogger) {
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            throw new RuntimeException("Log4J is not initialized.");
        }
    }

    @After
    public void tearDown() {
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.removeAppender(testAppender);
        rootLogger.setLevel(originalLevel);
    }

    @Override
    protected LogBridge provideLogBridge() {
        return Log4jLogBridge.getInstance();
    }
}
