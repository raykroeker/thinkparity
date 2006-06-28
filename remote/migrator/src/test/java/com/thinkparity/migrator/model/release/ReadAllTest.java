/*
 * Created On: Thu May 11 2006 08:25 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.controller.ControllerTestCase;

/**
 * A test for the release model's read all api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadAllTest extends ControllerTestCase {

    /** The test name. */
    private static final String NAME = "[RMIGRATOR] [MODEL] [RELEASE] [READ ALL TEST]";

    /** The test data. */
    private Fixture datum;

    /** Create ReadAllTest. */
    public ReadAllTest() { super(NAME); }

    /** Test the handle IQ api. */
    public void testHandleIQ() {
        final List<Release> releases = datum.rModel.readAll();

        final String assertion = NAME + " [EXPECTED RELEASES DOES NOT CONTAIN RELEASE] [{0}]";
        for(final Release release : releases) {
            assertTrue(MessageFormat.format(assertion, new Object[] {release}), datum.eReleases.contains(release));
        }
        for(final Release release : datum.eReleases) {
            assertTrue(MessageFormat.format(assertion, new Object[] {release}), releases.contains(release));
        }
    }
   
    /** Set up the test data. */
    protected void setUp() throws Exception {
        final ReleaseModel rModel = getReleaseModel(getClass());

        final List<Library> eLibraries = new LinkedList<Library>();
        eLibraries.add(createJavaLibrary());
        eLibraries.add(createNativeLibrary());

        final Release eRelease = getRelease();
        eRelease.setId(rModel.create(eRelease.getArtifactId(),
                eRelease.getGroupId(), eRelease.getVersion(), eLibraries)
                .getId());

        datum = new Fixture(rModel.readAll(), rModel);
    }

    /** Tear down the test data. */
    protected void tearDown() throws Exception { datum = null; }

    /** The test data fixture. */
    private class Fixture {
        private final List<Release> eReleases;
        private final ReleaseModel rModel;
        private Fixture(final List<Release> eReleases, final ReleaseModel rModel) {
            this.eReleases = eReleases;
            this.rModel = rModel;
        }
    }
}
