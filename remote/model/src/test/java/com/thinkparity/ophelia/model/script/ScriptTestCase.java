/*
 * Created On: Oct 15, 2006 3:13:03 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.File;

import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ScriptTestCase extends ModelTestCase {

    /** Create ScriptTestCase. */
    protected ScriptTestCase(final String name) {
        super(name);
    }

    /**
     * Obtain an output directory for a script test case. This will locate
     * the output directory in a per-test director beneath a common container
     * root.
     * 
     * @return A directory <code>File</code>.
     */
    @Override
    public File getOutputDirectory() {
        final File parentFile = new File(super.getOutputDirectory(), "Script");
        final File outputDirectory = new File(parentFile, getName());
        if (!outputDirectory.exists())
            assertTrue(outputDirectory.mkdirs());
        return outputDirectory;
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** Abstract script datum definition. */
    protected abstract class Fixture {}
}
