package jdepend.framework;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The <code>FileManager</code> class is responsible for extracting 
 * Java class files (<code>.class</code> files) from a collection of 
 * registered directories.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManager {

    private ArrayList<ClassContainer> classContainers = new ArrayList<>();
    private boolean acceptInnerClasses;


    public FileManager() {
        acceptInnerClasses = true;
    }

    /**
     * Determines whether inner classes should be collected.
     * 
     * @param b <code>true</code> to collect inner classes; 
     *          <code>false</code> otherwise.
     */
    public void acceptInnerClasses(boolean b) {
        acceptInnerClasses = b;
    }

    public void addDirectory(String fileName) throws IOException {
        classContainers.add(ClassContainerFactory.getContainer(fileName));
    }

    public boolean acceptClassFile(File file) {
        return file.isFile() && acceptClassFileName(file.getName());
    }

    public boolean acceptClassFileName(String name) {

        if (!acceptInnerClasses) {
            if (name.toLowerCase().indexOf("$") > 0) {
                return false;
            }
        }

        if (!name.toLowerCase().endsWith(".class")) {
            return false;
        }

        return true;
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    public Collection<File> extractFiles() {

        Collection files = new TreeSet();

        for (ClassContainer container : classContainers ) {
            files.addAll(collectFiles(container));
        }

        return files;
    }

    private Collection<File> collectFiles(ClassContainer container) {
        if (container instanceof ArchiveClassContainer) { return container.collectFiles(); }
        return collectFiles(container.getFile());
    }

    private Collection collectFiles(File directory) {

        Collection files = new TreeSet();
        for ( String fileName : directory.list()) {
            File file = new File(directory, fileName);
            if (acceptFile(file)) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(collectFiles(file));
            }
        }

        return (Collection) files.stream().distinct().collect(Collectors.toList());
    }

    private boolean acceptFile(File file) {
        return acceptClassFile(file) || isValidContainer(file);
    }

    private boolean isWar(File file) {
        return existsWithExtension(file, ".war");
    }

    private boolean isZip(File file) {
        return existsWithExtension(file, ".zip");
    }
 
    private boolean isJar(File file) { return existsWithExtension(file, ".jar"); }

    private boolean existsWithExtension(File file, String extension) {
        return file.isFile() &&
            file.getName().toLowerCase().endsWith(extension);
    }

}