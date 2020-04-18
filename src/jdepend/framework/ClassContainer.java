package jdepend.framework;

import java.io.File;
import java.io.IOException;

public class ClassContainer {

    private final File location;

    public ClassContainer(String source) throws IOException {
        location = new File(source);
        if (isNotAFile() || isNotAContainer()) {
            throw new IOException("Invalid directory or Container file: " + source);
        }
    }

    private boolean isNotAContainer() {
        return !location.isDirectory() && !isContainerFile();
    }

    private boolean isNotAFile() {
        return !location.exists();
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
