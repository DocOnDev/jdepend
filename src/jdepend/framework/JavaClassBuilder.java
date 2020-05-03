package jdepend.framework;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

/**
 * The <code>JavaClassBuilder</code> builds <code>JavaClass</code> 
 * instances from .class, .jar, .war, or .zip files.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JavaClassBuilder {

    private AbstractParser parser;
    private ClassContainers classContainers;

    public JavaClassBuilder() {
        this(new ClassFileParser());
    }

    public JavaClassBuilder(ClassContainers containers) {
        this(new ClassFileParser(), containers);
    }

    public JavaClassBuilder(AbstractParser parser, ClassContainers classContainers) {
        this.parser = parser;
        this.classContainers = classContainers;
    }

    public JavaClassBuilder(AbstractParser parser) {
        this(parser, new ClassContainers());
    }

    public int countClasses() {
        AbstractParser counter = new AbstractParser() {

            public JavaClass parse(InputStream is) {
                return new JavaClass("");
            }
        };

        JavaClassBuilder builder = new JavaClassBuilder(counter, classContainers);
        Collection classes = builder.build();
        return classes.size();
    }

    /**
     * Builds the <code>JavaClass</code> instances.
     * 
     * @return Collection of <code>JavaClass</code> instances.
     */
    public Collection build() {

        Collection classes = new ArrayList();

        for (File file : classContainers.extractFiles()) {
            try {
                classes.addAll(buildClasses(file));
            } catch (IOException ioe) {
                System.err.println("\n" + ioe.getMessage());
            }
        }

        return classes;
    }

    /**
     * Builds the <code>JavaClass</code> instances from the 
     * specified file.
     * 
     * @param file Class or Jar file.
     * @return Collection of <code>JavaClass</code> instances.
     */
    public Collection buildClasses(File file) throws IOException {

        if (classContainers.acceptClassFile(file)) {
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                return new ArrayList(Arrays.asList(parser.parse(is)));
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else if (classContainers.isValidContainer(file)) {

            JarFile jarFile = new JarFile(file);
            Collection result = buildClasses(jarFile);
            jarFile.close();
            return result;

        } else {
            throw new IOException("File is not a valid " + 
                ".class, .jar, .war, or .zip file: " + 
                file.getPath());
        }
    }

    /**
     * Builds the <code>JavaClass</code> instances from the specified 
     * jar, war, or zip file.
     * 
     * @param file Jar, war, or zip file.
     * @return Collection of <code>JavaClass</code> instances.
     */
    public Collection buildClasses(JarFile file) throws IOException {

        Collection javaClasses = new ArrayList();

        Enumeration entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = (ZipEntry) entries.nextElement();
            if (classContainers.acceptClassFileName(e.getName())) {
                InputStream is = null;
                try {
	                is = new BufferedInputStream(file.getInputStream(e));
                    javaClasses.add(parser.parse(is));
                } finally {
                    is.close();
                }
            }
        }

        return javaClasses;
    }
}
