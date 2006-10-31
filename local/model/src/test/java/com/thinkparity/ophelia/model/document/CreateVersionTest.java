/*
 * Nov 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.MD5Util;

import com.thinkparity.ophelia.OpheliaTestUser;

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
        assertEquals(NAME + " [DOCUMENT BYTES CHECKSUM DOES NOT MATCH EXPECTATION]",
                datum.documentChecksum, version.getChecksum());

        final InputStream stream = datum.documentModel.openVersionStream(version.getArtifactId(), version.getVersionId());
        final File outputFile = new File(getOutputDirectory(), datum.file.getName());
        OutputStream output = null;
        try {
            Assert.assertTrue(outputFile.createNewFile(), "Could not create file {0}", outputFile);
            output = new FileOutputStream(outputFile);
            StreamUtil.copy(stream, output);
            
            final String checksum = MD5Util.md5Hex(FileUtil.readBytes(outputFile));
            assertEquals("File checksums do not match", datum.documentChecksum, checksum);
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        } finally {
            try {
                output.flush();
                output.close();
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            }
        }
    }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFiles()[0];
        final byte[] inputFileBytes = FileUtil.readBytes(inputFile);
		final InternalDocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
        final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
		datum = new Fixture(document, MD5Util.md5Hex(inputFileBytes), documentModel, inputFile);
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
        private final String documentChecksum;
		private final InternalDocumentModel documentModel;
        private final File file;
		private Fixture(final Document document, final String documentChecksum,
                final InternalDocumentModel documentModel, final File file) {
			this.document = document;
            this.documentChecksum = documentChecksum;
			this.documentModel = documentModel;
            this.file = file;
		}
	}
}
