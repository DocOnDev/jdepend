package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JarFileParserTest extends JDependTestCase {

    private File jarFile;
    private File zipFile;

    public JarFileParserTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();

        jarFile = new File(getTestDataDir() + "test.jar");
        zipFile = new File(getTestDataDir() + "test.zip");
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

    private void assertInnerClassesExist(Collection classes) {
        assertTrue(classes.contains(new JavaClass(
                "jdepend.framework.ExampleConcreteClass$ExampleInnerClass")));
    }
}