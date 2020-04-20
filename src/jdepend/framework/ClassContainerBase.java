package jdepend.framework;

import java.io.File;
import java.io.IOException;

abstract class ClassContainerBase {
    protected final File location;

    ClassContainerBase(String source) throws IOException {
        location = new File(source);
        if (isNotAFile() || isNotAContainer()) {
            throw new IOException("Invalid directory or Container file: " + source);
        }

    }

    protected boolean isNotAFile() {
        return !location.exists();
    }

    protected abstract boolean isNotAContainer();
    public File getFile() {return location;}

}
