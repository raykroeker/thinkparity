/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractJFrameAnimator implements JFrameAnimator {

	/**
	 * The glass pane interceptor is added\removed to the jFrame during the
	 * subsequent start\stop calls. This is so that during the animation no
	 * mouse events can be captured by the underlying components.
	 * 
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class GlassPaneInterceptor extends JComponent implements
			MouseMotionListener {

		/**
		 * @see java.io.Serializable
		 * 
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Hints used to render the transulcent background.
		 * 
		 */
		private final RenderingHints renderingHints;

		/**
		 * Create a GlassPaneInterceptor.
		 * 
		 */
		private GlassPaneInterceptor() {
			super();
			addMouseMotionListener(this);
	        
			this.renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        this.renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}

		/**
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		public void mouseMoved(final MouseEvent e) {}

		/**
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 * 
		 */
		public void paintComponent(Graphics g) {
			final Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(renderingHints);
            
            g2.setColor(new Color(255, 255, 255, (int) (255 * 0.70f)));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
		}
	}

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
	private Component jFrameOriginalGlassPane;

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

	/**
	 * @see com.thinkparity.browser.javax.swing.animation.Animator#isRunning()
	 * 
	 */
	public boolean isRunning() { return timer.isRunning(); }

	/**
	 * Start the jFrame animation. This will check to see if any animations are
	 * currently running on the jFrame; and if they are; it will stop them. It
	 * then installs a glass pane to intercept the mouse input during the
	 * animation and finally start the swing timer.
	 * 
	 * @see com.thinkparity.browser.javax.swing.animation.JFrameAnimator#start()
	 */
	public void start() {
		if(null != getCurrentAnimator()) { getCurrentAnimator().stop(); }
		installInterceptor();
		setCurrentAnimator();
		startTimer();
	}

	/**
	 * Stop the jFrame animation. This will remove the intercept glass pane; and
	 * unset the current animation.
	 * 
	 * @see com.thinkparity.browser.javax.swing.animation.JFrameAnimator#stop()
	 */
	public void stop() {
		stopTimer();
		uninstallInterceptor();
		unsetCurrentAnimator();
	}

	/**
	 * Obtain a current animator.
	 * 
	 * @return The current animator; or null if no animator is running.
	 */
	private JFrameAnimator getCurrentAnimator() {
		return (JFrameAnimator) jFrame.getRootPane().getClientProperty(JFRAME_ANIMATOR_KEY);
	}

	/**
	 * Install a glass pane interceptor that will capture all mouse input.
	 *
	 */
	private void installInterceptor() {
		jFrameOriginalGlassPane = jFrame.getGlassPane();

		final GlassPaneInterceptor glassPaneInterceptor =
			new GlassPaneInterceptor();
		jFrame.setGlassPane(glassPaneInterceptor);
		glassPaneInterceptor.setVisible(true);
		
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

	private void uninstallInterceptor() {
		jFrame.setGlassPane(jFrameOriginalGlassPane);
	}

	/**
	 * Unset the current animator.
	 *
	 */
	private void unsetCurrentAnimator() {
		jFrame.getRootPane().putClientProperty(JFRAME_ANIMATOR_KEY, null);
	}

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
