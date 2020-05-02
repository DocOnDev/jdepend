package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ArchiveClassContainer extends ClassContainer {
    public ArchiveClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() { return !isContainerFile(); }

    protected ArrayList<File> collectFiles(boolean allowInnerFiles) {
        return new ArrayList<>(Arrays.asList(getFile()));
    }

    private boolean isContainerFile() {
        return hasExtension("war") || hasExtension("jar") || hasExtension("zip");
    }

    private boolean hasExtension(String ext) {
        return getFile().getName().endsWith(ext);
    }

}
