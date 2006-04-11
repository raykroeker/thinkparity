/*
 * Mar 16, 2006
 */
package com.thinkparity.browser.platform.login;

import com.thinkparity.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.login.ui.LoginAvatar;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LoginHelper {

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;

	/**
	 * The main platform.
	 * 
	 */
	private final Platform platform;

	/**
	 * The parity session interface.
	 * 
	 */
	private final SessionModel sessionModel;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry windowRegistry;

	/**
	 * Create a LoginHelper.
	 */
	public LoginHelper(final Platform platform) {
		super();
		this.platform = platform;
		this.sessionModel = platform.getModelFactory().getSessionModel(getClass());
		this.avatarRegistry = platform.getAvatarRegistry();
		this.windowRegistry = platform.getWindowRegistry();
	}

	/**
	 * Determine whether or not the user is logged in.
	 * 
	 * @return True if the user is logged in false otherwise.
	 */
	public Boolean isLoggedIn() { return sessionModel.isLoggedIn(); }

	/**
	 * Check if the user has set auto-login. If so; attempt an auto-login;
	 * otherwise attempt a manual login until the user cancels.
	 * 
	 */
	public void login() {
		if(isSetAutoLogin()) { autoLogin(); }
		else { manualLogin(); }
	}

	/**
	 * Grab the username\password from the preferences and auto-login.
	 *
	 */
	private void autoLogin() {
		login(platform.getPreferences().getUsername(),
				platform.getPreferences().getPassword(), Boolean.TRUE); 
	}

	/**
	 * Check the registry for the avatar; and if it does not yet exist; create
	 * it.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	private Avatar getAvatar(final AvatarId id) {
		if(avatarRegistry.contains(id)) { return avatarRegistry.get(id); }
		else { return AvatarFactory.create(AvatarId.PLATFORM_LOGIN); }
	}

	/**
	 * Check the registry for the window; and if it does not yet exist; create
	 * id.
	 * 
	 * @param id
	 *            The window id.
	 * @return The window.
	 */
	private Window getWindow(final WindowId id) {
		if(windowRegistry.contains(id)) { return windowRegistry.get(id); }
		else { return WindowFactory.create(id); }
	}

	/**
	 * Check to see if the auto-login flag is set.
	 * 
	 * @return Return true if auto-login is set; false otherwise.
	 */
	private Boolean isSetAutoLogin() {
		return platform.getPersistence().isSetAutoLogin();
	}

	/**
	 * Login.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password.
	 * @param savePassword
	 *            The savePassword flag.
	 */
	private void login(final String username, final String password,
			final Boolean savePassword) {
		try {
			sessionModel.login(username, password);
			if(savePassword) { platform.getPersistence().setAutoLogin(password); }
		}
		catch(final ParityException px) {
			platform.getLogger(getClass()).error("Could not login.", px);
		}
	}

	/**
	 * Display a login window; prompt the user for a login\password; then login.
	 * 
	 */
	private void manualLogin() { manualLogin(null); }

	/**
	 * Display a login window with an error message and prompt for a
	 * login\password; then login.
	 * 
	 * @param px
	 *            The previous login error.
	 */
	private void manualLogin(final ParityException px) {
		final Window window = getWindow(WindowId.PLATFORM_LOGIN);

		final LoginAvatar loginAvatar =
			(LoginAvatar) getAvatar(AvatarId.PLATFORM_LOGIN);

		if(null != px) { loginAvatar.addError(px); }
        window.open(loginAvatar);
		if(loginAvatar.isInputValid()) {
			final String username = loginAvatar.extractUsername();
			final String password = loginAvatar.extractPassword();
			final Boolean savePassword = loginAvatar.extractSavePassword();
			login(username, password, savePassword);
		}
	}
}
