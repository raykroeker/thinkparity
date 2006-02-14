/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.NotYetImplementedAssertion;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;

/**
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends com.thinkparity.model.ModelTestCase {

	/**
	 * The JUnit eXtension test session.
	 * 
	 */
	private static final TestSession testSession;

	static {
		testSession = TestCase.getTestSession();
		final ModelTestUser modelTestUser = ModelTestUser.getJUnit();
		testSession.setData("modelTestUser", modelTestUser);
		System.setProperty("parity.workspace",
				new File(testSession.getSessionDirectory(), "workspace")
				.getAbsolutePath());
		WorkspaceModel.getModel().getWorkspace().getPreferences().setUsername(modelTestUser.getUsername());
	}

	/**
	 * Assert that the document list provided contains the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected static void assertContains(final Collection<Document> documentList,
			Document document) {
		ModelTestCaseHelper.assertContains(documentList, document);
	}

	/**
	 * Assert that the document list provided doesn't contain the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected static void assertNotContains(final Collection<Document> documentList,
			Document document) {
		ModelTestCaseHelper.assertNotContains(documentList, document);
	}

	private InternalDocumentModel iDocumentModel;

	/**
	 * The parity preferences.
	 * 
	 */
	private Preferences preferences;

	/**
	 * The session model.
	 * 
	 */
	private SessionModel sessionModel;

	/**
	 * The parity workspace.
	 * 
	 */
	private Workspace workspace;

	/**
	 * The workspace model.
	 * 
	 */
	private WorkspaceModel workspaceModel;

	/**
	 * Create a ModelTestCase
	 * 
	 * @param name
	 *            The test name.
	 */
	protected ModelTestCase(final String name) { super(name); }
	
	/**
	 * @see com.raykroeker.junitx.TestCase#createFailMessage(java.lang.Throwable)
	 * 
	 */
	protected String createFailMessage(Throwable t) {
		testLogger.error("Failure", t);
		return super.createFailMessage(t);
	}

	protected User findUser(final String username) {
		try {
			final Collection<User> roster = getSessionModel().getRosterEntries();
			for(final User user : roster) {
				if(user.getSimpleUsername().equals(username)) { return user; }
			}
			return null;
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * Obtain a single test file.
	 * 
	 * @param name
	 *            The file name.
	 * @return The test file.
	 */
	protected File getInputFile(final String name) throws IOException {
		for(final File file : getInputFiles()) {
			if(file.getName().equals(name)) { return file; }
		}
		return null;
	}

	/**
	 * @see com.raykroeker.junitx.TestCase#getInputFiles()
	 * 
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	}

	protected InternalDocumentModel getInternalDocumentModel() {
		if(null == iDocumentModel) {
			return DocumentModel.getInternalModel(new Context(getClass()));
		}
		return iDocumentModel;
	}

	/**
	 * Obtain the junit test user.
	 * 
	 * @return The junit test user.
	 */
	protected ModelTestUser getModelTestUser() {
		return (ModelTestUser) testSession.getData("modelTestUser");
	}

	/**
	 * Obtain the parity preferences.
	 * 
	 * @return The parity preferences.
	 */
	protected Preferences getPreferences() {
		if(null == preferences) {
			preferences = getWorkspace().getPreferences();
		}
		return preferences;
	}

	/**
	 * Obtain the session model.
	 * 
	 * @return The session model.
	 */
	protected SessionModel getSessionModel() {
		if(null == sessionModel) {
			sessionModel = SessionModel.getModel();
		}
		return sessionModel;
	}

	/**
	 * Obtain the parity workspace.
	 * 
	 * @return The parity workspace.
	 */
	protected Workspace getWorkspace() {
		if(null == workspace) {
			workspace = getWorkspaceModel().getWorkspace();
		}
		return workspace;
	}

	/**
	 * Obtain a handle to the parity workspace model.
	 * 
	 * @return A handle to the parity workspace model.
	 */
	protected WorkspaceModel getWorkspaceModel() {
		if(null == workspaceModel) {
			workspaceModel = WorkspaceModel.getModel();
		}
		return workspaceModel;
	}

	/**
	 * Determine if the parity error is generated by the model due to operating
	 * system constraints.
	 * 
	 * @param px
	 *            The parity error.
	 * @return True if the error is caused by a NotYetImplementedAssertion and
	 *         the current operating system is linux.
	 */
	protected boolean isNYIAOnLinux(final ParityException px) {
		Throwable cause = px.getCause();
		while(null != cause) {
			if(NotYetImplementedAssertion.class.isAssignableFrom(cause.getClass())) {
				switch(OSUtil.getOS()) {
				case LINUX:
					testLogger.warn("[PARITY] Running test on un-supported platform.");
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	protected void login() {
		final ModelTestUser modelTestUser = getModelTestUser();
		try {
			getSessionModel().login(
					modelTestUser.getUsername(), modelTestUser.getPassword());
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

//	/**
//	 * Open the hypersonic database manager.
//	 *
//	 */
//	protected void openHypersonicManager() {
//		final String url = new StringBuffer("jdbc:hsqldb:file:")
//			.append(getWorkspace().getDataDirectory())
//			.append(File.separator)
//			.append(IParityModelConstants.DIRECTORY_NAME_DB_DATA)
//			.append(File.separator)
//			.append(IParityModelConstants.FILE_NAME_DB_DATA)
//			.toString();
//		final String[] args = {
//				"-url", url,
//				"-user", "sa",
//				"-noexit"
//		};
//		org.hsqldb.util.DatabaseManagerSwing.main(args);
//	}

	protected void logout() {
		try { getSessionModel().logout(); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception { super.setUp(); }

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception { super.tearDown(); }
}
