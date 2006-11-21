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
public class SlideJFrameAnimator extends AbstractJFrameAnimator {

	/**
	 * Size of the step to take when animating.
	 * 
	 */
	private static final int INCREMENT_STEP;

	static {
		INCREMENT_STEP = 3;
	}

    private final int maximumHeight;

	/**
	 * Completion flag.
	 * 
	 */
	private boolean isDone;

	/**
	 * Create a ShowJFrameAnimator.
	 * 
	 * @param jFrame
	 *            The JFrame to animate.
	 */
	public SlideJFrameAnimator(final JFrame jFrame) {
		super(jFrame);
		this.isDone = false;
        this.maximumHeight = jFrame.getPreferredSize().height;
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.animation.JFrameAnimator#animate()
	 * 
	 */
	public void animate() {
		if(isDone) { stop(); }
		else {
			jFrame.setSize(incrementWidth(jFrame.getSize()));
			jFrame.dispatchEvent(
					new ComponentEvent(jFrame, ComponentEvent.COMPONENT_RESIZED));
			jFrame.repaint();
		}
	}

	/**
	 * Increment the width com.thinkparity.browser.javax.swing.component of the dimension by a discrete value.
	 * 
	 * @param size
	 *            The size of the jFrame.
	 * @return The size of the jFrame with its width incremented.
	 */
	private Dimension incrementWidth(final Dimension size) {
		final double newWidth = size.width + INCREMENT_STEP;
		if(newWidth <= maximumHeight) {
			size.setSize(newWidth, size.height);
		}
		else { isDone = true; }
		return size;
	}
}
