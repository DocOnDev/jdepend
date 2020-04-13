package jdepend.framework;

import java.io.File;
import java.io.IOException;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManagerTest extends JDependTestCase {

    private FileManager fileManager;
    
    public FileManagerTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        fileManager = new FileManager();
        fileManager.acceptInnerClasses(false);
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testEmptyFileManager() {
        assertEquals(0, fileManager.extractFiles().size());
    }

    public void testAddDirectoryAcceptsValidDirectory() throws IOException {
        fileManager.addDirectory(getBuildDir());
        assertEquals(44, fileManager.extractFiles().size());
    }

    public void testAddDirectoryErrorsForNonExistentDirectory() {
        String file = getBuildDir() + "junk";
        String errorReason = "Non-existent directory:";
        assertIOError(file, errorReason);
    }

    public void testAddDirectoryErrorsForInvalidDirectory() {
        String file = getTestDir() + getPackageSubDir() + "ExampleTest.java";
        String errorReason = "Invalid directory:";
        assertIOError(file, errorReason);
    }

    public void testIsValidContainerAcceptsJarFile() {
        assertTrue(fileManager.isValidContainer(new File(getTestDataDir()+ "test.jar")));
    }

    public void testIsValidContainerAcceptsZipFile() {
        assertTrue(fileManager.isValidContainer(new File(getTestDataDir() + "test.zip")));
    }

    public void testIsValidContainerDoesNotAcceptBinFile() {
        assertFalse(fileManager.isValidContainer(new File(getTestDataDir() + "example_class1.bin")));
    }

    public void testClassFile() throws IOException {
        assertTrue(fileManager.acceptClassFile(new File(getBuildDir() + getPackageSubDir() + "JDepend.class")));
    }

    public void testNonExistentClassFile() {
        assertFalse(fileManager.acceptClassFile(new File(getBuildDir() + "JDepend.class")));
    }

    public void testInvalidClassFile() {
        assertFalse(fileManager.acceptClassFile(new File(getHomeDir() + "build.xml")));
    }

    private void assertIOError(String fileName, String errorReason) {
        try {
            fileManager.addDirectory(fileName);
            fail(errorReason + " " + "Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid directory or Container file: " + fileName, expected.getMessage());
        }
    }

}