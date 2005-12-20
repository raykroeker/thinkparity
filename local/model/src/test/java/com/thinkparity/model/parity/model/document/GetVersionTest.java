/*
 * Dec 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model getVersion api.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetVersionTest extends ModelTestCase {

	private class Fixture {
		private final UUID documentId;
		private final DocumentModel documentModel;
		private final DocumentVersion documentVersion;
		private final String versionId;
		private Fixture(final UUID documentId,
				final DocumentModel documentModel,
				final DocumentVersion documentVersion, final String versionId) {
			super();
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.documentVersion = documentVersion;
			this.versionId = versionId;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a GetVersionTest.
	 */
	public GetVersionTest() { super("testGetVersion"); }

	/**
	 * Test the document model getVersion api.
	 *
	 */
	public void testGetVersion() {
		try {
			DocumentVersion documentVersion;
			for(Fixture datum : data) {
				documentVersion =
					datum.documentModel.getVersion(datum.documentId, datum.versionId);

				assertNotNull(documentVersion);
				assertEquals(datum.documentVersion.getDocumentId(), documentVersion.getDocumentId());
				assertEquals(datum.documentVersion.getVersionId(), documentVersion.getVersionId());
				assertEquals(datum.documentVersion.getSnapshot(), documentVersion.getSnapshot());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel documentModel = getDocumentModel();
		final Project testProject = createTestProject(getName());
		data = new Vector<Fixture>(getJUnitTestFilesSize());

		Document document;
		DocumentVersion documentVersion;
		String name, description;
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = getName() + ":  " + name;

			document = documentModel.create(testProject.getId(), name, description, testFile.getFile());
			documentVersion = documentModel.listVersions(document.getId()).iterator().next();
			data.add(new Fixture(document.getId(), documentModel, documentVersion, documentVersion.getVersionId()));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		data.clear();
		data = null;
	}


}
