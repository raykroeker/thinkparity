/*
 * Mar 20, 2006
 */
package com.thinkparity.codebase.ui.application;

import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.ui.platform.Platform;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ApplicationListener<T extends Platform> extends EventListener {

	/**
	 * Notify the implementer that the application has ended.
	 *
	 * @param application
	 *            The application.
	 */
	void notifyEnd(final Application<T> application);

	/**
	 * Notify the implementer that the application has hibernated.
	 * 
	 * @param application
	 *            The application.
	 */
	void notifyHibernate(final Application<T> application);

	/**
	 * Notify the implementer that the application has been restored.
	 * 
	 * @param application
	 *            The application.
	 */
	void notifyRestore(final Application<T> application);

	/**
	 * Notify the implementer that the application has started.
	 * 
	 * @param application
	 *            The application.
	 */
	void notifyStart(final Application<T> application);
}
