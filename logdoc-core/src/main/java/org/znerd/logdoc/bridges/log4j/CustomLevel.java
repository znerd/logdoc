// Copyright 2011, Deli XL B.V. (Netherlands)
package org.znerd.logdoc.bridges.log4j;

import org.apache.log4j.Level;

@SuppressWarnings("serial")
public class CustomLevel extends Level {

    public CustomLevel(int value, String name, int syslogEquivalent) {
        super(value, name, syslogEquivalent);
    }
}
