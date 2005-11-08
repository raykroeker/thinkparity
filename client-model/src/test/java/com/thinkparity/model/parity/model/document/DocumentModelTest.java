 /*
 * Aug 21, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * DocumentModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModelTest extends ModelTestCase {

	private class AddNoteData {
		private final String content;
		private final Document document;
		private final DocumentModel documentModel;
		private final String subject;
		private AddNoteData(final String content, final Document document,
				final DocumentModel documentModel, final String subject) {
			this.content = content;
			this.document = document;
			this.documentModel = documentModel;
			this.subject = subject;
		}
	}
	/**
	 * Create document data structure.
	 */
	private class CreateData {
		private final String description;
		private final String documentContentChecksum;
		private final DocumentModel documentModel;
		private final File file;
		private final String name;
		private final Project parent;
		private CreateData(final String description,
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
		private final String contentChecksum;
		private final Document document;
		private final DocumentModel documentModel;
		private final File file;
		private ExportData(final String contentChecksum, final Document document,
				final DocumentModel documentModel, final File file) {
			this.contentChecksum = contentChecksum;
			this.document = document;
			this.documentModel = documentModel;
			this.file = file;
		}
	}
	private class GetContentData {
		final Document document;
		final DocumentModel documentModel;
		final String expectedContentChecksum;
		private GetContentData(final Document document,
				final DocumentModel documentModel,
				final String expectedContentChecksum) {
			this.document = document;
			this.documentModel = documentModel;
			this.expectedContentChecksum = expectedContentChecksum;
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
	private class MoveData {
		private final Project destination;
		private final Document document;
		private final DocumentModel documentModel;
		private final Project source;
		private MoveData(final Project destination, final Document document, final DocumentModel documentModel, final Project source) {
			this.destination = destination;
			this.document = document;
			this.documentModel = documentModel;
			this.source = source;
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
	private Vector<AddNoteData> addNoteData;
	private Vector<CreateData> createData;

	private Vector<DeleteData> deleteData;

	private Vector<ExportData> exportData;
	private Vector<GetContentData> getContentData;
	private Vector<ListData> listData;
	private Vector<ListVersionsData> listVersionsData;
	private Vector<MoveData> moveData;
	private Vector<OpenData> openData;
	/**
	 * Create a DocumentModelTest.
	 */
	public DocumentModelTest() { super("Test:  Document model"); }
	public void testAddNote() {
		try {
			Note note;
			for(AddNoteData data : addNoteData) {
				note = data.documentModel.addNote(data.document, data.subject, data.content);
				DocumentModelTest.assertNotNull(note);
				DocumentModelTest.assertEquals(note.getSubject(), data.subject);
				DocumentModelTest.assertEquals(note.getContent(), data.content);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * Test the create document method.  The content of the document and the
	 * original file will be compared.
	 *
	 */
	public void testCreate() {
		try {
			Document newDocument;
			DocumentContent newDocumentContent;
			for(CreateData data : createData) {
				newDocument = data.documentModel.create(data.parent, data.name,
						data.description, data.file);
				newDocumentContent = data.documentModel.getContent(newDocument);
				DocumentModelTest.assertNotNull(newDocument);
				DocumentModelTest.assertNotNull(newDocumentContent);
				DocumentModelTest.assertEquals(newDocumentContent.getChecksum(), data.documentContentChecksum);
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
			String fileContentChecksum;
			for(ExportData data : exportData) {
				data.documentModel.export(data.document, data.file);
				DocumentModelTest.assertTrue(data.file.exists());
				fileContent = FileUtil.readFile(data.file);
				fileContentChecksum = MD5Util.md5Hex(fileContent);
				DocumentModelTest.assertEquals(fileContentChecksum, data.contentChecksum);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testGetContent() {
		try {
			DocumentContent content;
			for(GetContentData data : getContentData) {
				content = data.documentModel.getContent(data.document);
				DocumentModelTest.assertNotNull(content);
				DocumentModelTest.assertEquals(
						data.expectedContentChecksum,
						content.getChecksum());
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

	public void testMove() {
		try {
			Collection<Document> sourceDocuments;
			Collection<Document> destinationDocuments;
			for(MoveData data : moveData) {
				data.documentModel.move(data.document, data.destination);

				sourceDocuments = data.documentModel.list(data.source);
				destinationDocuments = data.documentModel.list(data.destination);
				DocumentModelTest.assertNotNull(sourceDocuments);
				DocumentModelTest.assertNotNull(destinationDocuments);
				assertNotContains(sourceDocuments, data.document);
				assertContains(destinationDocuments, data.document);
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
		setUpAddNote();
		setUpCreate();
		setUpDelete();
		setUpExport();
		setUpGetContent();
		setUpList();
		setUpListVersions();
		setUpMove();
		setUpOpen();
	}

	protected void setUpAddNote() throws Exception {
		addNoteData = new Vector<AddNoteData>(5);
		final Project testProject = createTestProject("testAddNote");
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		String subject, content;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject, name,
					description, testFile.getFile());
			subject = "New note.";
			content = subject;

			addNoteData.add(new AddNoteData(content, document, documentModel, subject));
		}
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
		String documentContentChecksum;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			file = testFile.getFile();
			documentContentChecksum = MD5Util.md5Hex(FileUtil.readFile(file));
			createData.add(new CreateData(description, file,
					documentContentChecksum, documentModel, name, testProject));
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
		exportData = new Vector<ExportData>(getJUnitTestFilesSize());
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
			exportData.add(new ExportData(contentChecksum, document, documentModel, exportFile));	
		}
	}

	protected void setUpGetContent() throws Exception {
		getContentData = new Vector<GetContentData>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testGetContent");
		final DocumentModel documentModel = getDocumentModel();
		Document document;
		String name, description, contentChecksum;
		byte[] content;
		

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			content = FileUtil.readFile(testFile.getFile());
			contentChecksum = MD5Util.md5Hex(content);
			document = documentModel.create(testProject, name, description, testFile.getFile());

			getContentData.add(new GetContentData(document, documentModel, contentChecksum));
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

	protected void setUpMove() throws Exception {
		moveData = new Vector<MoveData>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testMove");
		final ProjectModel projectModel = getProjectModel();
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Project destination, source;
		Document document;

		name = "S";
		description = name;
		source = projectModel.create(testProject, name, description);

		name = "D";
		description = name;
		destination = projectModel.create(testProject, name, description);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(source, name, description, testFile.getFile());

			moveData.add(new MoveData(destination, document, documentModel, source));
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
		tearDownAddNote();
		tearDownCreate();
		tearDownDelete();
		tearDownExport();
		tearDownGetContent();
		tearDownList();
		tearDownListVersions();
		tearDownMove();
		tearDownOpen();
	}

	protected void tearDownAddNote() throws Exception {
		addNoteData.clear();
		addNoteData = null;
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

	protected void tearDownGetContent() throws Exception {
		getContentData.clear();
		getContentData = null;
	}

	protected void tearDownList() throws Exception {
		listData.clear();
		listData = null;
	}

	protected void tearDownListVersions() throws Exception {
		listVersionsData.clear();
		listVersionsData = null;
	}

	protected void tearDownMove() throws Exception {
		moveData.clear();
		moveData = null;
	}

	/**
	 * Clean up the open data structure.
	 *
	 */
	protected void tearDownOpen() throws Exception {
		openData.clear();
		openData = null;
	}
}
