/*
 * Mar 20, 2006
 */
package com.thinkparity.browser.platform.application;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ApplicationListener {

	/**
	 * Notify the implementer that the application has ended.
	 *
	 * @param application
	 *            The application.
	 */
	public void notifyEnd(final Application application);

	/**
	 * Notify the implementer that the application has hibernated.
	 * 
	 * @param application
	 *            The application.
	 */
	public void notifyHibernate(final Application application);

	/**
	 * Notify the implementer that the application has been restored.
	 * 
	 * @param application
	 *            The application.
	 */
	public void notifyRestore(final Application application);

	/**
	 * Notify the implementer that the application has started.
	 * 
	 * @param application
	 *            The application.
	 */
	public void notifyStart(final Application application);
}
