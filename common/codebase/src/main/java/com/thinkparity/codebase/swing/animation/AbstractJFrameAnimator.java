/*
 * Jan 5, 2006
 */
package com.thinkparity.codebase.swing.animation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class AbstractJFrameAnimator extends AbstractAnimator {

	/**
	 * List of registered completion listeners.
	 * 
	 */
	private static final Collection<CompletionListener> completionListeners;

	/**
	 * Synchronization lock for the completion listeners.
	 * 
	 */	
	private static final Object completionListenersLock;

	private static final String JFRAME_ANIMATOR_KEY;

	private static final int TIMER_INTERVAL;

	static {
		TIMER_INTERVAL = 10;
		JFRAME_ANIMATOR_KEY = "JFrameAnimator";
		completionListeners = new Vector<CompletionListener>(7);
		completionListenersLock = new Object();
	}

	/**
	 * Handle to the jFrame we want to animate.
	 * 
	 */
	protected final JFrame jFrame;

	/**
	 * Handle to the swing timer user to control the animation.
	 * 
	 */
	protected final Timer timer;

	/**
	 * The glass pane of the jframe.
	 */
	Component jFrameOriginalGlassPane;

	/**
	 * Create a AbstractJFrameAnimator.
	 * 
	 */
	protected AbstractJFrameAnimator(final JFrame jFrame) {
		super();
		this.jFrame = jFrame;
		this.timer = new Timer(TIMER_INTERVAL, new ActionListener() {
			public void actionPerformed(final ActionEvent e) { animate(); }
		});
	}

	public void addCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(completionListeners.contains(listener)) { return; }
			completionListeners.add(listener);
		}
	}

	/**
	 * @see com.thinkparity.codebase.swing.animation.IAnimator#isRunning()
	 * 
	 */
	public boolean isRunning() { return timer.isRunning(); }

	public void removeCompletionListener(final CompletionListener listener) {
		Assert.assertNotNull("", listener);
		synchronized(completionListenersLock) {
			if(!completionListeners.contains(listener)) { return; }
			completionListeners.remove(listener);
		}
	}

	/**
	 * Start the jFrame animation. This will check to see if any animations are
	 * currently running on the jFrame; and if they are; it will stop them. It
	 * then installs a glass pane to intercept the mouse input during the
	 * animation and finally start the swing timer.
	 * 
	 * @see com.thinkparity.codebase.swing.animation.JFrameAnimator#start()
	 */
	public void start() {
		if(null != getCurrentAnimator()) { getCurrentAnimator().stop(); }
		installInterceptor(jFrame);
		setCurrentAnimator();
		startTimer();
	}

	/**
	 * Stop the jFrame animation. This will remove the intercept glass pane; and
	 * unset the current animation.
	 * 
	 * @see com.thinkparity.codebase.swing.animation.JFrameAnimator#stop()
	 */
	public void stop() {
		stopTimer();
		uninstallInterceptor(jFrame);
		unsetCurrentAnimator();
	}

	protected void fireComplete() {
		synchronized(completionListenersLock) {
			for(CompletionListener listener : completionListeners) {
				listener.animationComplete();
			}
		}
	}

	/**
	 * Obtain a current animator.
	 * 
	 * @return The current animator; or null if no animator is running.
	 */
	private IAnimator getCurrentAnimator() {
		return (IAnimator) jFrame.getRootPane().getClientProperty(JFRAME_ANIMATOR_KEY);
	}

	/**
	 * Set the current animator.
	 *
	 */
	private void setCurrentAnimator() {
		jFrame.getRootPane().putClientProperty(JFRAME_ANIMATOR_KEY, this);
	}

	/**
	 * Start the swing timer.
	 *
	 */
	private void startTimer() { timer.start(); }

	/**
	 * Stop the swing timer.
	 *
	 */
	private void stopTimer() { timer.stop(); }

	/**
	 * Unset the current animator.
	 *
	 */
	private void unsetCurrentAnimator() {
		jFrame.getRootPane().putClientProperty(JFRAME_ANIMATOR_KEY, null);
	}
}
