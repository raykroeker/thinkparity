/*
 * Created On: Tue Jun 06 2006 10:31 PDT
 * $Id$
 */
package com.thinkparity.desdemona.model;

import java.io.File;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.junitx.TestCase;
import com.thinkparity.codebase.junitx.TestSession;

import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.desdemona.model.artifact.ArtifactModel;

/**
 * An abstraction of the parity remote model test case.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ModelTestCase extends TestCase {

    static {
        final TestSession testSession = getTestSession();
        final File log4jFile = new File(testSession.getOutputDirectory(), "desdemona.log4j");
        System.setProperty("thinkparity.log4j.file", log4jFile.getAbsolutePath());
    }

    /**
     * Create ModelTestCase.
     *
     * @param name
     *      The test name.
     */
    protected ModelTestCase(final String name) { super(name); }

    /**
     * Create an artifact.
     *
     * @return The new artifact.
     */
    protected Artifact createArtifact() {
        throw Assert.createNotYetImplemented("ModelTestCase#createArtifact");
    }

    /**
     * Obtain the parity artifact interface.
     *
     * @return The parity artifact interface.
     */
    protected ArtifactModel getArtifactModel() {
        return null;
    }

    /** @see com.raykroeker.junitx.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected File getOutputDirectory(final String child) {
        final File outputDirectory =
            new File(getOutputDirectory(), child);
        if (!outputDirectory.exists()) {
            Assert.assertTrue(outputDirectory.mkdir(), "Could not create directory {0}.", outputDirectory);
        }
        return outputDirectory;
    }

    protected File getOutputDirectory() {
        final File outputDirectory =
            new File(getTestSession().getOutputDirectory(), getName());
        if (!outputDirectory.exists()) {
            Assert.assertTrue(outputDirectory.mkdir(), "Could not create directory {0}.", outputDirectory);
        }
        return outputDirectory;
    }

    /** @see com.raykroeker.junitx.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}