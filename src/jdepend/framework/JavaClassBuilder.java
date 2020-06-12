package jdepend.framework;

import java.io.*;
import java.util.Collection;

/**
 * The <code>JavaClassBuilder</code> builds <code>JavaClass</code>
 * instances from .class, .jar, .war, or .zip files.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JavaClassBuilder {

    private final AbstractParser parser;
    private final ClassContainers classContainers;

    public JavaClassBuilder() {
        this(new ClassFileParser(), new ClassContainers());
    }

    public JavaClassBuilder(AbstractParser parser, ClassContainers containers) {
        this.parser = parser;
        this.classContainers = containers;
    }

    public int countClasses() {
        return classContainers.build().size();
    }

    /**
     * Builds the <code>JavaClass</code> instances from the
     * specified file.
     *
     * @param file Class or Jar file.
     * @return Collection of <code>JavaClass</code> instances.
     */
    public Collection buildClasses(File file) throws IOException {
        return classContainers.buildClasses(file);
    }

}
