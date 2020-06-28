package jdepend.framework;

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

    private void assertIOError(String fileName, String errorReason) {
        try {
            classContainers.add(ClassContainerFactory.getContainer(fileName));
            fail(errorReason + " " + "Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid directory or Container file: " + fileName, expected.getMessage());
        }
    }

}