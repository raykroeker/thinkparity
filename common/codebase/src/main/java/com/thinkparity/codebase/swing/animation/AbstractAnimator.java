/*
 * Jan 17, 2006
 */
package com.thinkparity.codebase.swing.animation;

import java.awt.Component;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JFrame;


import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractAnimator implements IAnimator {

	/**
	 * List of completion listeners.
	 * 
	 * @see #completionListenersLock
	 * @see #removeCompletionListener
	 * @see #fireCompletion
	 */
	private static final Collection<CompletionListener> completionListeners;

	/**
	 * Synchronization lock for the completion listeners.
	 * 
	 */
	private static final Object completionListenersLock;

	static {
		completionListeners = new Vector<CompletionListener>(7);
		completionListenersLock = new Object();
	}

	/**
	 * Glass pane of the JFrame. Used by the glass pane interceptor.
	 * 
	 * @see #installInterceptor(JFrame)
	 */
	private Component jFrameGlassPane;

	/**
	 * Create a AbstractAnimator.
	 * 
	 */
	protected AbstractAnimator() { super(); }

	/**
	 * @see com.thinkparity.codebase.swing.animation.IAnimator#addCompletionListener(com.thinkparity.codebase.swing.animation.CompletionListener)
	 * 
	 */
	public void addCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(completionListeners.contains(listener)) { return; }
			completionListeners.add(listener);
		}
	}

	/**
	 * @see com.thinkparity.codebase.swing.animation.IAnimator#removeCompletionListener(com.thinkparity.codebase.swing.animation.CompletionListener)
	 * 
	 */
	public void removeCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(!completionListeners.contains(listener)) { return; }
			completionListeners.remove(listener);
		}
	}

	/**
	 * Fire the completion event. This will notify all completion listeners that
	 * the animation has completed.
	 * 
	 */
	protected void fireComplete() {
		synchronized(completionListenersLock) {
			for(CompletionListener listener : completionListeners) {
				listener.animationComplete();
			}
		}
	}

	/**
	 * Install a glass pane interceptor that will capture all mouse input.
	 *
	 */
	protected void installInterceptor(final JFrame jFrame) {
		jFrameGlassPane = jFrame.getGlassPane();

		final GlassPaneInterceptor glassPaneInterceptor =
			new GlassPaneInterceptor();
		jFrame.setGlassPane(glassPaneInterceptor);
		glassPaneInterceptor.setVisible(true);
	}

	/**
	 * Uninstall the glass pane interceptor.
	 *
	 */
	protected void uninstallInterceptor(final JFrame jFrame) {
		jFrame.setGlassPane(jFrameGlassPane);
	}
}
