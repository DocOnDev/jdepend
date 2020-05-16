package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class DirectoryClassContainer extends ClassContainer {
    public DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() { return !getFile().isDirectory(); }

    @Override
    protected Collection<File> collectFiles() {
        return null;
    }
}
