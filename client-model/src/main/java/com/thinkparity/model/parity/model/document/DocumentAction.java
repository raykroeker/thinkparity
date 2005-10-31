/*
 * Oct 31, 2005
 */
package com.thinkparity.model.parity.model.document;

/**
 * Document actions.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum DocumentAction {

	CREATE("create"), RECEIVE("receive");

	/**
	 * Obtain a document action based upon its id.
	 * 
	 * @param actionId
	 *            The document action id.
	 * @return The document action.
	 */
	public static DocumentAction fromId(final String actionId) {
		if(actionId.equals("create")) { return CREATE; }
		else if(actionId.equals("receive")) { return RECEIVE; }
		else {
			throw new IllegalArgumentException(
					"Unrecognized document action:  " + actionId);
		}
	}

	/**
	 * The action id.
	 */
	private final String actionId;

	/**
	 * Create a DocumentAction.
	 * 
	 * @param actionId
	 *            The action id.
	 */
	private DocumentAction(final String actionId) {
		this.actionId = actionId;
	}

	/**
	 * Obtain the action id for the action.
	 * 
	 * @return The action id.
	 */
	public String getActionId() { return actionId; }
}
