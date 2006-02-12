/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform.application;

import com.thinkparity.browser.platform.Saveable;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Application extends Saveable {
	public void end();
	public ApplicationId getId();
	public void hibernate();
	public void start();
	public void launch();
}
