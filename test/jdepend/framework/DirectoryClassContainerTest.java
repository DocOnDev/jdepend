package jdepend.framework;

import java.io.IOException;
import java.util.Collection;

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

    public void testBuildClasses_SimpleDirectory_OneClass() throws IOException {
        ClassContainer container = new DirectoryClassContainer(getBuildDir()+"jdepend/framework/p1/");
        Collection classes = container.buildClasses(true, new ClassFileParser());
        assertEquals(1, classes.size());
    }

    public void testBuildClasses_FrameworkDir_39Classes() throws IOException {
        ClassContainer container = new DirectoryClassContainer(getBuildDir()+getPackageSubDir());
        Collection classes = container.buildClasses(false, new ClassFileParser());
        assertEquals(39, classes.size());
    }

    public void testBuildClasses_TestBuild_61Classes() throws IOException {
        ClassContainer container = new DirectoryClassContainer(getBuildDir());
        Collection classes = container.buildClasses(false, new ClassFileParser());
        assertEquals(61, classes.size());
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
