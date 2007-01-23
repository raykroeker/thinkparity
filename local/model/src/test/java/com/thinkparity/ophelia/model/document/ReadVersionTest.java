/*
 * Dec 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model getVersion api.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadVersionTest extends DocumentTestCase {

	private Vector<Fixture> data;

	/**
	 * Create a GetVersionTest.
	 */
	public ReadVersionTest() { super("testGetVersion"); }

	/**
	 * Test the document model getVersion api.
	 *
	 */
	public void testGetVersion() {
		try {
			DocumentVersion documentVersion;
			for(Fixture datum : data) {
				documentVersion =
					datum.documentModel.readVersion(datum.documentId, datum.versionId);

				assertNotNull(documentVersion);
				assertEquals(datum.documentVersion.getArtifactId(), documentVersion.getArtifactId());
				assertEquals(datum.documentVersion.getArtifactType(), documentVersion.getArtifactType());
				assertEquals(datum.documentVersion.getArtifactUniqueId(), documentVersion.getArtifactUniqueId());
				assertEquals(datum.documentVersion.getCreatedBy(), documentVersion.getCreatedBy());
				assertEquals(datum.documentVersion.getCreatedOn(), documentVersion.getCreatedOn());
				assertEquals(datum.documentVersion.getMetaData(), documentVersion.getMetaData());
				assertEquals(datum.documentVersion.getName(), documentVersion.getName());
				assertEquals(datum.documentVersion.getUpdatedBy(), documentVersion.getUpdatedBy());
				assertEquals(datum.documentVersion.getUpdatedOn(), documentVersion.getUpdatedOn());
				assertEquals(datum.documentVersion.getVersionId(), documentVersion.getVersionId());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final InternalDocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
		data = new Vector<Fixture>(getInputFilesLength());

		Document document;
		DocumentVersion documentVersion;
		for(File testFile : getInputFiles()) {
			document = create(OpheliaTestUser.JUNIT, testFile);
			documentModel.createVersion(document.getId(), currentDateTime());
			documentVersion = documentModel.readVersions(document.getId()).iterator().next();
			data.add(new Fixture(document.getId(), documentModel, documentVersion, documentVersion.getVersionId()));
		}
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		data.clear();
		data = null;
	}

	private class Fixture {
		private final Long documentId;
		private final InternalDocumentModel documentModel;
		private final DocumentVersion documentVersion;
		private final Long versionId;
		private Fixture(final Long documentId,
				final InternalDocumentModel documentModel,
				final DocumentVersion documentVersion, final Long versionId) {
			super();
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.documentVersion = documentVersion;
			this.versionId = versionId;
		}
	}
}
