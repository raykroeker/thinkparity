/*
 * Dec 12, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
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
		private final UUID documentId;
		private final DocumentModel documentModel;
		private Fixture(final UUID documentId, final DocumentModel documentModel) {
			super();
			this.documentId = documentId;
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
				datum.documentModel.lock(datum.documentId);

				document = datum.documentModel.get(datum.documentId);
				LockTest.assertFalse(document.contains(ParityObjectFlag.KEY));
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getInputFilesLength());
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();

		String description, name;
		Document document;
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject.getId(), name, description, testFile);
			data.add(new Fixture(document.getId(), documentModel));
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
