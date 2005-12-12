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
public class UnlockTest extends ModelTestCase {

		/**
		 * Test data fixture.
		 * 
		 * @see UnlockTest#setUp()
		 * @see UnlockTest#tearDown()
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
		 * Create an UnlockTest.
		 */
		public UnlockTest() { super("testUnlock"); }

		/**
		 * Test the document model lock api.
		 *
		 */
		public void testUnlock() {
			try {
				Document document;
				for(Fixture datum : data) {
					datum.documentModel.unlock(datum.document);

					document = datum.documentModel.get(datum.document.getId());
					LockTest.assertTrue(document.contains(ParityObjectFlag.KEY));
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
			Document document;
			for(ModelTestFile testFile : getJUnitTestFiles()) {
				name = testFile.getName();
				description = name;
				document = documentModel.create(
						testProject, name, description, testFile.getFile());
				// in order to unlock the document it must first be locked
				// since it's initial state is not locked after creation
				documentModel.lock(document);
				data.add(new Fixture(document, documentModel));
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
