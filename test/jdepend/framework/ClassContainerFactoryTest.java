package jdepend.framework;

import java.io.IOException;

public class ClassContainerFactoryTest extends JDependTestCase {
    public ClassContainerFactoryTest(String name) {
        super(name);
    }

    public void testGetContainer_Directory_DirectoryClassContainer() throws IOException {
        assertTrue(ClassContainerFactory.getContainer(getBuildDir()) instanceof DirectoryClassContainer);
    }

    public void testGetContainer_JarFile_ArchiveClassContainer() throws IOException {
        assertTrue(ClassContainerFactory.getContainer(getTestDataDir()+"test.jar") instanceof ArchiveClassContainer);
    }

    public void testGetContainer_MissingFile_IOException() {
        String source = getBuildDir() + "bogus.war";
        try {
            ClassContainerFactory.getContainer(source);
            fail("Should raise IOException");
        } catch (IOException exception) {
            assertEquals("Invalid directory or Container file: " + source, exception.getMessage());
            ;
        }

    }
}
