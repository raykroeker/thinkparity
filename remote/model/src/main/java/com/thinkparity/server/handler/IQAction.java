package com.thinkparity.server.handler;

import com.thinkparity.server.handler.controller.IQController;
import com.thinkparity.server.handler.controller.artifact.CreateArtifact;
import com.thinkparity.server.handler.controller.artifact.FlagArtifact;
import com.thinkparity.server.handler.controller.user.SubscribeUser;
import com.thinkparity.server.handler.controller.user.UnsubscribeUser;

/**
 * List of possible actions available within the parity artifact handler.
 * 
 */
public enum IQAction {

	/**
	 * Create an artifact.
	 */
	CREATEARTIFACT(new CreateArtifact()),

	/**
	 * Flag an artifact.
	 */
	FLAGARTIFACT(new FlagArtifact()),

	/**
	 * Subscribe a user to an artifact.
	 */
	SUBSCRIBEUSER(new SubscribeUser()),

	/**
	 * Unsubscribe a user from an artifact.
	 */
	UNSUBSCRIBEUSER(new UnsubscribeUser());

	/**
	 * Handle to the controller for the action.
	 */
	private IQController controller;

	/**
	 * Create an IQAction.
	 * 
	 * @param controller
	 *            Reference to the controller that can handle the action.
	 */
	private IQAction(final IQController controller) {
		this.controller = controller;
	}

	/**
	 * Obtain the controller for the action.
	 * 
	 * @return The controller for the action.
	 */
	public IQController getController() { return controller; }
}
