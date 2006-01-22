/*
 * Jan 18, 2006
 */
package com.thinkparity.browser;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.ui.MainWindow;
import com.thinkparity.browser.ui.display.Display;
import com.thinkparity.browser.ui.display.DisplayId;
import com.thinkparity.browser.ui.display.avatar.Avatar;
import com.thinkparity.browser.ui.display.avatar.AvatarFactory;
import com.thinkparity.browser.ui.display.avatar.AvatarId;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * The controller is used to manage state as well as control display of the
 * parity browser.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Controller {

	/**
	 * Singleton instance of the controller.
	 * 
	 */
	private static final Controller singleton;

	static { singleton = new Controller(); }

	/**
	 * Obtain the instance of the controller.
	 * 
	 * @return The instance of the controller.
	 */
	static Controller getInstance() { return singleton; }

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The project api.
	 * 
	 */
	protected final ProjectModel projectModel;

	/**
	 * Provides a map of all avatar input.
	 * 
	 */
	private final Map<AvatarId, Object> avatarInputMap;

	/**
	 * Main window.
	 * 
	 */
	private MainWindow mainWindow;

	/**
	 * The main project id.
	 * 
	 */
	private UUID projectId;

	/**
	 * The state information for the controller.
	 * 
	 */
	private final ControllerState state;

	/**
	 * Create a Controller [Singleton]
	 * 
	 */
	private Controller() {
		super();
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
		this.projectModel = ModelFactory.getInstance().getProjectModel(getClass());
		this.state = new ControllerState(this);
	}

	/**
	 * Close the main window.
	 *
	 */
	public void closeMainWindow() {
		Assert.assertNotNull(
				"Cannot close main window before it is open.", mainWindow);
	}

	/**
	 * Display an avatar.
	 * 
	 * @param displayId
	 *            The display to use.
	 * @param avatarId
	 *            The avatar to display.
	 */
	public void displayAvatar(final DisplayId displayId, final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display on a null display.", displayId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Display display = mainWindow.getDisplay(displayId);

		final Avatar currentAvatar = display.getAvatar();
		state.saveAvatarState(currentAvatar);
		if(null != currentAvatar) { currentAvatar.setDisplay(null); }

		final Avatar nextAvatar = AvatarFactory.create(avatarId);
		state.loadAvatarState(nextAvatar);

		final Object input = getAvatarInput(avatarId);
		if(null == input) { logger.info("Null input:  " + avatarId); }
		else { nextAvatar.setInput(getAvatarInput(avatarId)); }
		nextAvatar.setDisplay(display);

		display.setAvatar(nextAvatar);
		display.displayAvatar();

		final Display[] displays = getDisplays();
		for(Display d : displays) {
			d.revalidate();
			d.repaint();
		}
	}

	/**
	 * Open the main window.
	 *
	 */
	public void openMainWindow() {
		Assert.assertIsNull("Cannot reopen main window.", mainWindow);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	mainWindow = MainWindow.open(Controller.this);
            	displayAvatar(DisplayId.LOGO, AvatarId.BROWSER_LOGO);
            	displayAvatar(DisplayId.CONTENT, AvatarId.DOCUMENT_LIST);
            	displayAvatar(DisplayId.INFO, AvatarId.DOCUMENT_HISTORY_LIST);
        		setProjectId();
            }
        });
	}

	/**
	 * Obtain the input for an avatar.
	 * @param avatarId The avatar id.
	 * @return The avatar input.
	 */
	private Object getAvatarInput(final AvatarId avatarId) {
		return avatarInputMap.get(avatarId);
	}

	/**
	 * Obtain a list of all of the displays.
	 * 
	 * @return A list of all of the displays.
	 */
	private Display[] getDisplays() { return mainWindow.getDisplays(); }

	/**
	 * Set the input for an avatar. If the avatar is currently being displayed;
	 * it will be set immediately; otherwise it will be stored in the local
	 * hash; and set when the avatar is displayed.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @param input
	 *            The avatar input.
	 */
	private void setInput(final AvatarId avatarId, final Object input) {
		Assert.assertNotNull("Cannot set the input for a null avatar.", avatarId);
		final Display[] displays = getDisplays();
		Avatar avatar;
		for(Display display : displays) {
			avatar = display.getAvatar();
			if(null == avatar) {
				logger.warn("No avatar displayed:  " + display.getId());
			}
			else {
				if(avatarId == avatar.getId()) { avatar.setInput(input); }
				else { logger.debug(avatarInputMap.put(avatarId, input)); }
			}
		}
	}

	/**
	 * Set the project id.
	 *
	 */
	private void setProjectId() {
		try { projectId = projectModel.getMyProjects().getId(); }
		catch(ParityException px) {
			// NOTE Error Handler Code
			logger.fatal("Could not retreive main project.", px);
		}
		setInput(AvatarId.DOCUMENT_LIST, projectId);
	}
}
