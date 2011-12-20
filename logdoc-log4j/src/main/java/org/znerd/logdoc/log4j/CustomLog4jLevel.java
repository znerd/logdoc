// Copyright 2011, Deli XL B.V. (Netherlands)
package org.znerd.logdoc.log4j;

import org.apache.log4j.Level;

@SuppressWarnings("serial")
public class CustomLog4jLevel extends Level {
    public CustomLog4jLevel(int value, String name, int syslogEquivalent) {
        super(value, name, syslogEquivalent);
    }
}
