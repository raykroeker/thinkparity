/*
 * Mar 3, 2005
 */
package com.thinkparity.model.parity.api.events;

/**
 * CreationListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface CreationListener {

	public void objectCreated(final CreationEvent creationEvent);
	public void objectVersionCreated(final VersionCreationEvent versionCreationEvent);
}
