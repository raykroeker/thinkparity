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

import ejp.tracer.TracerAPI;

/**
 * Test the document model create api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateTest extends ModelTestCase {
	
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
		private final File file;
		private final String name;
		private final Project parent;
		private Fixture(final String description,
				final File file, final String documentContentChecksum,
				final DocumentModel documentModel, final String name,
				final Project parent) {
			this.description = description;
			this.file = file;
			this.documentContentChecksum = documentContentChecksum;
			this.documentModel = documentModel;
			this.name = name;
			this.parent = parent;
		}
	}

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
			Document newDocument;
			DocumentContent newDocumentContent;
			for(Fixture datum : data) {
				TracerAPI.enableTracing();
				newDocument = datum.documentModel.create(datum.parent, datum.name,
						datum.description, datum.file);
				TracerAPI.disableTracing();
				TracerAPI.enableTracing();
				newDocumentContent = datum.documentModel.getContent(newDocument);
				TracerAPI.disableTracing();

				assertNotNull(newDocument);
				assertNotNull(newDocumentContent);
				assertEquals(newDocumentContent.getChecksum(), datum.documentContentChecksum);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		data = new Vector<Fixture>(4);
		String name, description;
		File file;
		String documentContentChecksum;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			file = testFile.getFile();
			documentContentChecksum = MD5Util.md5Hex(FileUtil.readFile(file));
			data.add(new Fixture(description, file,
					documentContentChecksum, documentModel, name, testProject));
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
