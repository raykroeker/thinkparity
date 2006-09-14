/*
 * Feb 18, 2006
 */
package com.thinkparity.ophelia.browser.util;

import com.thinkparity.codebase.model.user.User;

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

	/**
	 * Create a ModelUtil.
	 */
	private ModelUtil() {
		super();
	}

	private String doGetName(final User user) {
        final String name = user.getName();
        if(null != name && 0 < name.length()) { return name; }
		else { return user.getUsername(); }
	}
}
