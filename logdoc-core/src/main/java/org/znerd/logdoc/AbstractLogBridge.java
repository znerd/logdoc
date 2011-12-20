// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.znerd.util.Preconditions;
import org.znerd.util.log.LogLevel;

/**
 * Abstract base class for <code>LogBridge</code> implementations. Implements the <em>level</em> property by storing it internally.
 */
public abstract class AbstractLogBridge implements LogBridge {

    private LogLevel level = LogLevel.DEBUG;

    @Override
    public void setLevel(LogLevel level) {
        Preconditions.checkArgument(level == null, "level == null");
        this.level = level;
    }

    @Override
    public LogLevel getLevel() {
        return level;
    }
}
