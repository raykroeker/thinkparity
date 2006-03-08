/*
 * Feb 13, 2006
 */
package com.thinkparity.model;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends TestCase {

	/**
	 * The JUnit eXtension test session.
	 * 
	 */
	private static final TestSession testSession;

	static {
		// set non ssl mode
		System.setProperty("parity.insecure", "true");
		// set staging system
		System.setProperty("parity.serverhost", "rkutil.raykroeker.com");
		// set archive directory
		testSession = TestCase.getTestSession();
		final ModelTestUser modelTestUser = ModelTestUser.getJUnit();
		testSession.setData("modelTestUser", modelTestUser);
		System.setProperty("parity.workspace",
				new File(testSession.getSessionDirectory(), "workspace")
				.getAbsolutePath());
		final Preferences preferences = WorkspaceModel.getModel().getWorkspace().getPreferences();
		preferences.setUsername(modelTestUser.getUsername());
		preferences.setArchiveOutputDirectory(new File(testSession.getSessionDirectory(), "Archive"));
	}

	private DocumentModel documentModel;

	private IndexModel indexModel;

	/**
	 * Create a ModelTestCase.
	 * 
	 * @param name
	 *            The test name
	 */
	protected ModelTestCase(final String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected DocumentModel getDocumentModel() {
		if(null == documentModel) {
			documentModel = DocumentModel.getModel();
		}
		return documentModel;
	}

	protected IndexModel getIndexModel() {
		if(null == indexModel) {
			indexModel = IndexModel.getModel();
		}
		return indexModel;
	}

	/**
	 * @see com.raykroeker.junitx.TestCase#getInputFiles()
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	
	}

	/**
	 * Obtain the junit test user.
	 * 
	 * @return The junit test user.
	 */
	protected ModelTestUser getModelTestUser() {
		return (ModelTestUser) testSession.getData("modelTestUser");
	}
}
