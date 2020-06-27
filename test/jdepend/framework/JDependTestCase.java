package jdepend.framework;

import junit.framework.TestCase;
import org.junit.Ignore;

import java.io.File;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JDependTestCase extends TestCase {

    private String homeDir;
    private String testDir;
    private String testDataDir;
    private String buildDir;
    private String packageSubDir;
    private String originalUserHome;
    private String jDependDir;


    public JDependTestCase(String name) {
        super(name);
    }

    protected void setUp() {
        homeDir = "./";
        testDir = homeDir + "test" + File.separator;
        testDataDir = testDir + "data" + File.separator;
        buildDir = homeDir + "test_build" + File.separator;
        jDependDir = "jdepend" + File.separator;
        packageSubDir = jDependDir + "framework" + File.separator;
        originalUserHome = System.getProperty("user.home");
    }

    protected void tearDown() {
        System.setProperty("user.home", originalUserHome);
    }

    public String getHomeDir() {
        return homeDir;
    }

    public String getJDependDir() { return jDependDir; }

    public String getTestDataDir() {
        return testDataDir;
    }
    
    public String getTestDir() {
        return testDir;
    }

    public String getBuildDir() {
        return buildDir;
    }
    
    public String getPackageSubDir() {
        return packageSubDir;
    }

    // Added to resolve a junit error
    @Ignore
    public void testFake() {
        assertTrue(true);
    }
}