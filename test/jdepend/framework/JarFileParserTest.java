package jdepend.framework;

import java.io.File;
import java.io.IOException;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JarFileParserTest extends JDependTestCase {

    public JarFileParserTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }


    public void testCountClasses() throws IOException {
        JDepend jdepend = new JDepend();
        jdepend.addDirectory(getTestDataDir());

        jdepend.analyzeInnerClasses(true);
        assertEquals(15, jdepend.countClasses());

        jdepend.analyzeInnerClasses(false);
        assertEquals(12, jdepend.countClasses());
    }

}