package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * The <code>FileManager</code> class is responsible for extracting
 * Java class files (<code>.class</code> files) from a collection of
 * registered directories.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManager {

    ClassContainers classContainers = new ClassContainers();


    public FileManager() {
        classContainers.acceptInnerClasses(true);
    }

    public void addDirectory(String fileName) throws IOException {
        classContainers.add(ClassContainerFactory.getContainer(fileName));
    }

    public boolean acceptClassFile(File file) {
        return file.isFile() && classContainers.acceptClassFileName(file.getName());
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    public Collection<File> extractFiles() {
        return classContainers.extractFiles();
    }

    private boolean isWar(File file) {
        return existsWithExtension(file, ".war");
    }

    private boolean isZip(File file) {
        return existsWithExtension(file, ".zip");
    }

    private boolean isJar(File file) {
        return existsWithExtension(file, ".jar");
    }

    private boolean existsWithExtension(File file, String extension) {
        return file.isFile() &&
                file.getName().toLowerCase().endsWith(extension);
    }

}