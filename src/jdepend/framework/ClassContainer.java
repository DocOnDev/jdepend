package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

abstract class ClassContainer {
    protected final File location;

    ClassContainer(String source) throws IOException {
        location = new File(source);
        if (isNotAFile() || isNotAContainer()) {
            throw new IOException("Invalid directory or Container file: " + source);
        }
    }

    public static boolean acceptClassFileName(String name, boolean acceptInnerClasses) {
        if (!acceptInnerClasses && (name.toLowerCase().indexOf("$") > 0)) return false;
        return name.toLowerCase().endsWith(".class");
    }

    protected boolean isNotAFile() {
        return !location.exists();
    }

    protected abstract boolean isNotAContainer();

    public File getFile() {
        return location;
    }

    protected abstract Collection<File> collectFiles(Boolean acceptInnerClasses);

    public abstract Collection<JavaClass> buildClasses(boolean acceptInnerClasses, AbstractParser parser);
}
