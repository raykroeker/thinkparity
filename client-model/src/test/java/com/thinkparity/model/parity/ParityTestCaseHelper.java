/*
 * Jul 1, 2005
 */
package com.thinkparity.model.parity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;


/**
 * ParityTestCaseHelper
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityTestCaseHelper {

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

	/**
	 * Handle to the workspace model.
	 */
	private final WorkspaceModel workspaceModel;

	/**
	 * Create a ParityTestCaseHelper
	 */
	ParityTestCaseHelper() {
		super();
		this.workspaceModel = WorkspaceModel.getModel();
	}

	/**
	 * Obtain the workspace url expected based upon the parity.workspace system
	 * variable set above.
	 * @return
	 */
	URL getExpectedWorkspaceDataURL() {
		final StringBuffer expectedURL = new StringBuffer()
			.append(getExpectedWorkspaceURL().toString())
			.append(".data/");
		return buildURL(expectedURL);
	}

	/**
	 * Obtain the workspace url expected based upon the parity.workspace system
	 * variable set above.
	 * @return
	 */
	URL getExpectedWorkspaceLogURL() {
		final StringBuffer expectedURL = new StringBuffer()
			.append(getExpectedWorkspaceURL().toString())
			.append(".log/");
		return buildURL(expectedURL);
	}

	/**
	 * Obtain the workspace url expected based upon the parity.workspace system
	 * variable set above.
	 * @return
	 */
	URL getExpectedWorkspaceURL() {
		final StringBuffer expectedURL = new StringBuffer("file:")
			.append(System.getProperty("parity.workspace"))
			.append("/.Parity Software/Parity/");
		return buildURL(expectedURL);
	}

	String getUniqueProjectName(final Integer index) {
		return "JUnit.Test." + vmStartTime + "." + index;
	}

	/**
	 * Obtain the workspace for the parity system.
	 * @return <code>Workspace</code>
	 */
	Workspace getWorkspace() { return workspaceModel.getWorkspace(); }
	
	private URL buildURL(final StringBuffer url) {
		try { return new URL(url.toString()); }
		catch(MalformedURLException murlx) {
			ParityTestCase.fail(murlx.getMessage());
			return null;
		}
	}
}
