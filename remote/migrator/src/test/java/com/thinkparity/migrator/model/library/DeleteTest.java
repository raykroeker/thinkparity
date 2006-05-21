/*
 * Created On: Fri May 19 2006 10:18 PDT
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;

/**
 * Test the parity library interface's delete api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class DeleteTest extends MigratorTestCase {

    /** Test data. */
    private Fixture datum;

    /** Create DeleteTest. */
    public DeleteTest() {
        super("[RMIGRATOR] [LIBRARY] [DELETE TEST]");
    }

    /** Test the delete api. */
    public void testDelete() {
        datum.lModel.delete(datum.libraryId);

        final Library library = datum.lModel.read(datum.libraryId);
        assertNull("[RMIGRATOR] [LIBRARY] [DELETE TEST] [LIBRARY IS NOT NULL]", library);
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        final LibraryModel lModel = getLibraryModel(getClass());

        final Library eLibrary = createJavaLibrary();
        datum = new Fixture(eLibrary.getId(), lModel);
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    private class Fixture {
        private final Long libraryId;
        private final LibraryModel lModel;
        private Fixture(final Long libraryId, final LibraryModel lModel) {
            this.libraryId = libraryId;
            this.lModel = lModel;
        }
    }
}
