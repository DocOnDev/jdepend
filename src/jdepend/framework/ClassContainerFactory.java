package jdepend.framework;

import java.io.File;
import java.io.IOException;

public class ClassContainerFactory {
    public static ClassContainer getContainer(String source) throws IOException {
        if (new File(source).isDirectory()) return new DirectoryClassContainer(source);
        return new ArchiveClassContainer(source);
    }
}
