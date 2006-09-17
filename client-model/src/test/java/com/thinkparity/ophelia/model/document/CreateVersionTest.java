/*
 * Nov 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.util.MD5Util;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[TEST CREATE VERSION]";
    
	/** Test datum. */
	private Fixture datum;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionTest() { super(NAME); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
		final DocumentVersion version = datum.documentModel.createVersion(datum.document.getId());

        assertNotNull(NAME, version);
		assertEquals(datum.document.getId(), version.getArtifactId());
		assertEquals(datum.document.getType(), version.getArtifactType());
		assertEquals(datum.document.getUniqueId(), version.getArtifactUniqueId());
		assertEquals(datum.document.getCreatedBy(), version.getCreatedBy());
		assertEquals(datum.document.getName(), version.getName());
		assertEquals(datum.document.getUpdatedBy(), version.getUpdatedBy());
        
        final DocumentVersionContent versionContent =
            datum.documentModel.getVersionContent(
                    version.getArtifactId(), version.getVersionId());
        assertNotNull(NAME, versionContent);
        assertEquals(NAME, datum.documentBytes, versionContent.getContent());

        assertEquals(NAME + " [DOCUMENT BYTES CHECKSUM DOES NOT MATCH EXPECTATION]",
                datum.documentChecksum, version.getChecksum());
    }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFiles()[0];
        final byte[] inputFileBytes = FileUtil.readBytes(inputFile);
		final DocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
        final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
		datum = new Fixture(document, inputFileBytes, MD5Util
                .md5Hex(inputFileBytes), documentModel);
    }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 */
	private class Fixture extends DocumentTestCase.Fixture {
		private final Document document;
		private final byte[] documentBytes;
        private final String documentChecksum;
		private final DocumentModel documentModel;
		private Fixture(final Document document, final byte[] documentBytes,
                final String documentChecksum, final DocumentModel documentModel) {
			this.document = document;
            this.documentChecksum = documentChecksum;
            this.documentBytes = documentBytes;
			this.documentModel = documentModel;
		}
	}
}
