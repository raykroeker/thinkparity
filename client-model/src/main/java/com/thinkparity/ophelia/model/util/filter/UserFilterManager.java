/*
 * Created On: Jun 23, 2006 2:34:21 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.util.filter;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.model.user.User;


/**
 * <b>Title:</b>thinkParity Model User Filter Manager<br>
 * <b>Description:</b>The artifact filter manager takes ordered lists of
 * users and applies a chain of filters to the list.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserFilterManager {

	/** A singleton implementation. */
	private static final UserFilterManager SINGLETON;

	static { SINGLETON = new UserFilterManager(); }

	/**
     * Filter a list of users.
     * 
     * @param list
     *            The list of users.
     * @param filter
     *            The users filter.
     */
	public static void filter(final List<? extends User> list,
			final Filter<? super User> filter) {
		SINGLETON.doFilter(list, filter);
	}

	/** Create ArtifactFilterManager. */
	private UserFilterManager() { super(); }

	/**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
	private void doFilter(final List<? extends User> list,
			final Filter<? super User> filter) {
        User user;
		for(final Iterator<? extends User> i = list.iterator(); i.hasNext();) {
            user = i.next();
			if(filter.doFilter(user)) { i.remove(); }
		}
	}
}
