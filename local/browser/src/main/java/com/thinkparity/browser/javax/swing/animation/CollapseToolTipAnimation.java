/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.animation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollapseToolTipAnimation extends AbstractJPanelAnimation {

	private static final int LOCATION_Y_STEP;

	private static final int SIZE_HEIGHT_STEP;

	private static final int TIMER_INTERVAL;

	static {
		LOCATION_Y_STEP = 3;
		SIZE_HEIGHT_STEP = 3;
		TIMER_INTERVAL = 10;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Animation flag indicating whether the animation is complete.
	 * 
	 */
	private boolean isDone;

	private final int originalLocationY;

	private final int originalSizeHeight;

	private final Timer timer;

	private final JPanel toolTip;

	/**
	 * Create a CollapseToolTipAnimation.
	 * 
	 * @param toolTip
	 *            The tool tip.
	 * @param originalSize
	 *            The original size.
	 * @param originalLocation
	 *            The original location.
	 */
	public CollapseToolTipAnimation(final JPanel toolTip,
			final Dimension originalSize, final Point originalLocation) {
		super();
		this.isDone = false;
		this.originalSizeHeight = originalSize.height;
		this.originalLocationY = originalLocation.y;
		this.toolTip = toolTip;
		this.timer = new Timer(TIMER_INTERVAL, new ActionListener() {
			public void actionPerformed(final ActionEvent e) { animate(); }
		});
	}

	public void animate() {
		if(isDone) { stop(); }
		else {
			toolTip.setSize(decrementSize(toolTip.getSize()));
			toolTip.setLocation(incrementLocation(toolTip.getLocation()));
			toolTip.dispatchEvent(new ComponentEvent(toolTip, ComponentEvent.COMPONENT_RESIZED));
			toolTip.doLayout();
		}
	}

	public void start() {
		logger.debug(toolTip.getSize());
		logger.debug(toolTip.getLocation());
		timer.start();
	}

	public void stop() {
		logger.debug(toolTip.getSize());
		logger.debug(toolTip.getLocation());
		timer.stop();
	}

	private Dimension decrementSize(final Dimension size) {
		final int newHeight = size.height - SIZE_HEIGHT_STEP;
		if(newHeight > originalSizeHeight) { size.height = newHeight; }
		else {
			if(size.height > originalSizeHeight) { size.height = originalSizeHeight; }
			else { isDone = true; }
		}
		return size;
	}

	private Point incrementLocation(final Point location) {
		final int newY = location.y + LOCATION_Y_STEP;
		if(newY < originalLocationY) { location.y = newY; }
		else if(location.y < originalLocationY) { location.y = originalLocationY; }
		return location;
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.animation.IAnimator#isRunning()
	 * 
	 */
	public boolean isRunning() { return timer.isRunning(); }
}
