/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform;

import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;

/**
 * The platform acts as the main controller for the parity browser.  It provides
 * facility for application launch and communication.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Platform {
	public void end();
	public Application getApplication(final ApplicationId applicationId);
	public Session getSession();
	public void start();
}
