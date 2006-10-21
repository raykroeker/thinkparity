/*
 * Jan 20, 2006
 */
package com.thinkparity.codebase.ui.avatar;

import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.platform.Platform;
import com.thinkparity.codebase.ui.provider.Provider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Avatar<T extends Platform, U extends Application<T>, V extends Provider> {

	/**
     * Obtain the avatar's application.
     * 
     * @return An <code>Application</code>.
     */
	public U getApplication();

    /**
     * Obtain the avatar's platform.
     * 
     * @return A <code>Platform</code>.
     */
    public T getPlatform();

    /**
	 * Obtain the avatar id.
	 * 
	 * @return An <code>AvatarId</code>
	 */
	public AvatarId getId();

	/**
	 * Obtain the avatar's input.
	 * 
	 * @return The input.
	 */
	public Object getInput();

	/**
	 * Obtain the content provider.
	 * 
	 * @return The content provider.
	 */
	public V getProvider();
    
	/**
     * Reload the avatar. This event is called when either the content provider
     * or the input has changed; or as a manual reload of the avatar.
     * 
     */
	public void reload();

	/**
	 * Set the avatar's input.
	 * 
	 * @param input
	 *            The avatar input.
	 */
	public void setInput(final Object input);

	/**
	 * Set the content provider.
	 * 
	 * @param contentProvider
	 *            The content provider.
	 */
	public void setProvider(final V provider);
}
