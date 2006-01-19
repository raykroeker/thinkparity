/*
 * Nov 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.NotYetImplementedAssertion;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.project.Project;

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
		private final UUID documentId;
		private final DocumentModel documentModel;
		private final String versionId;
		private Fixture(final UUID documentId,
				final DocumentModel documentModel, final String versionId) {
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
				catch(NotYetImplementedAssertion nyia) {
					switch(OSUtil.getOS()) {
					case LINUX:
						logger.warn("[PARITY] Running test on un-supported platform.");
						break;
					default: throw nyia;
					}
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(3);
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		final JUnitTestFile testFile = getJUnitTestFile("JUnitTestFramework.txt");
		final String name = testFile.getName();
		final String description = name;
		final Document document =
			documentModel.create(testProject.getId(), name, description, testFile.getFile());
		DocumentVersion version;

		version = documentModel.createVersion(
				document.getId(), DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));

		version = documentModel.createVersion(
				document.getId(), DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));

		version = documentModel.createVersion(
				document.getId(), DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(document.getId(), documentModel, version.getVersionId()));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}
}
