package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

class ClassContainers extends ArrayList<ClassContainer> {
    private boolean acceptInnerClasses = true;

    /**
     * Determines whether inner classes should be collected.
     *
     * @param b <code>true</code> to collect inner classes;
     *          <code>false</code> otherwise.
     */
    public void acceptInnerClasses(boolean b) {
        acceptInnerClasses = b;
    }

    public boolean acceptInnerClasses() {
        return this.acceptInnerClasses;
    }

    public boolean acceptClassFileName(String name) {
        return ClassContainer.acceptClassFileName(name, acceptInnerClasses());
    }

    public Collection build(AbstractParser parser) {

        Collection javaClasses = new ArrayList();
        for (ClassContainer container : this) {
            javaClasses.addAll(container.buildClasses(acceptInnerClasses, parser));
        }

        Collection classes = new ArrayList();

        for (File file : extractFiles()) {
            try {
                classes.addAll(buildClasses(parser, file));
            } catch (IOException ioe) {
                System.err.println("\n" + ioe.getMessage());
            }
        }

        return classes;
    }

    Collection<File> extractFiles() {
        Collection files = new TreeSet();
        for (ClassContainer container : this) {
            files.addAll(container.collectFiles(acceptInnerClasses()));
        }
        return files;
    }

    Collection buildClasses(AbstractParser parser, File file) throws IOException {
        if (!isAcceptableClassFile(file) && !isValidContainer(file)) {
            throw new IOException("File is not a valid .class, .jar, .war, or .zip file: " + file.getPath());
        }

        Collection result = new ArrayList();
        if (isAcceptableClassFile(file)) {
            result.addAll(parseFromSource(parser, new StreamSource(file)));
        } else if (isValidContainer(file)) {
            JarFile jarFile = new JarFile(file);
            for (ZipEntry entry : getJarFileEntries(jarFile)) {
                result.addAll(parseFromSource(parser, new StreamSource(jarFile, entry)));
            }
            jarFile.close();
        }
        return result;
    }

    boolean existsWithExtension(File file, String extension) {
        return file.isFile() &&
                file.getName().toLowerCase().endsWith(extension);
    }

    boolean isJar(File file) {
        return existsWithExtension(file, ".jar");
    }

    boolean isZip(File file) {
        return existsWithExtension(file, ".zip");
    }

    boolean isWar(File file) {
        return existsWithExtension(file, ".war");
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    boolean isAcceptableClassFile(File file) {
        return file.isFile() && acceptClassFileName(file.getName());
    }

    Collection parseFromSource(AbstractParser parser, StreamSource streamSource) throws IOException {
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

    List<ZipEntry> getJarFileEntries(JarFile jarFile) {
        return jarFile.stream()
                .filter(entry -> acceptClassFileName(entry.getName()))
                .collect(Collectors.toList());
    }
}
