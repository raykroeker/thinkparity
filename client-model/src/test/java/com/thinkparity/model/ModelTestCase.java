/*
 * Aug 6, 2005
 */
package com.thinkparity.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
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
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends TestCase {

	/**
	 * Counter of the number of projects created.
	 */
	private static Integer projectCount = 0;

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = ModelLoggerFactory.getLogger(getClass());

	/**
	 * Helper class for the parity test cases.  Used to offload the
	 * implementation of the data retreival functionality.
	 */
	private final ModelTestCaseHelper helper;

	/**
	 * Create a ModelTestCase
	 * @param name
	 */
	protected ModelTestCase(String name) {
		super(name);
		this.helper = new ModelTestCaseHelper(this);
	}

	/**
	 * Assert that the document list provided contains the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected void assertContains(final Collection<Document> documentList,
			Document document) {
		helper.assertContains(documentList, document);
	}

	/**
	 * Assert that the document list provided doesn't contain the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected void assertNotContains(final Collection<Document> documentList,
			Document document) {
		helper.assertNotContains(documentList, document);
	}

	/**
	 * Create a project for the given test. This method does not check for
	 * project existance.  It will attempt to create a project every time.
	 *
	 * @param testName
	 *            The name of the test currently being setup\run; ie
	 *            testCreateDocument
	 */
	protected Project createTestProject(final String test) throws ParityException {
		final String name = String.valueOf(projectCount++);
		final String description = "Automated test project for:  " +
			getClass().getCanonicalName() + "." + test + "()";
		return getProjectModel().create(helper.getJUnitProject().getId(), name, description);
	}

	/**
	 * Obtain a handle to the document model.
	 * 
	 * @return A handle to the document model.
	 */
	protected DocumentModel getDocumentModel() {
		return helper.getDocumentModel();
	}

	protected String getFailMessage(final Throwable t) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter, true);
		t.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	/**
	 * Obtain a single jUnit test file.
	 * 
	 * @param ordinal
	 * @return The jUnit test file.
	 */
	protected ModelTestFile getJUnitTestFile(final Integer ordinal) {
		return helper.getJUnitTestFile(ordinal);
	}

	/**
	 * Obtain a single jUnit test file.
	 * 
	 * @param name
	 * @return The jUnit test file.
	 */
	protected ModelTestFile getJUnitTestFile(final String name) {
		return helper.getJUnitTestFile(name);
	}

	/**
	 * Obtain the files for use with the jUnit test framework.
	 * 
	 * @return The list of test files.
	 */
	protected Collection<ModelTestFile> getJUnitTestFiles() {
		return helper.getJUnitTestFiles();
	}

	/**
	 * Obtain the number of jUnit test files.
	 * 
	 * @return The number of jUnit test files.
	 */
	protected Integer getJUnitTestFilesSize() {
		return helper.getJUnitTestFilesSize();
	}

	/**
	 * Obtain random test text to use with junit.
	 * 
	 * @param size
	 *            The size of the text in characters.
	 * @return The text.
	 */
	protected String getJUnitTestText(final Integer size) {
		return helper.getJUnitTestText(size);
	}

	/**
	 * Obtain the junit test user.
	 * 
	 * @return The junit test user.
	 */
	protected ModelTestUser getModelTestUser() {
		return helper.getJUnitTestUser();
	}
	
	/**
	 * Obtain a handle to the parity preferences.
	 * 
	 * @return A handle to the parity preferences.
	 */
	protected Preferences getPreferences() {
		return helper.getPreferences();
	}

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	protected ProjectModel getProjectModel() {
		return ProjectModel.getModel();
	}

	/**
	 * Obtain a handle to a session model.
	 * 
	 * @return A handle to a session model.
	 */
	protected SessionModel getSessionModel() {
		return helper.getSessionModel();
	}

	/**
	 * Obtain a handle to the parity workspace.
	 * 
	 * @return A handle to the parity workspace.
	 */
	protected Workspace getWorkspace() {
		return getWorkspaceModel().getWorkspace();
	}

	/**
	 * Obtain a handle to the parity workspace model.
	 * 
	 * @return A handle to the parity workspace model.
	 */
	protected WorkspaceModel getWorkspaceModel() {
		return WorkspaceModel.getModel();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception { super.setUp(); }

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception { super.tearDown(); }
}
