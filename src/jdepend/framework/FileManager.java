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

    ClassContainers classContainers = new ClassContainers();

    public FileManager() {
        acceptInnerClasses(true);
    }

    /**
     * Determines whether inner classes should be collected.
     * 
     * @param b <code>true</code> to collect inner classes; 
     *          <code>false</code> otherwise.
     */
    public void acceptInnerClasses(boolean b) {
        classContainers.acceptInnerClasses(b);
    }

    public Collection<File> extractFiles() {
        return classContainers.extractFiles();
    }

    public boolean acceptInnerClasses() {
        return classContainers.acceptInnerClasses();
    }
}