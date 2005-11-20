/*
 * Nov 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
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
		private final DocumentModel documentModel;
		private final DocumentVersion version;
		private Fixture(final DocumentModel documentModel, final DocumentVersion version) {
			this.documentModel = documentModel;
			this.version = version;
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
				datum.documentModel.openVersion(datum.version);
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
		final ModelTestFile testFile = getJUnitTestFile("JUnit Test Framework.txt");
		final String name = testFile.getName();
		final String description = name;
		final Document document =
			documentModel.create(testProject, name, description, testFile.getFile());
		DocumentVersion version;

		version = documentModel.createVersion(
				document, DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(documentModel, version));

		version = documentModel.createVersion(
				document, DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(documentModel, version));

		version = documentModel.createVersion(
				document, DocumentAction.SEND, new DocumentActionData());
		data.add(new Fixture(documentModel, version));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}
}
