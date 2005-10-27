 /*
 * Aug 21, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
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
	 * Create document data structure.
	 */
	private class CreateData {
		private final String description;
		private final byte[] documentFileContent;
		private final DocumentModel documentModel;
		private final File file;
		private final String name;
		private final Project parent;
		private CreateData(final String description,
				final File file, final byte[] documentFileContent,
				final DocumentModel documentModel, final String name,
				final Project parent) {
			this.description = description;
			this.file = file;
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
	private class DeleteData {
		private final Document document;
		private final DocumentModel documentModel;
		private DeleteData(final Document document,
				final DocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Export document data definition for the export document test.
	 * @see DocumentModelTest#testExport()
	 * @see DocumentModelTest#setUpExport()
	 */
	private class ExportData {
		private final Document document;
		private final DocumentModel documentModel;
		private final File file;
		private ExportData(final Document document,
				final DocumentModel documentModel, final File file) {
			this.document = document;
			this.documentModel = documentModel;
			this.file = file;
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

	/**
	 * Test data definition for list.
	 */
	private class ListData {
		private final DocumentModel documentModel;
		private final Collection<Document> expectedDocumentList;
		private final Project project;
		private ListData(final DocumentModel documentModel, final Collection<Document> expectedDocumentList, final Project project) {
			this.documentModel = documentModel;
			this.expectedDocumentList = expectedDocumentList;
			this.project = project;
		}
	}

	/**
	 * Test data definition for listVersions.
	 */
	private class ListVersionsData {
		private final Document document;
		private final DocumentModel documentModel;
		private ListVersionsData(final Document document,
				final DocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Open document data structure.
	 */
	private class OpenData {
		private final Document document;
		private final DocumentModel documentModel;
		private OpenData(final Document document,
				final DocumentModel documentModel) {
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	private Vector<CreateData> createData;

	private Vector<DeleteData> deleteData;

	private Vector<ExportData> exportData;

	private Vector<GetPathData> getPathData;
	private Collection<ListData> listData;

	private Collection<ListVersionsData> listVersionsData;

	private Vector<OpenData> openData;

	/**
	 * Create a DocumentModelTest.
	 */
	public DocumentModelTest() { super("Test:  Document model"); }

	/**
	 * Test the create document method.  The content of the document and the
	 * original file will be compared.
	 *
	 */
	public void testCreate() {
		try {
			Document newDocument;
			byte[] newDocumentContent;
			for(CreateData data : createData) {
				newDocument = data.documentModel.create(data.parent, data.name,
						data.description, data.file);
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
	public void testDelete() {
		for(DeleteData data : deleteData) {
			try { data.documentModel.delete(data.document); }
			catch(Throwable t) { fail(getFailMessage(t)); }
		}
	}

	/**
	 * Test the export of a document. The content of the document and the
	 * exported file will be compared.
	 * 
	 */
	public void testExport() {
		try {
			byte[] fileContent;
			byte[] documentContent;
			for(ExportData data : exportData) {
				data.documentModel.export(data.document, data.file);
				DocumentModelTest.assertTrue(data.file.exists());
				fileContent = FileUtil.readFile(data.file);
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
	 * Test the listing of documents from the model api.
	 *
	 */
	public void testList() {
		try {
			Collection<Document> documentList;
			for(ListData data : listData) {
				documentList = data.documentModel.list(data.project);
				DocumentModelTest.assertNotNull(documentList);
				DocumentModelTest.assertEquals(
						documentList.size(), data.expectedDocumentList.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * Test the document model listVersions api.
	 *
	 */
	public void testListVersions() {
		try {
			Collection<DocumentVersion> documentVersionsList;
			for(ListVersionsData data : listVersionsData) {
				documentVersionsList =
					data.documentModel.listVersions(data.document);
				DocumentModelTest.assertNotNull(documentVersionsList);
				DocumentModelTest.assertEquals(1, documentVersionsList.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * Test the document model's open api.
	 *
	 */
	public void testOpen() {
		try {
			for(OpenData data : openData) {
				data.documentModel.open(data.document);
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
		setUpCreate();
		setUpDelete();
		setUpExport();
		setUpGetPath();
		setUpList();
		setUpListVersions();
		setUpOpen();
	}

	/**
	 * Set up the create data structure.
	 * 
	 * @throws Exception
	 */
	protected void setUpCreate() throws Exception {
		final Project testProject = createTestProject("testCreate");
		final DocumentModel documentModel = getDocumentModel();
		createData = new Vector<CreateData>(4);
		String name, description;
		File file;
		byte[] documentFileContent;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			file = testFile.getFile();
			documentFileContent = FileUtil.readFile(file);
			createData.add(new CreateData(description,
					file, documentFileContent, documentModel, name,
					testProject));
		}
	}

	/**
	 * Set up the delete data structure.
	 * 
	 * @throws Exception
	 */
	protected void setUpDelete() throws Exception {
		final Project testProject = createTestProject("testDelete");
		final DocumentModel documentModel = getDocumentModel();
		Document document;
		String name, description;
		File file;

		deleteData = new Vector<DeleteData>(4);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			file = testFile.getFile();
			document = documentModel.create(testProject, name, description, file);

			deleteData.add(new DeleteData(document, documentModel));
		}
	}

	/**
	 * Set up the export data structure.
	 * 
	 * @throws Exception
	 */
	protected void setUpExport() throws Exception {
		final Project testProject = createTestProject("testExport");
		final DocumentModel documentModel = getDocumentModel();
		exportData = new Vector<ExportData>(4);
		String name, description;
		Document document;
		File exportFile;
		
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;

			exportFile = new File(
					System.getProperty("java.io.tmpdir"), testFile.getName());
			document = documentModel.create(
					testProject, name, description, testFile.getFile());
			exportData.add(
					new ExportData(document, documentModel, exportFile));	
		}
	}

	/**
	 * Set up the get path data structure.
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
			document = getDocumentModel().create(testProject, name, description, testFile.getFile());
			getPathData.add(
					new GetPathData(getExpectedPath(document), document.getPath()));
		}
	}

	protected void setUpList() throws Exception {
		final Integer scenarioCount = 4;
		listData = new Vector<ListData>(scenarioCount);
		final Project testProject = createTestProject("testList");
		final DocumentModel documentModel = getDocumentModel();
		final ProjectModel projectModel = getProjectModel();
		Collection<Document> documentList;
		Project project;
		String name, description;
		Document document;

		for(int i = 0; i < scenarioCount; i++) {
			name = "p." + i;
			description = "Project:  " + name;
			project = projectModel.create(testProject, name, description);
			documentList = new Vector<Document>(getJUnitTestFilesSize());
			for(ModelTestFile testFile : getJUnitTestFiles()) {
				name = testFile.getName();
				description = "Document:  " + name;
				document =
					documentModel.create(project, name, description, testFile.getFile());
				documentList.add(document);
			}
			listData.add(new ListData(documentModel, documentList, project));
		}
	}

	protected void setUpListVersions() throws Exception {
		listVersionsData = new Vector<ListVersionsData>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testListVersions");
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = "Document:  " + name;
			document =
				documentModel.create(testProject, name, description, testFile.getFile());
			listVersionsData.add(new ListVersionsData(document, documentModel));
		}
	}

	/**
	 * Set up the open data structure.
	 *
	 */
	protected void setUpOpen() throws Exception {
		final Project testOpenProject = super.createTestProject("testOpen");
		final DocumentModel documentModel = getDocumentModel();
		openData = new Vector<OpenData>(4);
		Document document;
		String name, description;
		File documentFile;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentFile = testFile.getFile();

			document = documentModel.create(testOpenProject, name,
					description, documentFile);
			openData.add(new OpenData(document, documentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCreate();
		tearDownDelete();
		tearDownExport();
		tearDownGetPath();
		tearDownList();
		tearDownListVersions();
		tearDownOpen();
	}

	/**
	 * Clean up the create data structure.
	 * 
	 * @throws Exception
	 */
	protected void tearDownCreate() throws Exception {
		createData.clear();
		createData = null;
	}

	/**
	 * Clean up the delete data structure.
	 * 
	 * @throws Exception
	 */
	protected void tearDownDelete() throws Exception {
		deleteData.clear();
		deleteData = null;
	}

	/**
	 * Clean up the export data structure.
	 * 
	 * @throws Exception
	 */
	protected void tearDownExport() throws Exception {
		// delete the exported files
		for(ExportData data : exportData) {
			FileUtil.delete(data.file);
		}
		exportData.clear();
		exportData = null;
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

	protected void tearDownList() throws Exception {
		listData.clear();
		listData = null;
	}

	protected void tearDownListVersions() throws Exception {
		listVersionsData.clear();
		listVersionsData = null;
	}

	/**
	 * Clean up the open data structure.
	 *
	 */
	protected void tearDownOpen() throws Exception {
		openData.clear();
		openData = null;
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
