/*
 * Jul 1, 2005
 */
package com.thinkparity.model;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * Provides singleton functionality for the model test suite.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.17
 */
public class ModelTestCaseHelper {

	/**
	 * Project unique to the junit session. It is created using a lazy
	 * initialization pattern.
	 * 
	 * @see ModelTestCaseHelper#getJUnitProject()
	 */
	private static Project jUnitProject;

	/**
	 * Test randomizer.
	 */
	private static final Random jUnitRandom = new Random();

	/**
	 * Unique session id for each junit session.
	 */
	private static final String jUnitSessionId;

	/**
	 * Start time of the junit session.
	 */
	private static final Long jUnitSessionStart;

	/**
	 * Files to use when testing.
	 */
	private static final Vector<ModelTestFile> jUnitTestFiles;

	/**
	 * Seed data for generating random test text.
	 */
	private static final char[] jUnitTestTextSeed =
		{ 'A','B','C','D','E','F','G','H','I','J',
		  'K','L','M','N','O','P','Q','R','S','T',
		  'U','V','W','X','Y','Z',
		  '0','1','2','3','4','5','6','7','8','9',
		  'a','b','c','d','e','f','g','h','i','j',
		  'k','l','m','n','o','p','q','r','s','t',
		  'u','v','w','x','y','z' };

	/**
	 * UserRenderer to use when testing.
	 */
	private static final ModelTestUser jUnitTestUser;

	static {
		// record the session start time.
		jUnitSessionStart = System.currentTimeMillis();
		// record the session id.
		jUnitSessionId = "jUnit." + jUnitSessionStart;
		// set the resources directory
		final File resourcesDirectory = new File(new StringBuffer(System.getProperty("user.dir"))
				.append(File.separatorChar).append("target")
				.append(File.separatorChar).append("test-classes")
				.append(File.separatorChar).append("com")
				.append(File.separatorChar).append("thinkparity")
				.append(File.separatorChar).append("model")
				.toString());
		// set the workspace directory, then delete its contents
		final File jUnitWorkspace = new File(
				"C:\\Documents and Settings\\raymond\\My Documents\\thinkparity.com",
				"jUnit");
		if(jUnitWorkspace.exists())
			FileUtil.deleteTree(jUnitWorkspace);
		// set the test files
		final File jUnitResourcesFiles =
			new File(resourcesDirectory, "junit.files");
		jUnitTestFiles = new Vector<ModelTestFile>(7);
		jUnitTestFiles.add(
				new ModelTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.doc")));
		jUnitTestFiles.add(
				new ModelTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.odt")));
		jUnitTestFiles.add(
				new ModelTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.png")));
		jUnitTestFiles.add(
				new ModelTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework.txt")));
		jUnitTestFiles.add(
				new ModelTestFile(
						new File(jUnitResourcesFiles, "JUnit Test Framework 1MB.txt")));
		// set the test user
		jUnitTestUser = ModelTestUser.getJUnit();
		// set non ssl mode
		System.setProperty("parity.insecure", "true");
		System.setProperty(
				"parity.workspace",
				jUnitWorkspace.getAbsolutePath());
		// set the output directory
		final File outputDirectory = new File(new StringBuffer(System.getProperty("user.dir"))
			.append(File.separatorChar).append("target")
			.append(File.separatorChar).append("test-reports")
			.toString());
		if(!outputDirectory.exists())
			Assert.assertTrue("jUnit<init>", outputDirectory.mkdir());
		// initialize the logger
		ModelTestLoggerConfigurator.configure(jUnitSessionId, outputDirectory);
	}

	/**
	 * Handle back to the test case.
	 */
	private final ModelTestCase modelTestCase;

	/**
	 * Create a ModelTestCaseHelper
	 */
	ModelTestCaseHelper(final ModelTestCase modelTestCase) {
		super();
		this.modelTestCase = modelTestCase;
	}

	/**
	 * Assert that the document list provided contains the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	void assertContains(final Collection<Document> documentList,
			Document document) {
		final StringBuffer actualIds = new StringBuffer("");
		Boolean didContain = Boolean.FALSE;
		int actualCounter = 0;
		for(Document actual : documentList) {
			if(actual.getId().equals(document.getId())) {
				didContain = Boolean.TRUE;
			}
			actualIds.append(actualCounter == 0 ? "" : ",");
			actualIds.append(actual.getId().toString());
		}
		ModelTestCase.assertTrue("expected:<" + document.getId() + " but was:<" + actualIds.toString(), didContain);
	}

	/**
	 * Assert that the document list provided doesn't contain the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	void assertNotContains(final Collection<Document> documentList,
			Document document) {
		final StringBuffer actualIds = new StringBuffer("");
		Boolean didContain = Boolean.FALSE;
		int actualCounter = 0;
		for(Document actual : documentList) {
			if(actual.getId().equals(document.getId())) {
				didContain = Boolean.TRUE;
			}
			actualIds.append(actualCounter++ == 0 ? "" : ",");
			actualIds.append(actual.getId().toString());
		}
		ModelTestCase.assertFalse("expected:<" + document.getId() + "> but was:<" + actualIds.toString() + ">", didContain);
	}

	void deleteWorkspace(final ModelTestUser modelTestUser) {
		final File workspaceDirectory = getWorkspaceDirectory(modelTestUser);
		deleteTree(workspaceDirectory);
		ModelTestCase.assertFalse(
				"Could not delete workspace directory.",
				workspaceDirectory.exists());
		workspaceDirectory.mkdir();
	}

	/**
	 * Obtain a handle to the document model.
	 * 
	 * @return A handle to the document model.
	 */
	DocumentModel getDocumentModel() {
		return DocumentModel.getModel();
	}

	/**
	 * Get the junit session project. If the project does not exist it will be
	 * created.
	 * 
	 * @return The junit session project.
	 * @throws ParityException
	 */
	Project getJUnitProject() throws ParityException {
		if(null == ModelTestCaseHelper.jUnitProject) {
			final Project myProjects = getMyProjects();
			final String name = jUnitSessionId;
			final String description = name;
			ModelTestCaseHelper.jUnitProject =
				getProjectModel().create(myProjects, name, description);
		}
		return ModelTestCaseHelper.jUnitProject;
	}

	/**
	 * Obtain a single jUnit test file located a the specified index.
	 * 
	 * @param index
	 *            The jUnit test file index.
	 * @return The jUnit test file.
	 */
	ModelTestFile getJUnitTestFile(final Integer index) {
		return jUnitTestFiles.elementAt(index);
	}

	/**
	 * Obtain a single jUnit test file with the given name.
	 * 
	 * @param name
	 *            The jUnit test file name.
	 * @return The jUnit test file.
	 */
	ModelTestFile getJUnitTestFile(final String name) {
		for(ModelTestFile testFile : jUnitTestFiles) {
			if(testFile.getName().equals(name)) { return testFile; }
		}
		throw Assert.createUnreachable("getJUnitTestFile(String)");
	}
	
	/**
	 * Obtain a list of the test files available to the jUnit test framework.
	 * 
	 * @return A list of the test files available to the jUnit test framework.
	 */
	Collection<ModelTestFile> getJUnitTestFiles() { 
		return Collections.unmodifiableCollection(
				ModelTestCaseHelper.jUnitTestFiles);
	}
	
	/**
	 * Obtain the number of jUnit test files.
	 * 
	 * @return The number of jUnit test files.
	 */
	Integer getJUnitTestFilesSize(){
		return ModelTestCaseHelper.jUnitTestFiles.size();
	}

	/**
	 * Obtain random test text to use with junit.
	 * 
	 * @param size
	 *            The size of the text in characters.
	 * @return The text.
	 */
	String getJUnitTestText(final Integer size) {
		Assert.assertNotNull("getJUnitTestText(Integer)", size);
		Assert.assertTrue("getJUnitTestText(Integer)", size > 0);
		final StringBuffer textBuffer = new StringBuffer(size);
		for(int i = 0; i < size; i++) {
			textBuffer.append(jUnitTestTextSeed[jUnitRandom.nextInt(jUnitTestTextSeed.length)]);
		}
		return textBuffer.toString();
	}

	/**
	 * Get the test user.
	 * 
	 * @return The model test user.
	 */
	ModelTestUser getJUnitTestUser() {
		return ModelTestCaseHelper.jUnitTestUser;
	}

	/**
	 * Obtain a handle to the parity preferences.
	 * 
	 * @return A handle to the parity preferences.
	 */
	Preferences getPreferences() { return getWorkspace().getPreferences(); }

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	ProjectModel getProjectModel() { return ProjectModel.getModel(); }

	/**
	 * Obtain a handle to the session model.
	 * 
	 * @return A handle to the session model.
	 */
	SessionModel getSessionModel() { return SessionModel.getModel(); }

	/**
	 * Obtain a handle to the workspace.
	 * 
	 * @return A handle to the workspace.
	 */
	Workspace getWorkspace() {
		return WorkspaceModel.getModel().getWorkspace();
	}

	private void deleteTree(final File rootDirectory) {
		FileUtil.deleteTree(rootDirectory);
	}

	/**
	 * Obtain a handle to the root project.
	 * 
	 * @return A handle to the root project.
	 */
	private Project getMyProjects() throws ParityException {
		return getProjectModel().getMyProjects();
	}

	private File getWorkspaceDirectory(final ModelTestUser modelTestUser) {
		final URL rootURL = ResourceUtil.getURL("resources/workspace");
		File rootDirectory = null;
		try { rootDirectory = new File(rootURL.toURI()); }
		catch(URISyntaxException usx) {
			ModelTestCase.fail(modelTestCase.getFailMessage(usx));
		}
		return new File(rootDirectory, modelTestUser.getUsername());
	}
}
