/*
 * Jul 1, 2005
 */
package com.thinkparity.model;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.tree.TreeModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;


/**
 * ModelTestCaseHelper
 * @author raykroeker@gmail.com
 * @version 1.1
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
		final File jUnitWorkspace =
			new File(resourcesDirectory, "junit.workspace");
		if(jUnitWorkspace.exists())
			FileUtil.deleteTree(jUnitWorkspace);
		// set the test files
		final File jUnitResourcesFiles =
			new File(resourcesDirectory, "junit.files");
		jUnitTestFiles = new Vector<ModelTestFile>(4);
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
		// set the test user
		jUnitTestUser = ModelTestUser.getJUnit();
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
			final Project rootProject = getRootProject();
			final String name = jUnitSessionId;
			final String description = name;
			ModelTestCaseHelper.jUnitProject =
				getProjectModel().create(rootProject, name, description);
		}
		return ModelTestCaseHelper.jUnitProject;
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
	 * Obtain a handle to the root project.
	 * 
	 * @return A handle to the root project.
	 */
	Project getRootProject() throws ParityException {
		return getProjectModel().getRootProject();
	}
	
	/**
	 * Obtain a handle to the session model.
	 * 
	 * @return A handle to the session model.
	 */
	SessionModel getSessionModel() { return SessionModel.getModel(); }

	/**
	 * Obtain a handle to the tree model.
	 * 
	 * @return A handle to the tree model.
	 */
	TreeModel getTreeModel() { return TreeModel.getModel(); }
	
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
