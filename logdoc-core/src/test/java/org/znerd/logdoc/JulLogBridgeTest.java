// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

public class JulLogBridgeTest extends AbstractLogBridgeTest {

    @Override
    protected LogBridge provideLogBridge() {
        return JulLogBridge.getInstance();
    }
}
