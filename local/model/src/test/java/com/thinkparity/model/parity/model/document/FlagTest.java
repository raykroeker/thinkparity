/*
 * Nov 14, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FlagTest extends DocumentTestCase {

	private class Fixture {
		private Document document;
		private Fixture(final Document document) {
			this.document = document;
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
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
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
		data.add(new Fixture(document));
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}
}
