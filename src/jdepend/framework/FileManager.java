package jdepend.framework;

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

}