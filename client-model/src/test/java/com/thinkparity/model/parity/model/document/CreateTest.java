/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.util.MD5Util;

/**
 * Test the document model create api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class CreateTest extends DocumentTestCase {
	
	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a CreateTest.
	 */
	public CreateTest() { super("testCreate"); }

	/**
	 * Test the document model create api.
	 * 
	 */
	public void testCreate() {
		try {
			Document document;
			DocumentContent content;
			Collection<DocumentVersion> versions;
			for(Fixture datum : data) {
				document = datum.documentModel.create(datum.name,
						datum.description, datum.file);
				assertNotNull(document);

				content = datum.documentModel.getContent(document.getId());
				assertNotNull(content);
				assertEquals(content.getChecksum(), datum.documentContentChecksum);

				versions = datum.documentModel.listVersions(document.getId());
				assertNotNull(versions);
				assertEquals("Number of versions does not match expectation.", datum.expectedVersionsSize, versions.size());
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel documentModel = getDocumentModel();
		data = new Vector<Fixture>(4);
		String name, description;
		String documentContentChecksum;

		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			documentContentChecksum = MD5Util.md5Hex(FileUtil.readBytes(testFile));
			data.add(new Fixture(description, testFile,
					documentContentChecksum, documentModel, 0, name));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see CreateTest#setUp()
	 * @see CreateTest#tearDown()
	 */
	private class Fixture {
		private final String description;
		private final String documentContentChecksum;
		private final DocumentModel documentModel;
		private final int expectedVersionsSize;
		private final File file;
		private final String name;
		private Fixture(final String description, final File file,
				final String documentContentChecksum,
				final DocumentModel documentModel,
				final int expectedVersionsSize, final String name) {
			this.description = description;
			this.file = file;
			this.documentContentChecksum = documentContentChecksum;
			this.documentModel = documentModel;
			this.expectedVersionsSize = expectedVersionsSize;
			this.name = name;
		}
	}
}
