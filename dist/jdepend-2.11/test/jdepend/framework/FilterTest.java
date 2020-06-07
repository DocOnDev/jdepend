package jdepend.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FilterTest extends JDependTestCase {

    private String explicitFileName;

    public FilterTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        System.setProperty("user.home", getTestDataDir());
        explicitFileName = getTestDataDir() + "explicit.properties";
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testDefaultPropertiesHasFiveFilters() {
        PackageFilter filter = new PackageFilter();
        assertEquals(5, filter.getFilters().size());
    }

    public void testDefaultPropertiesHasAllFilters() {
        PackageFilter filter = new PackageFilter();
        assertAllFiltersExist(filter);
    }

    public void testMissingFileReportsNoFilters() {
        PackageFilter filter = getFiltersFromFile("bogus.txt");
        assertEquals(0, filter.getFilters().size());
    }

    public void testPassedInFileHasThreeFilters() throws IOException {
        PackageFilter filter = getFiltersFromFile(explicitFileName);
        assertEquals(3, filter.getFilters().size());
    }

    public void testPassedInFileHasOnlySunAndXyzFilters() throws IOException {
        PackageFilter filter = getFiltersFromFile(explicitFileName);

        assertSunFilters(filter);
        assertXyzFilters(filter);
        assertNoJavaFilters(filter);
    }

    public void testCollectionHasTwoFilters() throws IOException {
        PackageFilter filter = getFilterFromCollection();
        assertEquals(2, filter.getFilters().size());
    }

    public void testCollectionHasNoSunFilters() throws IOException {
        PackageFilter filter = getFilterFromCollection();

        assertJavaFilters(filter);
        assertNoSunFilters(filter);
        assertNoXyzFilters(filter);
    }

    private PackageFilter getFiltersFromFile(String s) {
        return new PackageFilter(new File(s));
    }

    private PackageFilter getFilterFromCollection() {
        Collection filters = new ArrayList();
        filters.add("java.*");
        filters.add("javax.*");
        return new PackageFilter(filters);
    }

    private void assertAllFiltersExist(PackageFilter filter) {
        assertJavaFilters(filter);
        assertSunFilters(filter);
        assertXyzFilters(filter);
    }

    private void assertXyzFilters(PackageFilter filter) {
        assertFilter(filter,"com.xyz.tests.a");
        assertNoFilter(filter,"com.xyz.tests");
    }

    private void assertNoXyzFilters(PackageFilter filter) {
        assertNoFilter(filter,"com.xyz.tests.a");
    }


    private void assertSunFilters(PackageFilter filter) {
        assertFilter(filter, "sun.junk");
        assertFilter(filter, "com.sun.junk");
    }

    private void assertNoSunFilters(PackageFilter filter) {
        assertNoFilter(filter,"sun.junk");
        assertNoFilter(filter,"com.sun.junk");
    }

    private void assertJavaFilters(PackageFilter filter) {
        assertFilter(filter, "java.lang");
        assertFilter(filter, "javax.ejb");
    }

    private void assertNoJavaFilters(PackageFilter filter) {
        assertNoFilter(filter,"java.lang");
        assertNoFilter(filter,"javax.ejb");
    }

    private void assertFilter(PackageFilter filter, String filterName) {
        assertFalse("Filter " + filterName + " not present", filter.accept(filterName));
    }

    private void assertNoFilter(PackageFilter filter, String filterName) {
        assertTrue("Filter " + filterName + " is present", filter.accept(filterName));
    }

}