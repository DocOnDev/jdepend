package jdepend.framework;

import java.io.IOException;

public class DirectoryClassContainer extends ClassContainer {

    DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !location.isDirectory();
    }
}
