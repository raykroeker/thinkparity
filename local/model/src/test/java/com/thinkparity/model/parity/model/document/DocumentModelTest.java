 /*
 * Aug 21, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Stack;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * DocumentModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModelTest extends ModelTestCase {

	/**
	 * Create document data definition.
	 */
	private class CreateDocumentData {
		private final String description;
		private final File documentFile;
		private final byte[] documentFileContent;
		private final DocumentModel documentModel;
		private final String name;
		private final Project parent;
		private CreateDocumentData(final String description,
				final File documentFile, final byte[] documentFileContent,
				final DocumentModel documentModel, final String name,
				final Project parent) {
			this.description = description;
			this.documentFile = documentFile;
			this.documentFileContent = new byte[documentFileContent.length];
			System.arraycopy(documentFileContent, 0,
					this.documentFileContent, 0, documentFileContent.length);
			this.documentModel = documentModel;
			this.name = name;
			this.parent = parent;
		}
	}

	/**
	 * Delete document data definition.
	 */
	private class DeleteDocumentData {
		private final Document document;
		private final DocumentModel documentModel;
		private final Project documentProject;
		private final ProjectModel projectModel;
		private DeleteDocumentData(final Document document,
				final DocumentModel documentModel,
				final Project documentProject, final ProjectModel projectModel) {
			this.document = document;
			this.documentModel = documentModel;
			this.documentProject = documentProject;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Export document data definition for the export document test.
	 * @see DocumentModelTest#testExportDocument()
	 * @see DocumentModelTest#setUpExportDocument()
	 */
	private class ExportDocumentData {
		private final Document document;
		private final DocumentModel documentModel;
		private final File exportFile;
		private ExportDocumentData(final Document document,
				final DocumentModel documentModel, final File exportFile) {
			this.document = document;
			this.documentModel = documentModel;
			this.exportFile = exportFile;
		}
	}

	/**
	 * Test data definition for the get path test.
	 * @see DocumentModelTest#setUpGetPath()
	 * @see DocumentModelTest#testGetPath()
	 */
	private class GetPathData {
		private final StringBuffer expectedPath;
		private final StringBuffer path;
		private GetPathData(final StringBuffer expectedPath,
				final StringBuffer path) {
			this.expectedPath = expectedPath;
			this.path = path;
		}
	}

	private Vector<CreateDocumentData> createDocumentData;
	private Vector<DeleteDocumentData> deleteDocumentData;
	private Vector<ExportDocumentData> exportDocumentData;
	private Vector<GetPathData> getPathData;

	/**
	 * Create a DocumentModelTest.
	 */
	public DocumentModelTest() { super("Test:  Document model"); }

	/**
	 * Test the create document method.  The content of the document and the
	 * original file will be compared.
	 *
	 */
	public void testCreateDocument() {
		try {
			Document newDocument;
			byte[] newDocumentContent;
			for(CreateDocumentData data : createDocumentData) {
				newDocument = data.documentModel.createDocument(
						data.parent, data.name, data.description, data.documentFile);
				newDocumentContent = newDocument.getContent();
				DocumentModelTest.assertNotNull(newDocument);
				DocumentModelTest.assertEquals(
						newDocumentContent.length, data.documentFileContent.length);
				for(int i = 0; i < newDocumentContent.length; i++) {
					DocumentModelTest.assertEquals(
							newDocumentContent[i], data.documentFileContent[i]);
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * Test the deletion of documents. This will check the project to ensure
	 * that no documents remain post deletion.
	 * 
	 */
	public void testDeleteDocument() {
		for(DeleteDocumentData data : deleteDocumentData) {
			try { data.documentModel.deleteDocument(data.document); }
			catch(Throwable t) { fail(getFailMessage(t)); }
		}
	}

	/**
	 * Test the export of a document. The content of the document and the
	 * exported file will be compared.
	 * 
	 */
	public void testExportDocument() {
		try {
			byte[] fileContent;
			byte[] documentContent;
			for(ExportDocumentData data : exportDocumentData) {
				data.documentModel.exportDocument(data.exportFile, data.document);
				DocumentModelTest.assertTrue(data.exportFile.exists());
				fileContent = FileUtil.readFile(data.exportFile);
				documentContent = data.document.getContent();
				DocumentModelTest.assertEquals(fileContent.length, documentContent.length);
				for(int i = 0; i < fileContent.length; i++) {
					DocumentModelTest.assertEquals(fileContent[i], documentContent[i]);
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * Test the paths generated for all of the documents attached to the
	 * root project.
	 *
	 */
	public void testGetPath() {
		try {
			for(GetPathData data : getPathData) {
				DocumentModelTest.assertNotNull(data.path);
				DocumentModelTest.assertEquals(data.path.toString(), data.expectedPath.toString());
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
		setUpCreateDocument();
		setUpDeleteDocument();
		setUpExportDocument();
		setUpGetPath();
	}

	protected void setUpCreateDocument() throws Exception {
		final Project testProject = createTestProject("testCreateDocument");
		final DocumentModel documentModel = getDocumentModel();
		createDocumentData = new Vector<CreateDocumentData>(4);
		String name, description;
		File documentFile;
		byte[] documentFileContent;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentFile = testFile.getFile();
			documentFileContent = FileUtil.readFile(documentFile);
			createDocumentData.add(new CreateDocumentData(description,
					documentFile, documentFileContent, documentModel, name,
					testProject));
		}
	}

	protected void setUpDeleteDocument() throws Exception {
		final Project testProject = createTestProject("testDeleteDocument");
		final DocumentModel documentModel = getDocumentModel();
		final ProjectModel projectModel = getProjectModel();
		Document document;
		Project documentProject;
		String name, description;
		File documentFile;

		deleteDocumentData = new Vector<DeleteDocumentData>(4);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentFile = testFile.getFile();
			document = documentModel.createDocument(
					testProject, name, description, documentFile);
			documentProject = document.getParent();

			deleteDocumentData.add(
					new DeleteDocumentData(document, documentModel, documentProject, projectModel));
		}
	}

	/**
	 * Set up the testExportDocument data.
	 * 
	 * @throws Exception
	 */
	protected void setUpExportDocument() throws Exception {
		final Project testProject = createTestProject("testExportDocument");
		final DocumentModel documentModel = getDocumentModel();
		exportDocumentData = new Vector<ExportDocumentData>(4);
		String name, description;
		Document document;
		File exportFile;
		
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;

			exportFile = new File(
					System.getProperty("java.io.tmpdir"), testFile.getName());
			document = documentModel.createDocument(
					testProject, name, description, testFile.getFile());
			exportDocumentData.add(
					new ExportDocumentData(document, documentModel, exportFile));	
		}
	}

	/**
	 * Set up the testGetPath data.
	 * 
	 * @throws Exception
	 */
	protected void setUpGetPath() throws Exception {
		final Project testProject = createTestProject("testGetPath");
		String name, description;
		Document document;

		getPathData = new Vector<GetPathData>(10);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = getDocumentModel().createDocument(testProject, name, description, testFile.getFile());
			getPathData.add(
					new GetPathData(getExpectedPath(document), document.getPath()));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCreateDocument();
		tearDownDeleteDocument();
		tearDownExportDocument();
		tearDownGetPath();
	}

	/**
	 * Clean up the create document data.
	 * 
	 * @throws Exception
	 */
	protected void tearDownCreateDocument() throws Exception {
		createDocumentData.clear();
		createDocumentData = null;
	}

	protected void tearDownDeleteDocument() {
		deleteDocumentData.clear();
		deleteDocumentData = null;
	}

	/**
	 * Clean up the export document data.
	 * 
	 * @throws Exception
	 */
	protected void tearDownExportDocument() throws Exception {
		// delete the exported files
		for(ExportDocumentData data : exportDocumentData) {
			FileUtil.delete(data.exportFile);
		}
		exportDocumentData.clear();
		exportDocumentData = null;
	}

	/**
	 * Clean up the get path data.
	 * 
	 * @throws Exception
	 */
	protected void tearDownGetPath() throws Exception {
		getPathData.clear();
		getPathData = null;
	}

	/**
	 * Build an expected path for a given document.  This uses the document to
	 * obtain the project to obtain the project, etc.
	 * 
	 * @param document
	 *            The document to build the expected path for.
	 * @return The expected path.
	 */
	private StringBuffer getExpectedPath(final Document document) {
		final Stack<Project> projectStack = new Stack<Project>();
		Project parentProject = document.getParent();
		while(parentProject.isSetParent()) {
			projectStack.push(parentProject);
			parentProject = parentProject.getParent();
		}
		projectStack.add(parentProject);

		final StringBuffer expectedPath =
			new StringBuffer(projectStack.pop().getCustomName());
		while(!projectStack.isEmpty())
			expectedPath.append("/")
				.append(projectStack.pop().getCustomName());
		expectedPath.append("/")
			.append(document.getCustomName());
		return expectedPath;
	}
}
