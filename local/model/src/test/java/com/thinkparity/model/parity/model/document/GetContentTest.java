/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.util.MD5Util;

/**
 * Test the document model getContent api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class GetContentTest extends DocumentTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see GetContentTest#setUp()
	 * @see GetContentTest#tearDown()
	 */
	private class Fixture {
		final Document document;
		final DocumentModel documentModel;
		final String expectedContentChecksum;
		private Fixture(final Document document,
				final DocumentModel documentModel,
				final String expectedContentChecksum) {
			this.document = document;
			this.documentModel = documentModel;
			this.expectedContentChecksum = expectedContentChecksum;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetContentTest.
	 */
	public GetContentTest() { super("testGetContent"); }

	/**
	 * Test the document model getContent api.
	 */
	public void testGetContent() {
		try {
			DocumentContent content;
			for(Fixture datum : data) {
				content = datum.documentModel.getContent(datum.document.getId());

				assertNotNull(content);
				assertEquals(datum.expectedContentChecksum, content.getChecksum());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final DocumentModel documentModel = getDocumentModel();
		Document document;
		String contentChecksum;
		byte[] content;

		for(File testFile : getInputFiles()) {
			content = FileUtil.readBytes(testFile);
			contentChecksum = MD5Util.md5Hex(content);
			document = create(testFile);

			data.add(new Fixture(document, documentModel, contentChecksum));
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
}
