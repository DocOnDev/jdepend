package jdepend.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

class ClassContainers extends ArrayList<ClassContainer> {
    private boolean acceptInnerClasses;

    /**
     * Determines whether inner classes should be collected.
     *  @param b <code>true</code> to collect inner classes;
     *          <code>false</code> otherwise.
     *
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
}
