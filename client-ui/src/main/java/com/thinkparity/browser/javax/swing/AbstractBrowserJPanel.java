/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import javax.swing.JPanel;

import com.thinkparity.browser.model.ModelProvider;

import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AbstractBrowserJPanel extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	protected Preferences getPreferences() {
		return getWorkspace().getPreferences();
	}

	protected Workspace getWorkspace() {
		final WorkspaceModel workspaceModel = getWorkspaceModel();
		return workspaceModel.getWorkspace();
	}

	private WorkspaceModel getWorkspaceModel() {
		return ModelProvider.getWorkspaceModel(getClass());	
	}
}
