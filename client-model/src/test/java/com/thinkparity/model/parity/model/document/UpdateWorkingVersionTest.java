/*
 * Apr 7, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.thinkparity.model.parity.ParityException;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UpdateWorkingVersionTest extends DocumentTestCase {

    private Set<Fixture> data;

    /**
     * Create a UpdateWorkingVersionTest.
     * 
     */
    public UpdateWorkingVersionTest() { super("UpdateWorkingVersionTest"); }

    /**
     * Test the updateWorkingVersion api.
     *
     */
    public void testUpdateWorkingVersion() {
        for(final Fixture datum : data) {
            try {
                datum.dModel.updateWorkingVersion(datum.dId, datum.updateFile);
            }
            catch(final ParityException px) { fail(createFailMessage(px)); }
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final DocumentModel dModel = getDocumentModel();

        data = new HashSet<Fixture>();

        Document d;
        final File[] inputFiles = getInputFiles();
        final File[] modFiles = getModFiles();
        for(int i = 0; i < inputFiles.length; i++) {
            d = dModel.create(inputFiles[i].getName(), null, inputFiles[i]);
            
            data.add(new Fixture(d.getId(), dModel, modFiles[i]));
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    private class Fixture {
        private final Long dId;
        private final DocumentModel dModel;
        private final File updateFile;
        private Fixture(final Long dId, final DocumentModel dModel,
                final File updateFile) {
            this.dId = dId;
            this.dModel = dModel;
            this.updateFile = updateFile;
        }
    }
}
