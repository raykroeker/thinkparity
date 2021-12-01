/*
 * Jan 16, 2006
 */
package com.thinkparity.codebase.swing.animation;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public interface Animator {
	public void addCompletionListener(final CompletionListener listener);
	public void animate();
	public boolean isRunning();
	public void removeCompletionListener(final CompletionListener listener);
	public void start();
	public void stop();
}
