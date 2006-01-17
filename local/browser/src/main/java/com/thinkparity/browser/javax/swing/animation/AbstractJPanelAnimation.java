/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractJPanelAnimation implements JPanelAnimator {

	private static final Collection<CompletionListener> completionListeners;
	private static final Object completionListenersLock;

	static {
		completionListeners = new Vector<CompletionListener>(7);
		completionListenersLock = new Object();
	}

	/**
	 * Create a AbstractJPanelAnimation.
	 */
	protected AbstractJPanelAnimation() { super(); }

	public void addCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(completionListeners.contains(listener)) { return; }
			completionListeners.add(listener);
		}
	}

	public void removeCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(!completionListeners.contains(listener)) { return; }
			completionListeners.remove(listener);
		}
	}

	protected void fireComplete() {
		synchronized(completionListenersLock) {
			for(CompletionListener listener : completionListeners) {
				listener.animationComplete();
			}
		}
	}
}
