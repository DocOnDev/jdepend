package jdepend.framework;

import java.io.File;
import java.io.IOException;

public class JavaClassBuilderTest extends JDependTestCase{

    private final ClassContainers classContainers = new ClassContainers();

    public JavaClassBuilderTest(String name) { super(name); }

    protected void setUp() { super.setUp(); }
    protected void tearDown() {
        super.tearDown();
    }

    public void testThrowsExceptionWhenFileNotFound() {
        String missingFilePath = getTestDataDir() + "non-existent.file";

        try {
            classContainers.buildClasses(new File(missingFilePath));
            fail("Should raise IOException");
        } catch (IOException e) {
            assertEquals("File is not a valid .class, .jar, .war, or .zip file: " + missingFilePath, e.getMessage());
        }
    }

    public void testThrowsExceptionWhenFileNotValid() {
        try {
            classContainers.buildClasses(new File(getTestDataDir() + "bad.jar"));
            fail("Should raise IOException");
        } catch (IOException e) {
            assertEquals("error in opening zip file", e.getMessage());
        }
    }
}
