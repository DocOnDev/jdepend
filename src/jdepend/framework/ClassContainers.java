package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
            try {
                javaClasses.addAll(container.buildClasses(acceptInnerClasses, parser));
            } catch (IOException e) {
                System.err.println("\n" + e.getMessage());
            }
        }

        return javaClasses;
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
