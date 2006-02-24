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

	static {
		singleton = new ModelUtil();
		singletonLock = new Object();
	}

	public static Preferences getPreferences() {
		synchronized(singletonLock) { return singleton.doGetPreferences(); }
	}

	public static Workspace getWorkspace() {
		synchronized(singletonLock) { return singleton.doGetWorkspace(); }
	}

	private final Preferences preferences;

	private final Workspace workspace;

	/**
	 * Create a ModelUtil.
	 */
	private ModelUtil() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.preferences = workspace.getPreferences();
	}

	private Preferences doGetPreferences() { return preferences; }
	private Workspace doGetWorkspace() { return workspace; }
}
