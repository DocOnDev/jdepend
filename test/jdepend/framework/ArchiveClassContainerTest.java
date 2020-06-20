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

    public void testBuildClasses_JarWithInnerClasses_5Classes() throws IOException {
        ClassContainer archiveContainer = new ArchiveClassContainer(getTestDataDir()+"test.jar");
        Collection classes = archiveContainer.buildClasses(true, new ClassFileParser());
        assertEquals(5, classes.size());
    }

    public void testBuildClasses_ZipWithInnerClasses_HasInnerClass() throws IOException {
        ClassContainer archiveContainer = new ArchiveClassContainer(getTestDataDir()+"test.zip");
        Collection classes = archiveContainer.buildClasses(true, new ClassFileParser());
        assertTrue(classes.contains(new JavaClass("jdepend.framework.ExampleConcreteClass$ExampleInnerClass")));
    }

    public void testBuildClasses_ZipWithoutInnerClasses_4Classes() throws IOException {
        ClassContainer archiveContainer = new ArchiveClassContainer(getTestDataDir()+"test.zip");
        Collection classes = archiveContainer.buildClasses(false, new ClassFileParser());
        assertEquals(4, classes.size());
    }

    public void testBuildClasses_JarWithoutInnerClasses_NoInnerClass() throws IOException {
        ClassContainer archiveContainer = new ArchiveClassContainer(getTestDataDir()+"test.jar");
        Collection classes = archiveContainer.buildClasses(false, new ClassFileParser());
        assertFalse(classes.contains(new JavaClass("jdepend.framework.ExampleConcreteClass$ExampleInnerClass")));
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
