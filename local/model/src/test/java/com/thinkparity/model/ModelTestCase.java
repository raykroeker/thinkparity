/*
 * Feb 13, 2006
 */
package com.thinkparity.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

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
		testSession = TestCase.getTestSession();
		final ModelTestUser modelTestUser = ModelTestUser.getJUnit();
		testSession.setData("modelTestUser", modelTestUser);
		System.setProperty("parity.workspace",
				new File(testSession.getSessionDirectory(), "workspace")
				.getAbsolutePath());
		WorkspaceModel.getModel().getWorkspace().getPreferences().setUsername(modelTestUser.getUsername());
	}

	private DocumentModel documentModel;

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

	protected List<User> proxy(final Collection<User> c) {
		final List<User> l = new LinkedList<User>();
		for(final User u : c) { l.add(u); }
		return l;
	}
}
