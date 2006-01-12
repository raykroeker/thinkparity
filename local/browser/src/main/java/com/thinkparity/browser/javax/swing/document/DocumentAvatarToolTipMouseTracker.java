/*
 * Jan 12, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatarToolTipMouseTracker extends MouseInputAdapter {

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	private Component glassPane;

	private final DocumentAvatarToolTip toolTip;

	/**
	 * Create a DocumentAvatarToolTipMouseTracker.
	 */
	public DocumentAvatarToolTipMouseTracker(final DocumentAvatarToolTip toolTip) {
		super();
		this.toolTip = toolTip;
	}

	/**
	 * Install the mouse tracker.
	 *
	 */
	public void install() {
		glassPane = toolTip.getRootPane().getGlassPane();
		glassPane.addMouseListener(this);
		glassPane.addMouseMotionListener(this);
		glassPane.setVisible(true);
	}

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseClicked(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseDragged(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseEntered(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseEntered(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseExited(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseExited(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseMoved(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) { dispatchEvent(e); }
		else { toolTip.hideToolTip(); }
	}

	/**
	 * @see javax.swing.event.MouseInputAdapter#mousePressed(java.awt.event.MouseEvent)
	 * 
	 */
	public void mousePressed(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseReleased(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseReleased(final MouseEvent e) { dispatchEvent(e); }

	/**
	 * Uninstall the mouse tracker.
	 *
	 */
	public void uninstall() {
		if(null != glassPane) {
			glassPane.removeMouseListener(this);
			glassPane.removeMouseMotionListener(this);
			glassPane.setVisible(false);
		}
	}

	/**
	 * Convert the mouse event from a glassPane mouse event to a jPanel mouse
	 * event.
	 * 
	 * @param e
	 *            The glassPane mouse event.
	 * @return The jPanel mouse event.
	 * 
	 * @see #jPanel
	 * @see #glassPane
	 */
	private MouseEvent convertMouseEvent(final MouseEvent e) {
		return SwingUtilities.convertMouseEvent(glassPane, e, toolTip); 
	}

	/**
	 * Dispatch the mouse event to the underlying component.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	private void dispatchEvent(final MouseEvent e) {
		logger.debug(e);
		final MouseEvent jpe = convertMouseEvent(e);
		logger.debug(jpe);
		if(jPanelContains(jpe)) {
			final Component c = getJPanelComponent(jpe);
			if(null != c) {
				final Point jpePoint = jpe.getPoint();
				c.dispatchEvent(new MouseEvent(c, e.getID(), e.getWhen(),
						e.getModifiers(), jpePoint.x, jpePoint.y,
						e.getClickCount(), e.isPopupTrigger()));
			}
		}
	}

	/**
	 * Obtain the component that exists beneath the mouse event from the jPanel.
	 * 
	 * @param jpe
	 *            The jPanel mouse event.
	 * @return The underlying component at the location of the event; or null if
	 *         no component exists.
	 * 
	 * @see #convertMouseEvent(MouseEvent)
	 */
	private Component getJPanelComponent(final MouseEvent jpe) {
		return toolTip.getComponentAt(jpe.getPoint());
	}

	/**
	 * Determine if the mouse event exists within the jPanel.
	 * 
	 * @param jpe
	 *            The jPanel mouse event.
	 * @return True if the mouse event occurs within the jPanel; false
	 *         otherwise.
	 * 
	 * @see #convertMouseEvent(MouseEvent)
	 */
	private boolean jPanelContains(final MouseEvent jpe) {
		return toolTip.contains(jpe.getPoint());
	}
}
