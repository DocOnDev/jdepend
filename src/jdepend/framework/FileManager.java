package jdepend.framework;

import java.io.*;
import java.util.*;

/**
 * The <code>FileManager</code> class is responsible for extracting 
 * Java class files (<code>.class</code> files) from a collection of 
 * registered directories.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManager {

    private ArrayList<File> directories;
    private boolean acceptInnerClasses;


    public FileManager() {
        directories = new ArrayList();
        acceptInnerClasses = true;
    }

    /**
     * Determines whether inner classes should be collected.
     * 
     * @param b <code>true</code> to collect inner classes; 
     *          <code>false</code> otherwise.
     */
    public void acceptInnerClasses(boolean b) {
        acceptInnerClasses = b;
    }

    public void addDirectory(String name) throws IOException {

        File directory = new File(name);

        if (isDirectoryOrContainer(directory)) {
            directories.add(directory);
        } else {
            throw new IOException("Invalid directory or Container file: " + name);
        }
    }

    private boolean isDirectoryOrContainer(File directory) {
        return directory.isDirectory() || isValidContainer(directory);
    }

    public boolean acceptFile(File file) {
        return acceptClassFile(file) || isValidContainer(file);
    }

    public boolean acceptClassFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        return acceptClassFileName(file.getName());
    }

    public boolean acceptClassFileName(String name) {

        if (!acceptInnerClasses) {
            if (name.toLowerCase().indexOf("$") > 0) {
                return false;
            }
        }

        if (!name.toLowerCase().endsWith(".class")) {
            return false;
        }

        return true;
    }

    public boolean isValidContainer(File file) {
        return isJar(file) || isZip(file) || isWar(file);
    }

    public Collection<File> extractFiles() {

        Collection files = new TreeSet();

        for (File directory : directories ) {
            collectFiles(directory, files);
        }

        return files;
    }

    private void collectFiles(File directory, Collection files) {

        if (directory.isFile()) {

            addFile(directory, files);

        } else {

            String[] directoryFiles = directory.list();

            for (int i = 0; i < directoryFiles.length; i++) {

                File file = new File(directory, directoryFiles[i]);
                if (acceptFile(file)) {
                    addFile(file, files);
                } else if (file.isDirectory()) {
                    collectFiles(file, files);
                }
            }
        }
    }

    private void addFile(File f, Collection files) {
        if (!files.contains(f)) {
            files.add(f);
        }
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