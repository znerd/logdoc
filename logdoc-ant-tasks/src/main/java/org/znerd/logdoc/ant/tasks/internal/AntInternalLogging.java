// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks.internal;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.znerd.logdoc.LogLevel;
import org.znerd.logdoc.internal.InternalLogging;

/**
 * Logdoc-internal logger that sends output via the Ant logging mechanism.
 */
public class AntInternalLogging extends InternalLogging {

    public AntInternalLogging(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task == null");
        }
        _task = task;
    }

    private final Task _task;

    @Override
    public void logImpl(LogLevel level, String message, Throwable exception) {
        final int antLevel = convertToAntLevel(level);
        logViaAnt(message, exception, antLevel);
    }

    private int convertToAntLevel(LogLevel level) {
        int antLevel;
        switch (level) {
            case DEBUG:
                antLevel = Project.MSG_DEBUG;
                break;
            case INFO:
                antLevel = Project.MSG_VERBOSE;
                break;
            case NOTICE:
                antLevel = Project.MSG_INFO;
                break;
            case WARNING:
                antLevel = Project.MSG_WARN;
                break;
            default:
                antLevel = Project.MSG_ERR;
        }
        return antLevel;
    }

    private void logViaAnt(String message, Throwable exception, int antLevel) {
        if (exception == null) {
            _task.log(message, antLevel);
        } else {
            _task.log(message, exception, antLevel);
        }
    }
}
