/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IAnimator {
	public void addCompletionListener(final CompletionListener listener);
	public void animate();
	public boolean isRunning();
	public void removeCompletionListener(final CompletionListener listener);
	public void start();
	public void stop();
}
