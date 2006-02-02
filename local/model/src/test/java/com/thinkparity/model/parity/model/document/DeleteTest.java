/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model delete api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class DeleteTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see DeleteTest#setUp()
	 * @see DeleteTest#tearDown()
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
	 * Create a DeleteTest.
	 */
	public DeleteTest() { super("testDelete"); }

	/**
	 * Test the document model delete api.
	 * 
	 */
	public void testDelete() {
		try {
			Document document;
			for(Fixture datum : data) {
				datum.documentModel.delete(datum.document.getId());

				document = datum.documentModel.get(datum.document.getId());
				assertNull(document);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		Document document;
		String name, description;

		data = new Vector<Fixture>(4);
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject.getId(), name, description, testFile);
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
