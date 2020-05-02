package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

abstract class ClassContainer {
    protected final File location;

    ClassContainer(String source) throws IOException {
        location = new File(source);
        if (isNotAFile() || isNotAContainer()) {
            throw new IOException("Invalid directory or Container file: " + source);
        }
    }

    protected boolean isNotAFile() {
        return !location.exists();
    }

    protected abstract boolean isNotAContainer();

    public File getFile() {
        return location;
    }

    boolean acceptClassFileName(String name, boolean allowInnerClasses) {
        if (!allowInnerClasses && name.toLowerCase().indexOf("$") > 0) {
            return false;
        }

        return name.toLowerCase().endsWith(".class");
    }


    protected abstract ArrayList<File> collectFiles(boolean allowInnerClasses);
}
