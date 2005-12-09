/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * An abstraction of the artifact jabber iq extensions for parity. This includes
 * the create,falg,subscribe and unsubscribe extensions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQArtifact extends IQParity {

	/**
	 * Artifact actions that are possible to perform.
	 * 
	 */
	protected enum Action {
		CREATEARTIFACT, FLAGARTIFACT, REQUESTKEY, SUBSCRIBEUSER, UNSUBSCRIBEUSER
	}

	/**
	 * Finish xml tag for action. 
	 */
	private static final String XML_ACTION_FINISH = "</action>";

	/**
	 * Start xml tag for action.
	 */
	private static final String XML_ACTION_START = "<action>";

	/**
	 * Finish xml tag for the query.
	 */
	private static final String XML_QUERY_FINISH = "</query>";

	private static final String XML_QUERY_NAMESPACE_ROOT = "jabber:iq:parity:";

	/**
	 * Start xml tag for the query.
	 */
	private static final String XML_QUERY_START = "<query xmlns=\"";

	/**
	 * Finish xml tag for actionUUID.
	 */
	private static final String XML_UUID_FINISH = "</uuid>";

	/**
	 * Start xml tag for actionUUID.
	 */
	private static final String XML_UUID_START = "<uuid>";

	/**
	 * Action to perform.
	 */
	private final Action action;

	/**
	 * Artifact unique id.
	 */
	private UUID artifactUUID;

	/**
	 * Create a IQArtifact.
	 */
	protected IQArtifact(final Action action, final UUID artifactUUID) {
		super();
		this.action = action;
		setArtifactUUID(artifactUUID);
	}

	/**
	 * Obtain the action.
	 * 
	 * @return The action.
	 */
	public Action getAction() { return action; }

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getArtifactUUIDXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	/**
	 * Set the artifact unique id.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public void setArtifactUUID(final UUID artifactUUID) {
		this.artifactUUID = artifactUUID;
	}

	/**
	 * Finish xml for the query.
	 * 
	 * @return The finish xml tag for the query.
	 */
	protected String finishQueryXML() { return IQArtifact.XML_QUERY_FINISH; }

	/**
	 * Obtain the xml tag for the action.
	 * 
	 * @return The xml tag for the action.
	 */
	protected String getActionXML() {
		return new StringBuffer(IQArtifact.XML_ACTION_START)
			.append(getAction().toString())
			.append(IQArtifact.XML_ACTION_FINISH).toString();
	}

	/**
	 * Obtain the xml tag for the artifact unique id.
	 * 
	 * @return The xml tag for the artifact unique id.
	 */
	protected String getArtifactUUIDXML() {
		return new StringBuffer(IQArtifact.XML_UUID_START)
			.append(getArtifactUUID().toString())
			.append(IQArtifact.XML_UUID_FINISH).toString();
	}

	/**
	 * Start the xml tag for the query.
	 * 
	 * @return The start xml tag for the query.
	 */
	protected String startQueryXML() {
		return new StringBuffer(IQArtifact.XML_QUERY_START)
			.append(IQArtifact.XML_QUERY_NAMESPACE_ROOT)
			.append(action.toString().toLowerCase())
			.append("\">").toString();
	}
}
