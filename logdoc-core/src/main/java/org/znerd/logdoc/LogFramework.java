// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.logdoc.bridges.LogBridge;
import org.znerd.logdoc.bridges.atg.AtgLogBridge;
import org.znerd.logdoc.bridges.log4j.Log4jLogBridge;

public enum LogFramework {

    LOG4J(Log4jLogBridge.getInstance()), ATG(AtgLogBridge.getInstance());

    private LogBridge logBridge;

    LogFramework(LogBridge logBridge) {
        this.logBridge = logBridge;
    }

    public LogBridge getLogBridge() {
        return logBridge;
    }

}
