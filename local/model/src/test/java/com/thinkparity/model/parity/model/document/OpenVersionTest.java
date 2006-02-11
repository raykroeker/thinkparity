/*
 * Nov 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model open version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class OpenVersionTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class Fixture {
		private final Long documentId;
		private final DocumentModel documentModel;
		private final Long versionId;
		private Fixture(final Long documentId,
				final DocumentModel documentModel, final Long versionId) {
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.versionId = versionId;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a OpenVersionTest.
	 */
	public OpenVersionTest() { super("testOpenVersion"); }

	/**
	 * Test the document model open version api.
	 *
	 */
	public void testOpenVersion() {
		try {
			for(Fixture datum : data) {
				try {
					datum.documentModel.openVersion(datum.documentId, datum.versionId);
				}
				catch(ParityException px) { if(!isNYIAOnLinux(px)) { throw px; } }
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(3);
		final DocumentModel documentModel = getDocumentModel();
		final File testFile = getInputFile("JUnitTestFramework.txt");
		final String name = testFile.getName();
		final String description = name;
		final Document document =
			documentModel.create(name, description, testFile);
		DocumentVersion version;

		version = documentModel.createVersion(document.getId());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}
}
