package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DirectoryClassContainer extends ClassContainer {
    public DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() { return !getFile().isDirectory(); }

    @Override
    protected ArrayList<File> collectFiles(boolean allowInnerFiles) {
        ArrayList<File> files = new ArrayList<>();
        for ( File file : this.getFiles() ) {
            try {
                ClassContainer subContainer = ClassContainerFactory.getContainer(file.getPath());
                files.addAll(subContainer.collectFiles(allowInnerFiles));
            } catch (IOException e) {
                if (acceptClassFileName(file.getName(), allowInnerFiles)) {
                    files.add(file);
                }
            }
        }

        return (ArrayList<File>) files.stream().distinct().collect(Collectors.toList());
    }

    private Collection<File> getFiles() {
        Collection<File> files = new ArrayList<>();
        for (String fileName : getFile().list()) {
            files.add(new File(getFile(), fileName));
        }
        return files;
    }
}
