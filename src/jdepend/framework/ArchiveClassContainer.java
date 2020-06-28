package jdepend.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class ArchiveClassContainer extends ClassContainer {
    public ArchiveClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !isContainerFile();
    }

    @Override
    public Collection<JavaClass> buildClasses(boolean acceptInnerClasses, AbstractParser parser) throws IOException {
        Collection<JavaClass> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(getFile());
        for (ZipEntry entry : getJarFileEntries(jarFile, acceptInnerClasses)) {
            classes.addAll(parseFromSource(parser, new StreamSource(jarFile, entry)));
        }
        jarFile.close();
        return classes;
    }

    List<ZipEntry> getJarFileEntries(JarFile jarFile, boolean acceptInnerClasses) {
        return jarFile.stream()
                .filter(entry -> acceptClassFileName(entry.getName(), acceptInnerClasses))
                .collect(Collectors.toList());
    }

    private boolean isContainerFile() {
        return hasExtension("war") || hasExtension("jar") || hasExtension("zip");
    }

    private boolean hasExtension(String ext) {
        return getFile().getName().endsWith(ext);
    }

}
