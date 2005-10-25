/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentXmlIOTest extends ModelTestCase {

	private class BuildXmlFileData {
		private final Document document;
		private final DocumentXmlIO documentXmlIO;
		private BuildXmlFileData(final Document document,
				final DocumentXmlIO documentXmlIO) {
			this.document = document;
			this.documentXmlIO = documentXmlIO;
		}
	}

	private Vector<BuildXmlFileData> buildXmlFileData;

	/**
	 * Create a DocumentXmlIOTest.
	 */
	public DocumentXmlIOTest() { super("Document xml io");}

	public void testBuildXmlFile() {
		try {
			File xmlFile;
			for(BuildXmlFileData data : buildXmlFileData) {
				xmlFile = data.documentXmlIO.getXmlFile(data.document);
				DocumentXmlIOTest.assertNotNull(xmlFile);
				DocumentXmlIOTest.assertTrue(xmlFile.exists());
				DocumentXmlIOTest.assertTrue(xmlFile.canRead());
				DocumentXmlIOTest.assertTrue(xmlFile.canWrite());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpBuildXmlFile();
	}

	protected void setUpBuildXmlFile() throws Exception {
		buildXmlFileData = new Vector<BuildXmlFileData>(8);
		final Project testProject = createTestProject("testBuildXmlFile");
		final DocumentModel documentModel = getDocumentModel();
		final DocumentXmlIO documentXmlIO = new DocumentXmlIO(getWorkspace());
		String name, description;
		Document document;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;

			document = documentModel.create(testProject, name, description, testFile.getFile());
			buildXmlFileData.add(new BuildXmlFileData(document, documentXmlIO));
		}

		name = "Prj.1";
		description = name;
		final Project nestedProject = getProjectModel().create(testProject, name, description);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;

			document = documentModel.create(nestedProject, name, description, testFile.getFile());
			buildXmlFileData.add(new BuildXmlFileData(document, documentXmlIO));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownBuildXmlFile();
	}

	protected void tearDownBuildXmlFile() throws Exception {
		buildXmlFileData.clear();
		buildXmlFileData = null;
	}
}
