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

    public void testExtractFiles_EmptyFileManager_ZeroSize() {
        assertEquals(0, fileManager.extractFiles().size());
    }

    public void testAddDirectory_BuildDirectory_44Files() throws IOException {
        fileManager.classContainers.addDirectory(getBuildDir());
        assertEquals(44, fileManager.extractFiles().size());
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

    public void testIsValidContainer_JarFile_True() {
        assertTrue(fileManager.classContainers.isValidContainer(new File(getTestDataDir()+ "test.jar")));
    }

    public void testIsValidContainer_ZipFile_True() {
        assertTrue(fileManager.classContainers.isValidContainer(new File(getTestDataDir() + "test.zip")));
    }

    public void testIsValidContainer_BinFile_False() {
        assertFalse(fileManager.classContainers.isValidContainer(new File(getTestDataDir() + "example_class1.bin")));
    }

    public void testAcceptClassFile_ValidClassFile_True() throws IOException {
        assertTrue(fileManager.classContainers.acceptClassFile(new File(getBuildDir() + getPackageSubDir() + "JDepend.class")));
    }

    public void testAcceptClassFile_NonExistentClassFile_False() {
        assertFalse(fileManager.classContainers.acceptClassFile(new File(getBuildDir() + "JDepend.class")));
    }

    public void testAcceptClassFile_InvalidClassFile_False() {
        assertFalse(fileManager.classContainers.acceptClassFile(new File(getHomeDir() + "build.xml")));
    }

    private void assertIOError(String fileName, String errorReason) {
        try {
            fileManager.classContainers.addDirectory(fileName);
            fail(errorReason + " " + "Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid directory or Container file: " + fileName, expected.getMessage());
        }
    }

}