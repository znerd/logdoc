// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.bridges.atg;

import java.util.Map;

import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.logdoc.internal.AbstractTranslationBundle;
import org.znerd.util.log.LogLevel;

import atg.nucleus.logging.ApplicationLogging;
import atg.nucleus.logging.ApplicationLoggingImpl;

public final class AtgLogBridge extends LogBridge {

    private static final AtgLogBridge SINGLETON_INSTANCE = new AtgLogBridge();

    private AtgLogBridge() {
    }

    public static AtgLogBridge getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public void log(String domain, String groupId, String entryId, LogLevel level, AbstractTranslationBundle translationBundle, Throwable exception, Map<String, Object> parameters) {
        String componentId = domain + '.' + groupId;
        ApplicationLogging logger = getApplicationLogging(componentId);
        if (isLevelEnabled(logger, level)) {
            log(logger, entryId, level, translationBundle, exception);
        }
    }

    private ApplicationLogging getApplicationLogging(String componentId) {
        ApplicationLoggingImpl logger = new ApplicationLoggingImpl(componentId);
        return logger;
    }

    private boolean isLevelEnabled(ApplicationLogging logger, LogLevel level) {
        if (LogLevel.DEBUG.equals(level)) {
            return logger.isLoggingDebug();
        } else if (LogLevel.INFO.equals(level) || LogLevel.NOTICE.equals(level)) {
            return logger.isLoggingInfo();
        } else if (LogLevel.WARNING.equals(level)) {
            return logger.isLoggingWarning();
        } else {
            return logger.isLoggingError();
        }
    }

    private void log(ApplicationLogging logger, String entryId, LogLevel level, AbstractTranslationBundle translationBundle, Throwable exception) {
        if (LogLevel.DEBUG.equals(level)) {
            logger.logDebug(message, exception);
        } else if (LogLevel.INFO.equals(level) || LogLevel.NOTICE.equals(level)) {
            return logger.isLoggingInfo();
        } else if (LogLevel.WARNING.equals(level)) {
            return logger.isLoggingWarning();
        } else {
            return logger.isLoggingError();
        }
    }
}
