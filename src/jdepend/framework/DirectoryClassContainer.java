package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DirectoryClassContainer extends ClassContainer {
    public DirectoryClassContainer(String source) throws IOException {
        super(source);
    }

    @Override
    protected boolean isNotAContainer() {
        return !getFile().isDirectory();
    }

    @Override
    public Collection<JavaClass> buildClasses(boolean acceptInnerClasses, AbstractParser parser) throws IOException {
        File directory = getFile();

        Collection<JavaClass> classes = new ArrayList<>();

        for (String fileName : directory.list()) {
            File file = new File(directory, fileName);
            try {
                ClassContainer subContainer = ClassContainerFactory.getContainer(file.getPath());
                classes.addAll(subContainer.buildClasses(acceptInnerClasses, parser));
            } catch (IOException e) {
                if (acceptClassFileName(file.getName(), acceptInnerClasses)) {
                    classes.addAll(parseFromSource(parser, new StreamSource(file)));
                }
            }
        }
        return classes;
    }
}
