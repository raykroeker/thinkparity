/*
 * Jan 4, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HideJFrameAnimator extends AbstractJFrameAnimator {

	private static final int DECREMENT_STEP;

	private static final int MIN_BROWSER_HEIGHT;

	static {
		MIN_BROWSER_HEIGHT = 28;
		DECREMENT_STEP = 3;
	}

	private boolean isDone;

	/**
	 * Create a HideJFrameAnimator.
	 * 
	 */
	public HideJFrameAnimator(final JFrame jFrame) {
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
			jFrame.setSize(decrementHeight(jFrame.getSize()));
			jFrame.dispatchEvent(
					new ComponentEvent(jFrame, ComponentEvent.COMPONENT_RESIZED));
			jFrame.repaint();
		}
	}

	/**
	 * Decrement the height of the jFrame.
	 * 
	 * @param size
	 *            The size of the jFrame.
	 * @return The jFrame's size with the height com.thinkparity.browser.javax.swing.component decremented.
	 */
	private Dimension decrementHeight(final Dimension size) {
		final double newHeight = size.getHeight() - DECREMENT_STEP;
		if(newHeight >= MIN_BROWSER_HEIGHT) {
			size.setSize(size.getWidth(), newHeight);
		}
		else { isDone = true; }
		return size;
	}
}