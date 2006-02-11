/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 */
	private class Fixture {
		private final DocumentContent content;
		private final Document document;
		private final DocumentModel documentModel;
		private Fixture(final DocumentContent content, final Document document,
				final DocumentModel documentModel) {
			this.content = content;
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionTest() { super("testCreateVersion"); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
		try {
			Iterator<DocumentVersion> iVersions;
			DocumentVersion version;
			DocumentVersionContent versionContent;
			for(Fixture datum : data) {
				datum.documentModel.createVersion(datum.document.getId());
				// the version we want to compare to will be the last one in
				// the list
				iVersions =
					datum.documentModel.listVersions(datum.document.getId()).iterator();
				version = null;
				while(iVersions.hasNext()) { version = iVersions.next(); }

				assertNotNull(version);
				assertEquals(datum.document.getId(), version.getArtifactId());
				assertEquals(datum.document.getType(), version.getArtifactType());
				assertEquals(datum.document.getUniqueId(), version.getArtifactUniqueId());
				assertEquals(datum.document.getCreatedBy(), version.getCreatedBy());
				assertEquals(datum.document.getCreatedOn(), version.getCreatedOn());
				assertEquals(datum.document.getName(), version.getName());
				assertEquals(datum.document.getUpdatedBy(), version.getUpdatedBy());
				assertEquals(datum.document.getUpdatedOn(), version.getUpdatedOn());

				versionContent = datum.documentModel.getVersionContent(datum.document.getId(), version.getVersionId());
				assertNotNull(versionContent);
				assertEquals(datum.document.getId(), versionContent.getDocumentId());
				assertEquals(datum.content, versionContent.getDocumentContent());
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		DocumentContent content;

		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(name, description, testFile);
			content = documentModel.getContent(document.getId());
			data.add(new Fixture(content, document, documentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
