package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class ClassFileParserTest extends JDependTestCase {

    private ClassFileParser parser;

    public ClassFileParserTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        PackageFilter filter = new PackageFilter(new ArrayList());
        parser = new ClassFileParser(filter);
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testInvalidClassFile() {

        String filePath = getTestDir() + getPackageSubDir() + "ExampleTest.java";
        File f = new File(filePath);

        try {
            parser.parse(f);
            fail("Invalid class file: Should raise IOException");
        } catch (IOException expected) {
            assertEquals("Invalid class file: " + filePath, expected.getMessage());
        }
    }

    public void testInterfaceClass() throws IOException {

        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.math"),
                new JavaPackage("java.text"),
                new JavaPackage("java.lang"),
                new JavaPackage("java.io"),
                new JavaPackage("java.rmi"),
                new JavaPackage("java.util")
        ));

        ClassFileCriteria classFileCriteria = new ClassFileCriteria(
                "ExampleInterface",
                "ExampleInterface.java",
                true,
                expectedImports);

        validateParser(classFileCriteria);
    }

    public void testAbstractClass() throws IOException {

        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.math"),
                new JavaPackage("java.text"),
                new JavaPackage("java.lang"),
                new JavaPackage("java.lang.reflect"),
                new JavaPackage("java.io"),
                new JavaPackage("java.rmi"),
                new JavaPackage("java.util")
        ));

        ClassFileCriteria classFileCriteria = new ClassFileCriteria(
                "ExampleAbstractClass",
                "ExampleAbstractClass.java",
                true,
                expectedImports);

        validateParser(classFileCriteria);
    }

    public void testConcreteClass() throws IOException {

        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.net"),
                new JavaPackage("java.text"),
                new JavaPackage("java.sql"),
                new JavaPackage("java.lang"),
                new JavaPackage("java.io"),
                new JavaPackage("java.rmi"),
                new JavaPackage("java.util"),
                new JavaPackage("java.util.jar"),
                new JavaPackage("java.math"),

                // annotations
                new JavaPackage("org.junit.runners"),
                new JavaPackage("java.applet"),
                new JavaPackage("org.junit"),
                new JavaPackage("javax.crypto"),
                new JavaPackage("java.awt.geom"),
                new JavaPackage("java.awt.image.renderable"),
                new JavaPackage("jdepend.framework.p1"),
                new JavaPackage("jdepend.framework.p2"),
                new JavaPackage("java.awt.im"),
                new JavaPackage("java.awt.dnd.peer")
        ));

        ClassFileCriteria classFileCriteria = new ClassFileCriteria(
                "ExampleConcreteClass",
                "ExampleConcreteClass.java",
                false,
                expectedImports);

        validateParser(classFileCriteria);
    }

    public void testInnerClass() throws IOException {

        ClassFileCriteria classFileCriteria = new ClassFileCriteria(
                "ExampleConcreteClass$ExampleInnerClass",
                "ExampleConcreteClass.java",
                false,
                new ArrayList(Arrays.asList(
                        new JavaPackage("java.lang")
                )));

        validateParser(classFileCriteria);
    }

    public void testPackageClass() throws IOException {

        ClassFileCriteria classFileCriteria = new ClassFileCriteria(
                "ExamplePackageClass",
                "ExampleConcreteClass.java",
                false,
                new ArrayList(Arrays.asList(
                        new JavaPackage("java.lang")
                )));

        validateParser(classFileCriteria);
    }

    private void validateParser(ClassFileCriteria classFileCriteria) throws IOException {
        File f = new File(getBuildDir() + getPackageSubDir() + classFileCriteria.getClassName() + ".class");

        JavaClass javaClass = parser.parse(f);
        assertEquals(javaClass.isAbstract(), classFileCriteria.isAbstract());
        assertEquals("jdepend.framework." + classFileCriteria.getClassName(), javaClass.getName());
        assertEquals(classFileCriteria.getSourceFileName(), javaClass.getSourceFile());

        Collection importedPackages = javaClass.getImportedPackages();
        Collection<JavaPackage> expectedPackages = classFileCriteria.getExpectedImports();

        assertEquals(expectedPackages.size(), importedPackages.size());
        for (JavaPackage javaPackage : expectedPackages) {
            assertTrue(importedPackages.contains(javaPackage));
        }
    }

    public void testExampleClassFileFromTimDrury() throws IOException {
        // see http://github.com/clarkware/jdepend/issues#issue/1
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class1.bin"));
    }

    public void testExampleClassFile2() throws IOException {
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class2.bin"));
    }

    private static class ClassFileCriteria {
        private final String className;
        private final String sourceFileName;
        private final boolean isAbstract;
        private final Collection<JavaPackage> expectedImports;

        private ClassFileCriteria(String className, String sourceFileName, boolean isAbstract, Collection<JavaPackage> expectedImports) {
            this.className = className;
            this.sourceFileName = sourceFileName;
            this.isAbstract = isAbstract;
            this.expectedImports = expectedImports;
        }

        public String getClassName() {
            return className;
        }

        public String getSourceFileName() {
            return sourceFileName;
        }

        public boolean isAbstract() {
            return isAbstract;
        }

        public Collection<JavaPackage> getExpectedImports() {
            return expectedImports;
        }
    }
}