package jdepend.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

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

    Collection<File> extractFiles() {

        Collection files = new TreeSet();

        for (ClassContainer container : this) {
            files.addAll(container.collectFiles(acceptInnerClasses()));
        }

        return files;
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
}
