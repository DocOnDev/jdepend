package jdepend.framework;

import java.io.File;
import java.io.IOException;

abstract class ClassContainer {
    protected final File location;

    ClassContainer(String source) throws IOException {
        location = new File(source);
        if (!location.exists() || isNotAContainer()) {
            throw new IOException("Invalid directory or Container file: " + source);
        }
    }

    protected abstract boolean isNotAContainer();
    public File getFile() {return location;}

}
