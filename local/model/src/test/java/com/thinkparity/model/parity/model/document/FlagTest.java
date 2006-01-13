/*
 * Nov 14, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.model.project.Project;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FlagTest extends ModelTestCase {

	private class Fixture {
		private Document document;
		private DocumentModel documentModel;
		private Fixture(final Document document, final DocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a FlagTest.
	 */
	public FlagTest() { super("testFlags"); }

	public void testFlags() {
		try {
			Collection<ParityObjectFlag> flags;
			for(Fixture datum : data) {
				flags = datum.document.getFlags();

				assertNotNull(flags);
				assertTrue(flags.contains(ParityObjectFlag.SEEN));

				datum.document.remove(ParityObjectFlag.SEEN);
				datum.documentModel.update(datum.document);

				flags = datum.document.getFlags();
				assertNotNull(flags);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(1);
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		final JUnitTestFile testFile = getJUnitTestFiles().iterator().next();
		String name, description;
		Document document;

		name = testFile.getName();
		description = name;
		document = documentModel.create(testProject.getId(), name, description, testFile.getFile());
		data.add(new Fixture(document, documentModel));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
