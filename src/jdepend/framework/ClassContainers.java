package jdepend.framework;

import java.util.ArrayList;

class ClassContainers extends ArrayList<ClassContainer> {
    private boolean innerClasses = true;

    public void acceptInnerClasses(boolean acceptInnerClass) {
        this.innerClasses = acceptInnerClass;
    }

    public boolean acceptInnerClasses() {
        return this.innerClasses;
    }
}
