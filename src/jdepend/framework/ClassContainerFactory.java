package jdepend.framework;

import java.io.File;
import java.io.IOException;

public class ClassContainerFactory {
    public static ClassContainerBase getContainer(String source) throws IOException {
        if (new File(source).isDirectory()) return new DirectoryClassContainer(source);
        return new ArchiveClassContainer(source);
    }
}
