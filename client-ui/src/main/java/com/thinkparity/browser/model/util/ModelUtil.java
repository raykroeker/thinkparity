/*
 * Feb 18, 2006
 */
package com.thinkparity.browser.model.util;

import com.thinkparity.codebase.StringUtil.Separator;

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
		if(isSetFirstAndLastName(user)) {
			return new StringBuffer(user.getFirstName())
                .append(Separator.Space)
                .append(user.getLastName())
				.toString();
		}
		else if(isSetFirstName(user)) { return user.getFirstName(); }
		else { return user.getUsername(); }
	}

	private Boolean isSetFirstAndLastName(final User user) {
		return isSetFirstName(user) && isSetLastName(user);
	}

	private Boolean isSetFirstName(final User user) {
		final String firstName = user.getFirstName();
		return null != firstName && 0 < firstName.length();
	}
	
	private Boolean isSetLastName(final User user) {
		final String lastName = user.getLastName();
		return null != lastName && 0 < lastName.length();
	}

	private Preferences doGetPreferences() { return preferences; }

	private Workspace doGetWorkspace() { return workspace; }
}
