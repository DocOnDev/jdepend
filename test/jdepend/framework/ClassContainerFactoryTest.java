package jdepend.framework;

import java.io.IOException;

public class ClassContainerFactoryTest extends JDependTestCase {
    public ClassContainerFactoryTest(String name) {
        super(name);
    }

    public void testGetContainer_ValidDirectory_DirectoryClassContainer() throws IOException {
        assertTrue(ClassContainerFactory.getContainer(getBuildDir()) instanceof DirectoryClassContainer);
    }

    public void testGetContainer_JarFile_ArchiveClassContainer() throws IOException {
        assertTrue(ClassContainerFactory.getContainer(getTestDataDir()+"test.jar") instanceof ArchiveClassContainer);
    }

    public void testGetContaier_MissingFile_IOException() {
        String source = getBuildDir() + "bogus";
        try {
            ClassContainerFactory.getContainer(source);
            fail("Shoudl raise IOException!");
        } catch (IOException e) {
            assertEquals("Invalid directory or Container file: " + source, e.getMessage());
        }
    }
}
