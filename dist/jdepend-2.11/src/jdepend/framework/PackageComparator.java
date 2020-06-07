package jdepend.framework;

import java.util.Comparator;

/**
 * The <code>PackageComparator</code> class is a <code>Comparator</code>
 * used to compare two <code>JavaPackage</code> instances for order using a
 * sorting strategy.
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class PackageComparator implements Comparator {

    private static final PackageComparator byName;

    static {
        byName = new PackageComparator();
    }

    private PackageComparator byWhat;

    private PackageComparator() {
    }

    public PackageComparator(PackageComparator byWhat) {
        this.byWhat = byWhat;
    }

    public static PackageComparator byName() {
        return byName;
    }

    public PackageComparator byWhat() {
        return byWhat;
    }

    public int compare(Object p1, Object p2) {

        JavaPackage a = (JavaPackage) p1;
        JavaPackage b = (JavaPackage) p2;

        if (byWhat() == byName()) {
            return a.getName().compareTo(b.getName());
        }

        return 0;
    }
}