/*
 * May 9, 2006
 */
package com.thinkparity.migrator;

import java.io.File;
import java.io.IOException;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;

import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;

/**
 * The remote migrator's test case abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class MigratorTestCase extends TestCase {

    static {
        final TestSession testSession = TestCase.getTestSession();
        final File dbDirectory = new File(testSession.getSessionDirectory(), "db.io");
        final File dbFile = new File(dbDirectory, "db");
        System.setProperty("hsqldb.file", dbFile.getAbsolutePath());
    }

    /**
     * Create MigratorTestCase.
     * 
     * @param name
     *            The test case name.
     */
    protected MigratorTestCase(final String name) { super(name); }

    protected LibraryModel getLibraryModel(final Class clasz) {
        return LibraryModel.getModel();
    }

    protected ReleaseModel getReleaseModel(final Class clasz) {
        return ReleaseModel.getModel();
    }

    protected File[] getInputFiles() throws IOException {
        return super.getInputFiles();
    }
}
