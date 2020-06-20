package jdepend.framework;

import java.io.IOException;
import java.util.Collection;

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

    public void testBuildClasses_JarWith5Classes_5Classes() throws IOException {
        ClassContainer archiveContainer = new ArchiveClassContainer(getTestDataDir()+"test.jar");
        Collection classes = archiveContainer.buildClasses(true, new ClassFileParser());
        assertEquals(5, classes.size());
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
