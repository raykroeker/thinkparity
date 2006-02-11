/*
 * Nov 14, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;

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
			Collection<ArtifactFlag> flags;
			for(Fixture datum : data) {
				flags = datum.document.getFlags();

				assertNotNull(flags);
				assertTrue(flags.contains(ArtifactFlag.SEEN));

				datum.document.remove(ArtifactFlag.SEEN);
				datum.documentModel.update(datum.document);

				flags = datum.document.getFlags();
				assertNotNull(flags);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(1);
		final DocumentModel documentModel = getDocumentModel();
		final File testFile = getInputFile("JUnitTestFramework.txt");
		String name, description;
		Document document;

		name = testFile.getName();
		description = name;
		document = documentModel.create(name, description, testFile);
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
