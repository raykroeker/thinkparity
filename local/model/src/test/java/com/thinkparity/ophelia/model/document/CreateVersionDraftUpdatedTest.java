/*
 * Nov 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionDraftUpdatedTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[TEST CREATE VERSION DRAFT UPDATED]";
    
	/** Test datum. */
	private Fixture datum;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionDraftUpdatedTest() { super(NAME); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
		final DocumentVersion version = datum.documentModel.createVersion(datum.document.getId(), currentDateTime());

        assertNotNull(NAME, version);
		assertEquals(datum.document.getId(), version.getArtifactId());
		assertEquals(datum.document.getType(), version.getArtifactType());
		assertEquals(datum.document.getUniqueId(), version.getArtifactUniqueId());
		assertEquals(datum.document.getCreatedBy(), version.getCreatedBy());
		assertEquals(datum.document.getName(), version.getName());
		assertEquals(datum.document.getUpdatedBy(), version.getUpdatedBy());
    }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final InternalDocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
        final File inputFile = getInputFiles()[0];
        final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
        modifyDocument(OpheliaTestUser.JUNIT, document.getId());
		datum = new Fixture(document, documentModel);
        documentModel.addListener(datum);
    }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
        datum.documentModel.removeListener(datum);
		datum = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 */
	private class Fixture extends DocumentTestCase.Fixture {
		private final Document document;
		private final InternalDocumentModel documentModel;
		private Fixture(final Document document,
                final InternalDocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}
}
