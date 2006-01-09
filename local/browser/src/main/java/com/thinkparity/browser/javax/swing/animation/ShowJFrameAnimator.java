/*
 * Jan 4, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/**
 * Create a simple animation that will increase the size of the browser to
 * pre-set maximum; then stop.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ShowJFrameAnimator extends AbstractJFrameAnimator {

	private static final int INCREMENT_STEP;

	private static final int MAX_BROWSER_HEIGHT;

	static {
		MAX_BROWSER_HEIGHT = 300;
		INCREMENT_STEP = 3;
	}

	private boolean isDone;

	/**
	 * Create a ShowJFrameAnimator.
	 */
	public ShowJFrameAnimator(final JFrame jFrame) {
		super(jFrame);
		this.isDone = false;
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.animation.JFrameAnimator#animate()
	 * 
	 */
	public void animate() {
		if(isDone) { stop(); }
		else {
			jFrame.setSize(incrementHeight(jFrame.getSize()));
			jFrame.dispatchEvent(
					new ComponentEvent(jFrame, ComponentEvent.COMPONENT_RESIZED));
			jFrame.repaint();
		}
	}

	/**
	 * Increment the height component of the dimension by a discrete value.
	 * 
	 * @param size
	 *            The size of the jFrame.
	 * @return The size of the jFrame with its height incremented.
	 */
	private Dimension incrementHeight(final Dimension size) {
		final double newHeight = size.getHeight() + INCREMENT_STEP;
		if(newHeight <= MAX_BROWSER_HEIGHT) {
			size.setSize(size.getWidth(), newHeight);
		}
		else { isDone = true; }
		return size;
	}
}
