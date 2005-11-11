/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * Test the document model export api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ExportTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see ExportTest#setUp()
	 * @see ExportTest#tearDown()
	 */
	private class Fixture {
		private final String contentChecksum;
		private final Document document;
		private final DocumentModel documentModel;
		private final File file;
		private Fixture(final String contentChecksum, final Document document,
				final DocumentModel documentModel, final File file) {
			this.contentChecksum = contentChecksum;
			this.document = document;
			this.documentModel = documentModel;
			this.file = file;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ExportTest.
	 */
	public ExportTest() { super("testExport"); }

	/**
	 * Test the document model export api.
	 */
	public void testExport() {
		try {
			byte[] fileContent;
			String fileContentChecksum;
			for(Fixture datum : data) {
				datum.documentModel.export(datum.document, datum.file);
				
				assertTrue(datum.file.exists());

				fileContent = FileUtil.readFile(datum.file);
				fileContentChecksum = MD5Util.md5Hex(fileContent);

				assertEquals(fileContentChecksum, datum.contentChecksum);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testExport");
		final DocumentModel documentModel = getDocumentModel();
		Document document;
		String name, description, contentChecksum;
		byte[] content;
		File exportFile;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			content = FileUtil.readFile(testFile.getFile());
			contentChecksum = MD5Util.md5Hex(content);
			document = documentModel.create(
					testProject, name, description, testFile.getFile());
			exportFile = new File(
					System.getProperty("java.io.tmpdir"),
					testFile.getName());
			data.add(new Fixture(contentChecksum, document, documentModel, exportFile));	
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		// delete the exported files
		for(Fixture datum : data) { FileUtil.delete(datum.file); }
		data.clear();
		data = null;
	}
}
