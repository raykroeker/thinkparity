/*
 * Nov 30, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;


import com.thinkparity.desdemona.model.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserRenderer implements ObjectRenderer {

	private static final String PREFIX =
		User.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String USERNAME = "username:";

	/**
	 * Create a UserRenderer.
	 */
	public UserRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX).toString();
		}
		else {
			final User u = (User) o;
			return new StringBuffer(PREFIX)
				.append(USERNAME).append(u.getId())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}
}
