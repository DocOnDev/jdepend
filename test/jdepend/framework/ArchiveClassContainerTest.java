package jdepend.framework;

import java.io.IOException;

public class ArchiveClassContainerTest extends JDependTestCase {
    public ArchiveClassContainerTest(String name) {
        super(name);
    }

    public void testConstructor_MissingFile_IOException() {
        confirmIOExeption(getTestDataDir() + "bogus.jar");
    }

    public void testConstructor_JarFile_Exists() throws IOException {
        assertNotNull(new ArchiveClassContainer(getTestDataDir()+"test.jar"));
    }

    public void testConstructor_WarFile_Exists() throws IOException {
        assertNotNull(new ArchiveClassContainer(getTestDataDir()+"test.war"));
    }

    public void testConstructor_ZipFile_Exists() throws IOException {
        assertNotNull(new ArchiveClassContainer(getTestDataDir()+"test.zip"));
    }

    private void confirmIOExeption(String source) {
        try {
            new ArchiveClassContainer(source);
            fail("Should raise IOException");
        } catch (IOException exception) {
            assertEquals("Invalid directory or Container file: " + source, exception.getMessage());
        }
    }

}
