/*
 * Dec 12, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.model.project.Project;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LockTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see LockTest#setUp()
	 * @see LockTest#tearDown()
	 */
	private class Fixture {
		private final Document document;
		private final DocumentModel documentModel;
		private Fixture(final Document document, final DocumentModel documentModel) {
			super();
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;


	/**
	 * Create a LockTest.
	 */
	public LockTest() { super("testLock"); }

	/**
	 * Test the document model lock api.
	 *
	 */
	public void testLock() {
		try {
			Document document;
			for(Fixture datum : data) {
				datum.documentModel.lock(datum.document);

				document = datum.documentModel.get(datum.document.getId());
				LockTest.assertFalse(document.contains(ParityObjectFlag.KEY));
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();

		String description, name;
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			data.add(new Fixture(
					documentModel.create(testProject, name, description, testFile.getFile()),
					documentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
