package jdepend.framework;

import java.io.File;

abstract class ClassContainerBase {
    protected final File location;

    ClassContainerBase(String source) {
        location = new File(source);
    }

    protected boolean isNotAFile() {
        return !location.exists();
    }

    protected abstract boolean isNotAContainer();
    public File getFile() {return location;}

}
