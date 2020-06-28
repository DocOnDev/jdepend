package jdepend.framework;

import java.io.File;
import java.io.IOException;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class ClassContainersTest extends JDependTestCase {

    private ClassContainers classContainers;

    public ClassContainersTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        classContainers = new ClassContainers();
        classContainers.acceptInnerClasses(false);
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testAddDirectory_NonExistentDirectory_IOError() {
        String file = getBuildDir() + "junk";
        String errorReason = "Non-existent directory:";
        assertIOError(file, errorReason);
    }

    public void testAddDirectory_InvalidDirectory_IOError() {
        String file = getTestDir() + getPackageSubDir() + "ExampleTest.java";
        String errorReason = "Invalid directory:";
        assertIOError(file, errorReason);
    }

    public void testAcceptClassFile_ValidClassFile_True() throws IOException {
        File file = new File(getBuildDir() + getPackageSubDir() + "JDepend.class");
        assertTrue(classContainers.acceptClassFileName(file.getName()));
    }

    public void testAcceptClassFile_InvalidClassFile_False() {
        File file = new File(getHomeDir() + "build.xml");
        assertFalse(classContainers.acceptClassFileName(file.getName()));
    }

    private void assertIOError(String fileName, String errorReason) {
        try {
            classContainers.add(ClassContainerFactory.getContainer(fileName));
            fail(errorReason + " " + "Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid directory or Container file: " + fileName, expected.getMessage());
        }
    }

}