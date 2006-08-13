/*
 * Feb 18, 2006
 */
package com.thinkparity.browser.platform.util.model;

import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

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

	public static String getName(final User user) {
		synchronized(singletonLock) { return singleton.doGetName(user); }
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

	private String doGetName(final User user) {
        final String name = user.getName();
        if(null != name && 0 < name.length()) { return name; }
		else { return user.getUsername(); }
	}

	private Preferences doGetPreferences() { return preferences; }

	private Workspace doGetWorkspace() { return workspace; }
}
