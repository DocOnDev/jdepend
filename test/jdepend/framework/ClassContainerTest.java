package jdepend.framework;

import java.awt.*;
import java.io.IOException;

public class ClassContainerTest extends JDependTestCase {
    public ClassContainerTest(String name) {
        super(name);
    }

    public void testConstructor_ValidDirectory_Exists() throws IOException {
        assertNotNull(new ClassContainer(getBuildDir()));
    }

    public void testConstructor_InvalidDirectory_IOException() {
        confirmIOExeption(getBuildDir() + "bogus");
    }

    public void testConstructor_JarFile_Exists() throws IOException {
        assertNotNull(new ClassContainer(getTestDataDir()+"test.jar"));
    }

    public void testConstructor_WarFile_Exists() throws IOException {
        assertNotNull(new ClassContainer(getTestDataDir()+"test.war"));
    }

    public void testConstructor_ZipFile_Exists() throws IOException {
        assertNotNull(new ClassContainer(getTestDataDir()+"test.zip"));
    }

    public void testConstructor_MissingFile_IOException() {
        confirmIOExeption(getTestDataDir() + "bogus.jar");
    }

    public void testgetFile_JarFile_JarFile() throws IOException {
        String source = getTestDataDir() + "test.jar";
        ClassContainer classContainer = new ClassContainer(source);
        assertEquals(classContainer.getFile().getName(), "test.jar");
    }

    private void confirmIOExeption(String source) {
        try {
            new ClassContainer(source);
            fail("Should raise IOException");
        } catch (IOException exception) {
            assertEquals("Invalid directory or Container file: " + source, exception.getMessage());
            ;
        }
    }
}
