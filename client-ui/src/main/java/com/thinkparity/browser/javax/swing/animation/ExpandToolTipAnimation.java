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

import com.thinkparity.browser.log4j.BrowserLoggerFactory;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ExpandToolTipAnimation extends AbstractJPanelAnimation {

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
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Animation flag indicating whether the animation is complete.
	 * 
	 */
	private boolean isDone;

	private final int maxSizeHeight;

	private final int minLocationY;

	private final Timer timer;

	private final JPanel toolTip;

	/**
	 * Create a ExpandToolTipAnimation.
	 * @param toolTip
	 * @param maxSizeHeight
	 */
	public ExpandToolTipAnimation(final JPanel toolTip,
			final int maxSizeHeight, final int minLocationY) {
		super();
		this.isDone = false;
		this.maxSizeHeight = maxSizeHeight;
		this.minLocationY = minLocationY;
		this.toolTip = toolTip;
		this.timer = new Timer(TIMER_INTERVAL, new ActionListener() {
			public void actionPerformed(final ActionEvent e) { animate(); }
		});
	}

	public void animate() {
		if(isDone) {
			stop();
			fireComplete();
		}
		else {
			toolTip.setSize(incrementSize(toolTip.getSize()));
			toolTip.setLocation(decrementLocation(toolTip.getLocation()));
			toolTip.dispatchEvent(new ComponentEvent(toolTip, ComponentEvent.COMPONENT_RESIZED));
			toolTip.doLayout();
		}
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.animation.IAnimator#isRunning()
	 * 
	 */
	public boolean isRunning() { return timer.isRunning(); }

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

	private Point decrementLocation(final Point location) {
		final int newY = location.y - LOCATION_Y_STEP;
		if(newY > minLocationY) { location.y = newY; }
		else if(location.y > minLocationY) { location.y = minLocationY; }
		return location;
	}

	private Dimension incrementSize(final Dimension size) {
		final int newHeight = size.height + SIZE_HEIGHT_STEP;
		if(newHeight <= maxSizeHeight) { size.height = newHeight; }
		else {
			if(size.height < maxSizeHeight) { size.height = maxSizeHeight; }
			else { isDone = true; }
		}
		return size;
	}
}
