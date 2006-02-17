/*
 * Feb 15, 2006
 */
package com.thinkparity.model.xmpp;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JIDBuilder {

	private static final Preferences preferences;

	static {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		preferences = workspace.getPreferences();
	}

	/**
	 * The qualified jabber id.
	 * 
	 */
	private String qualifiedJID;

	/**
	 * The qualified username.
	 * 
	 */
	private String qualifiedUsername;

	public JIDBuilder(final String qualifiedJID) {
		super();
		final int indexOfAt = qualifiedJID.indexOf('@');
		final String username = qualifiedJID.substring(0, indexOfAt);
		final int indexOfSlash = qualifiedJID.indexOf("/");
		final String host = qualifiedJID.substring(indexOfAt + 1, indexOfSlash);
		final String resource = qualifiedJID.substring(indexOfSlash + 1);
		setQUalifiedJID(username, host, resource);
		setQualifiedUsername(username, host);
	}

	/**
	 * Create a JIDBuilder.
	 * 
	 * @param user
	 *            The parity user.
	 */
	public JIDBuilder(final User user) {
		super();
		setQUalifiedJID(user.getSimpleUsername(),
				preferences.getServerHost(),
				IParityModelConstants.PARITY_CONNECTION_RESOURCE);
		setQualifiedUsername(user.getSimpleUsername(), preferences.getServerHost());
	}

	/**
	 * Obtain the qualified jabber id:  user@host/resource
	 * 
	 * @return The qualified jabber id.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

	/**
	 * Obtain the qualified username:  user@host
	 * 
	 * @return The qualified username.
	 */
	public String getQualifiedUsername() {
		return qualifiedUsername;
	}

	/**
	 * Set the qualified jabber id.
	 * 
	 * @param username
	 *            The user name.
	 * @param host
	 *            The host.
	 * @param resource
	 *            The resource.
	 */
	private void setQUalifiedJID(final String username, final String host,
			final String resource) {
		qualifiedJID = new StringBuffer(username)
			.append("@")
			.append(host)
			.append("/")
			.append(resource)
			.toString();
	}

	/**
	 * Set the qualified username.
	 * 
	 * @param username
	 *            The username.
	 * @param host
	 *            The host.
	 */
	private void setQualifiedUsername(final String username, final String host) {
		qualifiedUsername = new StringBuffer(username)
			.append("@")
			.append(host)
			.toString();
	}
}
