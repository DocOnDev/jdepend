package jdepend.framework;

import java.io.IOException;

public class ArchiveClassContainer extends ClassContainerBase {
    public ArchiveClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() { return !isContainerFile(); }

    private boolean isContainerFile() {
        return hasExtension("war") || hasExtension("jar") || hasExtension("zip");
    }

    private boolean hasExtension(String ext) {
        return location.getName().endsWith(ext);
    }

}
