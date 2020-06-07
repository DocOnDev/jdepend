package jdepend.framework;

import java.io.IOException;

public class DirectoryClassContainerTest extends JDependTestCase {
    public DirectoryClassContainerTest(String name) {
        super(name);
    }

    public void testConstructor_ValidDirectory_Exists() throws IOException {
        assertNotNull(new DirectoryClassContainer(getBuildDir()));
    }

    public void testConstructor_JarFile_IOException() {
        confirmIOExeption(getTestDataDir() + "test.jar");
    }

    public void testConstructor_InvalidDirectory_IOException() {
        confirmIOExeption(getBuildDir() + "bogus");
    }

    private void confirmIOExeption(String source) {
        try {
            new DirectoryClassContainer(source);
            fail("Should raise IOException");
        } catch (IOException exception) {
            assertEquals("Invalid directory or Container file: " + source, exception.getMessage());
        }
    }

}
