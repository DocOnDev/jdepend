package jdepend.framework;

import java.util.ArrayList;

class ClassContainers extends ArrayList<ClassContainer> {
    private boolean acceptInnerClasses;

    /**
     * Determines whether inner classes should be collected.
     *  @param b <code>true</code> to collect inner classes;
     *          <code>false</code> otherwise.
     *
     */
    public void acceptInnerClasses(boolean b) {
        acceptInnerClasses = b;
    }

    public boolean acceptInnerClasses() {
        return this.acceptInnerClasses;
    }
}
