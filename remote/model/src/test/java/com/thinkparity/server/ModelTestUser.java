/*
 * Created On: Aug 2, 2006 9:53:35 AM
 */
package com.thinkparity.model;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.Constants.Server;
import com.thinkparity.model.Constants.Session;

/**
 * <b>Title:</b>thinkParity Model Test User<br>
 * <b>Description:</b>A series of users to use for testing.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ModelTestUser {

    /**
     * Obtain the jUnit test user.
     * 
     * @return A model test user.
     */
	public static ModelTestUser getJUnit() {
		return new ModelTestUser("junit");
	}

    /**
     * Obtain the jUnit X test user.
     * 
     * @return A model test user.
     */
    public static ModelTestUser getX() {
		return new ModelTestUser("junit.x");
	}

    /**
     * Obtain the jUnit Y test user.
     * 
     * @return A model test user.
     */
    public static ModelTestUser getY() {
        return new ModelTestUser("junit.y");
    }

    /**
     * Obtain the jUnit Z test user.
     * 
     * @return A model test user.
     */
    public static ModelTestUser getZ() {
        return new ModelTestUser("junit.z");
    }

    /** The user's username. */
    private final String username;

	/**
     * Create ModelTestUser.
     * 
     * @param username
     *            The user's username.
     */
	private ModelTestUser(final String username) {
		super();
		this.username = username;
	}

    /**
     * Obtain the jabber id.
     * 
     * @return A jabber id.
     */
	public JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(
				new StringBuffer(username)
				.append('@')
				.append(Server.HOST)
				.append('/')
				.append(Session.RESOURCE).toString());
	}
}
