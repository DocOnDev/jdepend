package jdepend.framework;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

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

    public JavaClassBuilder(ClassContainers containers) {
        this(new ClassFileParser(), containers);
    }

    public JavaClassBuilder(AbstractParser parser, ClassContainers containers) {
        this.parser = parser;
        this.classContainers = containers;
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

        if (classContainers.isNotClassOrContainer(file)) {
            throw new IOException("File is not a valid .class, .jar, .war, or .zip file: " + file.getPath());
        }

        Collection javaClasses = new ArrayList();

        if (classContainers.isClassFile(file)) {
            javaClasses.add(parseClassFile(new StreamSource(file)));
        } else if (classContainers.isValidContainer(file)) {
            JarFile jarFile = new JarFile(file);
            for ( ZipEntry zipEntry : getClassesFromJarFile(jarFile)) {
                javaClasses.add(parseClassFile(new StreamSource(jarFile, zipEntry)));
            }
            jarFile.close();
        }
        return javaClasses;
    }

    private Collection<ZipEntry> getClassesFromJarFile(JarFile jarFile) {
        return jarFile.stream()
                        .filter(entry -> classContainers.acceptClassFileName(entry.getName()))
                        .collect(Collectors.toList());
    }

    private JavaClass parseClassFile(StreamSource source) throws IOException {
        InputStream inStream = null;
        JavaClass parsedClass;
        try {
            inStream = source.getStream();
            parsedClass = parser.parse(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return parsedClass;
    }

    private static class StreamSource {
        File file;
        JarFile jarFile;
        ZipEntry zipEntry;

        public StreamSource(JarFile jarFile, ZipEntry zipEntry) {
            this.jarFile = jarFile;
            this.zipEntry = zipEntry;
        }

        public StreamSource(File file) {
            this.file = file;
        }

        public boolean isZipEntry() {
            return this.jarFile != null && this.zipEntry != null;
        }

        BufferedInputStream getStream() throws IOException {
            if (isZipEntry()) return new BufferedInputStream(jarFile.getInputStream(zipEntry));
            else return new BufferedInputStream(new FileInputStream(file));
        }
    }

}
