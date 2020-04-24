package jdepend.framework;

import java.io.File;
import java.io.IOException;

public class ClassContainer extends ClassContainerBase {

    public ClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !location.isDirectory() && !isContainerFile();
    }

    private boolean isContainerFile() {
        return hasExtension("war") || hasExtension("jar") || hasExtension("zip");
    }

    private boolean hasExtension(String ext) {
        return location.getName().endsWith(ext);
    }

    public File getFile() {
        return location;
    }
}
