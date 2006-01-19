/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.action.session;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.thinkparity.browser.javax.swing.action.AbstractAction;
import com.thinkparity.browser.javax.swing.action.Data;
import com.thinkparity.browser.javax.swing.animation.AbstractJPanelAnimation;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ShowLogin extends AbstractAction {

	public enum DataKey { LOGIN_JPANEL, MAX_SIZE_HEIGHT, MIN_LOCATION_Y }

	private class ShowAnimation extends AbstractJPanelAnimation {

		private static final int LOCATION_Y_STEP = 3;
		private static final int SIZE_HEIGHT_STEP = 3;
		private static final int TIMER_INTERVAL = 10;
		private boolean isDone;
		private final JPanel jPanel;
		private final int maxSizeHeight;
		private final int minLocationY;
		private final Timer timer;

		private ShowAnimation(final JPanel jPanel, final int maxSizeHeight,
				final int minLocationY) {
			super();
			this.isDone = false;
			this.maxSizeHeight = maxSizeHeight;
			this.minLocationY = minLocationY;
			this.jPanel = jPanel;
			this.timer = new Timer(TIMER_INTERVAL, new ActionListener() {
				public void actionPerformed(final ActionEvent e) { animate(); }
			});
		}

		/**
		 * @see com.thinkparity.browser.javax.swing.animation.IAnimator#animate()
		 */
		public void animate() {
			if(isDone) {
				stop();
				fireComplete();
			}
			else {
				jPanel.setSize(incrementSize(jPanel.getSize()));
				jPanel.setLocation(decrementLocation(jPanel.getLocation()));
				jPanel.dispatchEvent(new ComponentEvent(jPanel, ComponentEvent.COMPONENT_RESIZED));
				jPanel.doLayout();
			}
		}

		/**
		 * @see com.thinkparity.browser.javax.swing.animation.IAnimator#isRunning()
		 * 
		 */
		public boolean isRunning() { return timer.isRunning(); }

		public void start() {
			timer.start();
		}

		public void stop() {
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

	private static final Icon icon;

	private static final String name;

	static {
		name = "Show Login";
		icon = null;
	}

	/**
	 * Create a ShowLogin.
	 * 
	 */
	public ShowLogin() { super(name, icon); }

	/**
	 * @see com.thinkparity.browser.javax.swing.action.AbstractAction#invoke(com.thinkparity.browser.javax.swing.action.Data)
	 * 
	 */
	public void invoke(Data data) {


	
	
	
	
	
	}
}
