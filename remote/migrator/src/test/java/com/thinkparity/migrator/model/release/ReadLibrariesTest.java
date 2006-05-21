/*
 * Created On: Fri May 19 2006 12:22 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import java.util.List;
import java.util.LinkedList;
import java.text.MessageFormat;

import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * Test the parity release interface's read libraries api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadLibrariesTest extends MigratorTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create ReadLibrariesTest. */
    public ReadLibrariesTest() {
        super("[RMIGRATOR] [RELEASE] [READ LIBRARIES TEST]");
    }

    /** Test the read api. */
    public void testReadLibraries() {
        final List<Library> libraries = datum.rModel.readLibraries(datum.releaseId);

        final String assertion = "[RMIGRATOR] [RELEASE] [READ LIBRARIES TEST] [EXPECTED LIBRARIES DOES NOT CONTAIN LIBRARY] [{0}]";
        for(final Library library : libraries) {
            assertTrue(MessageFormat.format(assertion, new Object[] {library}), datum.eLibraries.contains(library));
        }
        for(final Library library : datum.eLibraries) {
            assertTrue(MessageFormat.format(assertion, new Object[] {library}), libraries.contains(library));
        }
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final ReleaseModel rModel = getReleaseModel(getClass());

        final List<Library> eLibraries = new LinkedList<Library>();
        eLibraries.add(createJavaLibrary());
        eLibraries.add(createNativeLibrary());

        final Release eRelease = getRelease();
        eRelease.setId(rModel.create(eRelease.getArtifactId(),
                eRelease.getGroupId(), eRelease.getVersion(), eLibraries)
                .getId());
        datum = new Fixture(eLibraries, rModel, eRelease.getId());
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
        datum = null;
    }

    private class Fixture {
        private final List<Library> eLibraries;
        private final ReleaseModel rModel;
        private final Long releaseId;
        private Fixture(final List<Library> eLibraries, final ReleaseModel rModel,
                final Long releaseId) {
            this.eLibraries = eLibraries;
            this.rModel = rModel;
            this.releaseId = releaseId;
        }
    }
}
