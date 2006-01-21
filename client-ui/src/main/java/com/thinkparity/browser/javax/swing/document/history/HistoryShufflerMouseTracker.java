/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.document.history;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.apache.log4j.Logger;

import com.thinkparity.browser.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryShufflerMouseTracker implements MouseInputListener {

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Handle to the history shuffler.
	 * 
	 */
	private final HistoryShuffler historyShuffler;

	/**
	 * Used to track mouse entry\exit on the history shuffler.
	 * 
	 */
	private Component previousA;

	/**
	 * Create a HistoryShufflerMouseTracker.
	 * 
	 * @param historyShuffler
	 *            The history shuffler JPanel.
	 */
	public HistoryShufflerMouseTracker(final HistoryShuffler historyShuffler) {
		super();
		this.historyShuffler = historyShuffler;
	}

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseClicked(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseDragged(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseEntered(final MouseEvent e) { dispatchMoveEvent(e); }

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseExited(final MouseEvent e) {
		final Component[] components = historyShuffler.getComponents();
		for(Component c : components) { fireMouseExited(c, e); }
		previousA = null;
	}

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseMoved(final MouseEvent e) { dispatchMoveEvent(e); }

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 * 
	 */
	public void mousePressed(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseReleased(final MouseEvent e) {}

	/**
	 * Dispatch the mouse event to underlying avatars on the history shuffler.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	private void dispatchEvent(final MouseEvent e) {
		final HistoryAvatar a = getAvatar(e);
		if(null != a) {
			fire(e.getID(), a, e);
			previousA = a;
		}
	}

	/**
	 * Dispatch the mouse move event to the underlying avatars on the history
	 * shuffler. Will automatically generate entry\exit events for components.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	private void dispatchMoveEvent(final MouseEvent e) {
		final HistoryAvatar a = getAvatar(e);
		if(null != a) {
			if(a == previousA) { fire(e.getID(), a, e); }
			else {
				fireMouseEntered(a, e);
				if(null != previousA) { fireMouseExited(previousA, e); }
			}
			previousA = a;
		}
	}

	/**
	 * Fire a mouse event for the com.thinkparity.browser.javax.swing.component given the original glass pane event.
	 * 
	 * 
	 * @param c
	 *            The com.thinkparity.browser.javax.swing.component.
	 * @param e
	 *            The glass pane mouse event.s
	 * @return The generated mouse event.
	 */
	private MouseEvent fire(final int id, final Component c, final MouseEvent e) {
		final Point cPoint = SwingUtilities.convertPoint(historyShuffler, e.getPoint(), c);
		final MouseEvent ce = new MouseEvent(c, id, e.getWhen(),
				e.getModifiers(), cPoint.x, cPoint.y, e.getClickCount(),
				e.isPopupTrigger());
		c.dispatchEvent(ce);
		return ce;
	}

	/**
	 * Fire a mouse entered event for the com.thinkparity.browser.javax.swing.component given the original glass
	 * pane event.
	 * 
	 * @param c
	 *            The com.thinkparity.browser.javax.swing.component.
	 * @param e
	 *            The glass pane mouse event.
	 * @return The generated mouse event.
	 */
	private MouseEvent fireMouseEntered(final Component c, final MouseEvent e) {
		return fire(MouseEvent.MOUSE_ENTERED, c, e);
	}

	/**
	 * Fire a mouse exited event for the com.thinkparity.browser.javax.swing.component given the original glass pane
	 * event.
	 * 
	 * @param c
	 *            The com.thinkparity.browser.javax.swing.component.
	 * @param e
	 *            The glass pane mouse event.
	 * @return The generated mouse event.
	 */
	private MouseEvent fireMouseExited(final Component c, final MouseEvent e) {
		return fire(MouseEvent.MOUSE_EXITED, c, e);
	}

	/**
	 * Obtain the underlying avatar on the shuffler.
	 * 
	 * @param e
	 *            The mouse event.
	 * @return The history avatar.
	 */
	private HistoryAvatar getAvatar(final MouseEvent e) {
		final Component c = historyShuffler.getComponentAt(e.getPoint());
		if(HistoryAvatar.class.isAssignableFrom(c.getClass()))
			return (HistoryAvatar) c;
		return null;
	}
}
