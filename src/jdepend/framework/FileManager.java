package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The <code>FileManager</code> class is responsible for extracting
 * Java class files (<code>.class</code> files) from a collection of
 * registered directories.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManager {

    ClassContainers classContainers = new ClassContainers();


    public FileManager() {
        classContainers.acceptInnerClasses(true);
    }

    public void addDirectory(String fileName) throws IOException {
        classContainers.add(ClassContainerFactory.getContainer(fileName));
    }

    public boolean acceptClassFile(File file) {
        return file.isFile() && acceptClassFileName(file.getName());
    }

    public boolean acceptClassFileName(String name) {

        if (!classContainers.acceptInnerClasses()) {
            if (name.toLowerCase().indexOf("$") > 0) {
                return false;
            }
        }

        return name.toLowerCase().endsWith(".class");
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    public Collection<File> extractFiles() {
        return extractFiles(classContainers);
    }

    private Collection<File> extractFiles(ClassContainers classContainers) {

        Collection files = new TreeSet();

        for (ClassContainer container : classContainers) {
            files.addAll(collectFiles(container));
        }

        return files;
    }

    private Collection collectFiles(ClassContainer container) {
        if (container instanceof ArchiveClassContainer) {
            return container.collectFiles();
        }
        File directory = container.getFile();

        Collection<File> files = new ArrayList<>();

        for (String fileName : directory.list()) {
            File file = new File(directory, fileName);
            ClassContainer subContainer = null;
            try {
                subContainer = ClassContainerFactory.getContainer(file.getPath());
                files.addAll(collectFiles(subContainer));
            } catch (IOException e) {
                if (acceptClassFileName(file.getName())) {
                    files.add(file);
                }
            }
        }
        return files.stream().distinct().collect(Collectors.toList());
    }

    private boolean acceptFile(File file) {
        return acceptClassFile(file) || isValidContainer(file);
    }

    private boolean isWar(File file) {
        return existsWithExtension(file, ".war");
    }

    private boolean isZip(File file) {
        return existsWithExtension(file, ".zip");
    }

    private boolean isJar(File file) {
        return existsWithExtension(file, ".jar");
    }

    private boolean existsWithExtension(File file, String extension) {
        return file.isFile() &&
                file.getName().toLowerCase().endsWith(extension);
    }

}