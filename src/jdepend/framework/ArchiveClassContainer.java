package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ArchiveClassContainer extends ClassContainer {
    public ArchiveClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !isContainerFile();
    }

    @Override
    protected Collection<File> collectFiles(Boolean acceptInnerClasses) {
        return new ArrayList(Arrays.asList(getFile()));
    }

    @Override
    public Collection<JavaClass> buildClasses(Boolean acceptInnerClasses, AbstractParser parser) {
        return new ArrayList<>();
    }

    private boolean isContainerFile() {
        return hasExtension("war") || hasExtension("jar") || hasExtension("zip");
    }

    private boolean hasExtension(String ext) {
        return getFile().getName().endsWith(ext);
    }

}
