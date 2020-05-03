package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
        ArrayList<File> files = new ArrayList<>();
        for (ClassContainer container : this ) {
            files.addAll(container.collectFiles(this.innerClasses));
        }
        return files;
    }

    public boolean acceptClassFile(File file) {
        return file.isFile() && acceptClassFileName(file.getName());
    }

    boolean existsWithExtension(File file, String extension) {
        return file.isFile() &&
            file.getName().toLowerCase().endsWith(extension);
    }

    boolean isJar(File file) { return existsWithExtension(file, ".jar"); }

    boolean isZip(File file) {
        return existsWithExtension(file, ".zip");
    }

    boolean isWar(File file) {
        return existsWithExtension(file, ".war");
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    public void addDirectory(String fileName) throws IOException {
        add(ClassContainerFactory.getContainer(fileName));
    }
}
