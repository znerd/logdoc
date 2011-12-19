// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import org.junit.Test;
import org.znerd.util.test.TestFailedException;
import org.znerd.util.test.TestUtils;

public class LibraryClassTest {

    @Test
    public void testUtilityClassConstructor() throws TestFailedException {
        TestUtils.testUtilityClassConstructor(Library.class);
    }
}
