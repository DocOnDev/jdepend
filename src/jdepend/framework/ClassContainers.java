package jdepend.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

class ClassContainers extends ArrayList<ClassContainer> {
    private boolean innerClasses = true;

    public void acceptInnerClasses(boolean acceptInnerClass) {
        this.innerClasses = acceptInnerClass;
    }

    public boolean acceptInnerClasses() {
        return this.innerClasses;
    }

    public boolean acceptClassFileName(String name) {
        if (!acceptInnerClasses() && name.toLowerCase().indexOf("$") > 0) {
            return false;
        }

        return name.toLowerCase().endsWith(".class");
    }

    Collection<File> extractFiles() {

        Collection files = new TreeSet();

        for (ClassContainer container : this ) {
            files.addAll(container.collectFiles(this.innerClasses));
        }
        return files;
    }

}
