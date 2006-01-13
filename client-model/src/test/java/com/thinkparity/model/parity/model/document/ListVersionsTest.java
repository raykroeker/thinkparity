/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model listVersions api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ListVersionsTest extends ModelTestCase {
	
	/**
	 * Test data fixture.
	 * 
	 * @see ListVersionsTest#setUp()
	 * @see ListVersionsTest#tearDown()
	 */
	private class Fixture {
		private final Document document;
		private final DocumentModel documentModel;
		private Fixture(final Document document,
				final DocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ListVersionsTest.
	 */
	public ListVersionsTest() { super("testListVersions"); }

	/**
	 * Test the document model listVersions api.
	 *
	 */
	public void testListVersions() {
		try {
			Collection<DocumentVersion> documentVersionsList;
			for(Fixture datum : data) {
				documentVersionsList =
					datum.documentModel.listVersions(datum.document.getId());

				assertNotNull(documentVersionsList);
				assertEquals(1, documentVersionsList.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testListVersions");
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = "Document:  " + name;
			document =
				documentModel.create(testProject.getId(), name, description, testFile.getFile());
			data.add(new Fixture(document, documentModel));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
