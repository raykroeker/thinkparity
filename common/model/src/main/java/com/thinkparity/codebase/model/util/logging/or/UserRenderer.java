/*
 * 18-Oct-2005
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.codebase.model.user.User;

import org.apache.log4j.or.ObjectRenderer;

/**
 * This class is used whenever a logger is required to output a User.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class UserRenderer implements ObjectRenderer {

    /**
	 * Create UserRenderer.
     * 
	 */
	public UserRenderer() {
        super();
	}

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if (null == o) {
            return Separator.Null.toString();
		}
		else {
            final User user = (User) o;
            return StringUtil.toString(getClass(), "id", user.getId(),
                    "localId", user.getLocalId(), "name", user.getName());
		}
	}
}
