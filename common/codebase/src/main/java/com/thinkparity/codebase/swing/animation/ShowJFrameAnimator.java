/*
 * Jan 4, 2006
 */
package com.thinkparity.codebase.swing.animation;

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

	static {
		INCREMENT_STEP = 3;
	}

    private final int maximumHeight;
	private boolean isDone;

	/**
	 * Create a ShowJFrameAnimator.
	 */
	public ShowJFrameAnimator(final JFrame jFrame) {
		super(jFrame);
		this.isDone = false;
        this.maximumHeight = jFrame.getPreferredSize().height;
	}

	/**
	 * @see com.thinkparity.codebase.swing.animation.JFrameAnimator#animate()
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
	 * Increment the height com.thinkparity.browser.javax.swing.component of the dimension by a discrete value.
	 * 
	 * @param size
	 *            The size of the jFrame.
	 * @return The size of the jFrame with its height incremented.
	 */
	private Dimension incrementHeight(final Dimension size) {
		final double newHeight = size.getHeight() + INCREMENT_STEP;
		if(newHeight <= maximumHeight) {
			size.setSize(size.getWidth(), newHeight);
		}
		else { isDone = true; }
		return size;
	}
}
