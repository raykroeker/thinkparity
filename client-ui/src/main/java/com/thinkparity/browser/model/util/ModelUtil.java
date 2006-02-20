/*
 * Feb 18, 2006
 */
package com.thinkparity.browser.model.util;

import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelUtil {

	private static final ModelUtil singleton;

	private static final Object singletonLock;

	public static Preferences getPreferences() {
		synchronized(singletonLock) { return singleton.doGetPreferences(); }
	}

	static {
		singleton = new ModelUtil();
		singletonLock = new Object();
	}

	private final Preferences preferences;

	/**
	 * Create a ModelUtil.
	 */
	private ModelUtil() {
		super();
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		this.preferences = workspace.getPreferences();
	}

	private Preferences doGetPreferences() { return preferences; }
}
