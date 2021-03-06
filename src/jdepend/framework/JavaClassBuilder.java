package jdepend.framework;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

        if (!classContainers.isAcceptableClassFile(file) && !classContainers.isValidContainer(file)) {
            throw new IOException("File is not a valid .class, .jar, .war, or .zip file: " + file.getPath());
        }

        Collection result = new ArrayList();
        if (classContainers.isAcceptableClassFile(file)) {
            result.addAll(parseFromSource(new StreamSource(file)));
        } else if (classContainers.isValidContainer(file)) {
            JarFile jarFile = new JarFile(file);
            for (ZipEntry entry : getJarFileEntries(jarFile)) {
                result.addAll(parseFromSource(new StreamSource(jarFile, entry)));
            }
            jarFile.close();
        }
        return result;
    }

    private Collection parseFromSource(StreamSource streamSource) throws IOException {
        InputStream is = null;
        Collection parsedResult = new ArrayList();
        try {
            is = streamSource.invoke();
            parsedResult.add(parser.parse(is));
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return parsedResult;
    }

    private List<ZipEntry> getJarFileEntries(JarFile jarFile) {
        return jarFile.stream()
                .filter(entry -> classContainers.acceptClassFileName(entry.getName()))
                .collect(Collectors.toList());
    }

    private class StreamSource {
        private final JarFile jarFile;
        private final ZipEntry entry;
        private final File file;

        public StreamSource(File file) {
            this.file = file;
            this.jarFile = null;
            this.entry = null;
        }

        public StreamSource(JarFile jarFile, ZipEntry entry) {
            this.jarFile = jarFile;
            this.entry = entry;
            this.file = null;
        }

        public BufferedInputStream invoke() throws IOException {
            if (isZipSource()) return new BufferedInputStream(jarFile.getInputStream(entry));
            return new BufferedInputStream(new FileInputStream(file));
        }

        private boolean isZipSource() {
            return this.jarFile != null && this.entry != null;
        }
    }
}
