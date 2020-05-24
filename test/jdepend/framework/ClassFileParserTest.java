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

        File f = new File(getTestDir() + getPackageSubDir() + "ExampleTest.java");

        try {
            parser.parse(f);
            fail("Invalid class file: Should raise IOException");
        } catch (IOException expected) {
            assertTrue(true);
        }
    }

    public void testInterfaceClass() throws IOException {

        String className = "ExampleInterface";
        String sourceFileName = className + ".java";
        boolean isAbstract = true;
        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.math"),
                new JavaPackage("java.text"),
                new JavaPackage("java.lang"),
                new JavaPackage("java.io"),
                new JavaPackage("java.rmi"),
                new JavaPackage("java.util")
        ));

        validateParser(className, sourceFileName, isAbstract, expectedImports);
    }

    public void testAbstractClass() throws IOException {

        String className = "ExampleAbstractClass";
        String sourceFileName = className + ".java";
        boolean isAbstract = true;
        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.math"),
                new JavaPackage("java.text"),
                new JavaPackage("java.lang"),
                new JavaPackage("java.lang.reflect"),
                new JavaPackage("java.io"),
                new JavaPackage("java.rmi"),
                new JavaPackage("java.util")
        ));

        validateParser(className, sourceFileName, isAbstract, expectedImports);

    }

    public void testConcreteClass() throws IOException {

        String className = "ExampleConcreteClass";
        String sourceFileName = className + ".java";
        boolean isAbstract = false;
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

        validateParser(className, sourceFileName, isAbstract, expectedImports);
    }

    public void testInnerClass() throws IOException {

        String className = "ExampleConcreteClass$ExampleInnerClass";
        String sourceFileName = "ExampleConcreteClass.java";
        boolean isAbstract = false;
        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.lang")
        ));

        validateParser(className, sourceFileName, isAbstract, expectedImports);

    }

    public void testPackageClass() throws IOException {
        String className = "ExamplePackageClass";
        String sourceFileName = "ExampleConcreteClass.java";
        boolean isAbstract = false;
        Collection<JavaPackage> expectedImports = new ArrayList(Arrays.asList(
                new JavaPackage("java.lang")
        ));

        validateParser(className, sourceFileName, isAbstract, expectedImports);

    }

    private void validateParser(String className, String sourceFileName, boolean isAbstract, Collection<JavaPackage> expectedImports) throws IOException {
        File f = new File(getBuildDir() + getPackageSubDir() + className + ".class");

        JavaClass clazz = parser.parse(f);
        assertEquals(clazz.isAbstract(), isAbstract);
        assertEquals("jdepend.framework." + className, clazz.getName());
        assertEquals(sourceFileName, clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(expectedImports.size(), imports.size());
        for (JavaPackage pkg : expectedImports) {
            assertTrue(imports.contains(pkg));
        }
    }

    public void testExampleClassFileFromTimDrury() throws IOException {
        // see http://github.com/clarkware/jdepend/issues#issue/1
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class1.bin"));
    }

    public void testExampleClassFile2() throws IOException {
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class2.bin"));
    }
}

