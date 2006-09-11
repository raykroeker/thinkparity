/*
 * 18-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.codebase.model.user.User;


/**
 * This class is used whenever a logger is required to output a User.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class UserRenderer implements ObjectRenderer {

	/**
	 * Prefix to the log statement.
	 */
	private static final String PREFIX =
		new StringBuffer(User.class.getCanonicalName()).append(":  ").toString();

	/**
	 * Null user rendering.
	 */
	private static final String NULL_USER =
		new StringBuffer(PREFIX).append("null").toString();

	/**
	 * Create a UserRenderer.
	 */
	public UserRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) { return NULL_USER; }
		else {
			return new StringBuffer(PREFIX)
				.append(((User) o).getSimpleUsername()).toString();
		}
	}
}
