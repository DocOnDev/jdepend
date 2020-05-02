package jdepend.framework;

import java.io.IOException;

public class DirectoryClassContainer extends ClassContainer {
    public DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() { return !getFile().isDirectory(); }
}