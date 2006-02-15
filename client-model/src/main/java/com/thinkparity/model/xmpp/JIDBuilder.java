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
	}

	/**
	 * Obtain the qualified jabber id.
	 * 
	 * @return The qualified jabber id.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

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
}
