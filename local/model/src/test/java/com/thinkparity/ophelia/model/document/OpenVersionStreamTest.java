/*
 * Nov 18, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.MD5Util;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model open version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class OpenVersionStreamTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "Open Version Stream Test";

	/** Test datum. */
	private Fixture datum;

	/** Create OpenVersionTest. */
	public OpenVersionStreamTest() { super(NAME); }

	/**
	 * Test the document model open version api.
	 *
	 */
	public void testOpenVersion() {
	    final InputStream inputStream =
            datum.documentModel.openVersion(
                    datum.version.getArtifactId(), datum.version.getVersionId());
        final File outputFile = new File(getTestSession().getOutputDirectory(), datum.version.getName());
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
        try {
            StreamUtil.copy(inputStream, outputStream);
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        } finally {
            try {
                inputStream.close();
            } catch (final IOException iox) {
                throw new RuntimeException(iox);
            } finally {
                try {
                    outputStream.close();
                } catch (final IOException iox) {
                    throw new RuntimeException(iox);
                }
            }
        }

        try {
            final InputStream inputFileStream = new FileInputStream(datum.inputFile);
            String inputChecksum;
            try {
                 inputChecksum = MD5Util.md5Hex(inputFileStream);
            } catch (final Throwable t) {
                inputChecksum = null;
            } finally {
                inputFileStream.close();
            }

            final InputStream outputFileStream = new FileInputStream(outputFile);
            String outputChecksum;
            try {
                outputChecksum = MD5Util.md5Hex(outputFileStream);
            } catch (final Throwable t) {
                outputChecksum = null;
            } finally {
                outputFileStream.close();
            }

            assertEquals("Input and output files do not match.", inputChecksum,
                    outputChecksum);
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }

	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFile("JUnitTestFramework.txt");
		final InternalDocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
		final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
        final File modFile = modifyDocument(OpheliaTestUser.JUNIT, document.getId());
        final DocumentVersion version = createDocumentVersion(OpheliaTestUser.JUNIT, document, currentDateTime());

		datum = new Fixture(documentModel, modFile, version);
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	/** Test datum definition. */
	private class Fixture {
		private final InternalDocumentModel documentModel;
		private final File inputFile;
        private final DocumentVersion version;
		private Fixture(final InternalDocumentModel documentModel,
                final File inputFile, final DocumentVersion version) {
			this.documentModel = documentModel;
            this.inputFile = inputFile;
			this.version = version;
		}
	}
}
