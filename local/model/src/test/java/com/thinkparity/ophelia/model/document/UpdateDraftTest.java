/*
 * Created On: Apr 7, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.thinkparity.ophelia.model.document.Document;
import com.thinkparity.ophelia.model.document.DocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateDraftTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[UPDATE DRAFT]";

    /** Test datum. */
    private Fixture datum;

    /** Create UpdateDraftTest. */
    public UpdateDraftTest() { super(NAME); }

    /** Test the update draft api. */
    public void testUpdateDraft() {
        datum.documentModel.updateDraft(datum.document.getId(), datum.modFileContent);

        assertTrue(NAME + " [DRAFT NOT MODIFIED]",
                datum.documentModel.isDraftModified(datum.document.getId()));
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final File inputFile = getInputFiles()[0];
        final File modFile = getModFiles()[0];
        final DocumentModel documentModel = getDocumentModel();
        final Document document = createDocument(inputFile);
        datum = new Fixture(document, documentModel, new FileInputStream(modFile));
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum.modFileContent.close();
        datum = null;
        super.tearDown();
    }

    private class Fixture {
        private final Document document;
        private final DocumentModel documentModel;
        private final InputStream modFileContent;
        private Fixture(final Document document,
                final DocumentModel documentModel,
                final InputStream modFileContent) {
            this.document = document;
            this.documentModel = documentModel;
            this.modFileContent = modFileContent;
        }
    }
}
