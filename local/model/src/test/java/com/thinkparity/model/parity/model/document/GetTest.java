/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model get api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see GetTest#setUp()
	 * @see GetTest#tearDown()
	 */
	private class Fixture {
		private final Document document;
		private final DocumentModel documentModel;
		private final UUID id;
		private Fixture(final Document document,
				final DocumentModel documentModel, final UUID id) {
			this.document = document;
			this.documentModel = documentModel;
			this.id = id;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetTest.
	 */
	public GetTest() { super("testGet"); }

	/**
	 * Test the document model get api.
	 */
	public void testGet() {
		try {
			Document document;
			for(Fixture datum : data) {
				document = datum.documentModel.get(datum.id);

				assertNotNull(document);
				assertEquals(datum.document, document);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testGet");
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		UUID id;
		
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject, name, description, testFile.getFile());
			id = document.getId();

			data.add(new Fixture(document, documentModel, id));
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
