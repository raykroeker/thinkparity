/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [TEST CREATE VERSION]";
    
	/** Test data. */
	private Vector<Fixture> data;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionTest() { super(NAME); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
		try {
			DocumentVersion version;
			DocumentVersionContent versionContent;
			for(Fixture datum : data) {
				datum.documentModel.createVersion(datum.document.getId());

                version = datum.documentModel.readLatestVersion(datum.document.getId());
				assertNotNull(version);
				assertEquals(datum.document.getId(), version.getArtifactId());
				assertEquals(datum.document.getType(), version.getArtifactType());
				assertEquals(datum.document.getUniqueId(), version.getArtifactUniqueId());
				assertEquals(datum.document.getCreatedBy(), version.getCreatedBy());
				assertEquals(datum.document.getCreatedOn(), version.getCreatedOn());
				assertEquals(datum.document.getName(), version.getName());
				assertEquals(datum.document.getUpdatedBy(), version.getUpdatedBy());
				assertEquals(datum.document.getUpdatedOn(), version.getUpdatedOn());

                versionContent =
                    datum.documentModel.getVersionContent(
                            datum.document.getId(), version.getVersionId());
                assertNotNull(NAME, versionContent);
                assertEquals(NAME, datum.content, versionContent.getContent());
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final DocumentModel documentModel = getDocumentModel();
		Document document;

		for(File testFile : getInputFiles()) {
			document = create(testFile);
			data.add(new Fixture(FileUtil.readBytes(testFile), document, documentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 */
	private class Fixture {
        private final byte[] content;
		private final Document document;
		private final DocumentModel documentModel;
		private Fixture(final byte[] content, final Document document,
                final DocumentModel documentModel) {
            this.content = content;
			this.document = document;
			this.documentModel = documentModel;
		}
	}
}
