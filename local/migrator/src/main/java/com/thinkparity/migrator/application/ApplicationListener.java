/*
 * Created On: Sun Jun 25 2006 10:54 PDT
 * $Id$
 */
package com.thinkparity.migrator.application;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
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
