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

    public void testBuildDirectory() throws IOException {
        fileManager.addDirectory(getBuildDir());
        assertEquals(44, fileManager.extractFiles().size());
    }

    public void testNonExistentDirectory() {

        String file = getBuildDir() + "junk";
        String errorReason = "Non-existent directory:";
        assertIOError(file, errorReason);
    }

    public void testInvalidDirectory() {

        String file = getTestDir() + getPackageSubDir() + "ExampleTest.java";
        String errorReason = "Invalid directory:";
        assertIOError(file, errorReason);
    }

    private void assertIOError(String fileName, String errorReason) {
        try {
            fileManager.addDirectory(fileName);
            fail(errorReason + " " + "Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid directory or Container file: " + fileName, expected.getMessage());
        }
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

        File f = new File(getBuildDir() + getPackageSubDir() + "JDepend.class");

        assertEquals(true, new FileManager().acceptClassFile(f));
    }

    public void testNonExistentClassFile() {
        File f = new File(getBuildDir() + "JDepend.class");
        assertEquals(false, new FileManager().acceptClassFile(f));
    }

    public void testInvalidClassFile() {
        File f = new File(getHomeDir() + "build.xml");
        assertEquals(false, new FileManager().acceptClassFile(f));
    }

    public void testJar() throws IOException {
        File f = File.createTempFile("bogus", ".jar", 
            new File(getTestDataDir()));
        fileManager.addDirectory(f.getPath());
        f.deleteOnExit();
    }

    public void testZip() throws IOException {
        File f = File.createTempFile("bogus", ".zip", 
            new File(getTestDataDir()));
        fileManager.addDirectory(f.getPath());
        f.deleteOnExit();
    }

    public void testWar() throws IOException {
        File f = File.createTempFile("bogus", ".war", 
            new File(getTestDataDir()));
        fileManager.addDirectory(f.getPath());
        f.deleteOnExit();
    }
}