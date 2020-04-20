package jdepend.framework;

import java.io.IOException;

public class DirectoryClassContainer extends ClassContainerBase {

    DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !location.isDirectory();
    }
}
