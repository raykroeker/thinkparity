/*
 * Jul 1, 2005
 */
package com.thinkparity.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ResourceUtil;


/**
 * ModelTestCaseHelper
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelTestCaseHelper {

	private static final Long vmStartTime = System.currentTimeMillis();

	static {
		System.setProperty("parity.dev.env", "true");
		System.setProperty("parity.host", "mob-002");
		System.setProperty("parity.username", "junit");
		final String parityWorkspace = new StringBuffer(System.getProperty("user.dir"))
			.append(File.separator).append("src")
			.append(File.separator).append("test")
			.append(File.separator).append("resources")
			.append(File.separator).append("data-set").toString();
		System.setProperty("parity.workspace", parityWorkspace);
	}

	private final ModelTestCase modelTestCase;

	/**
	 * Create a ModelTestCaseHelper
	 */
	ModelTestCaseHelper(final ModelTestCase modelTestCase) {
		super();
		this.modelTestCase = modelTestCase;
	}

	String getUniqueProjectName(final Integer index) {
		return "JUnit.Test." + vmStartTime + "." + index;
	}

	void deleteWorkspace(final ModelTestUser modelTestUser) {
		final File workspaceDirectory = getWorkspaceDirectory(modelTestUser);
		deleteTree(workspaceDirectory);
		ModelTestCase.assertFalse(
				"Could not delete workspace directory.",
				workspaceDirectory.exists());
		workspaceDirectory.mkdir();
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

	ModelTestUser getModelTestUserJUnit0() { return ModelTestUser.getJUnit0(); }
}
