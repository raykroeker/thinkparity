/*
 * Jan 12, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatarToolTipMouseTracker implements MouseInputListener {

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * The glass pane.
	 * 
	 */
	private Component glassPane;

	/**
	 * Used by the dispatchEvent api to differentiate mouse move\enter\exit
	 * events.
	 * 
	 */
	private Component previousC;

	/**
	 * The tool tip being displayed.
	 * 
	 */
	private final DocumentAvatarToolTip toolTip;

	/**
	 * Create a DocumentAvatarToolTipMouseTracker.
	 */
	public DocumentAvatarToolTipMouseTracker(final DocumentAvatarToolTip toolTip) {
		super();
		this.toolTip = toolTip;
	}

	/**
	 * Install the mouse tracker. This will use the glass pane to intercept all
	 * mouse events and dispatch them to the components on the tool tip. This is
	 * done to track when the mouse leaves the tool tip so that it can be
	 * hidden.
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
	public void mouseClicked(final MouseEvent e) { dispatchClickEvent(e); }

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
	 * If the mouse leaves the tool tip panel; we also want to fire a mouse
	 * exited event for all components on the panel before hiding the tool tip.
	 * 
	 * @see javax.swing.event.MouseInputAdapter#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) { dispatchEvent(e); }
		else {
			final Component[] components = toolTip.getComponents();
			for(Component component : components) {
				fireMouseExited(component, e);
			}
			toolTip.hideToolTip();
		}
	}

	/**
	 * @see javax.swing.event.MouseInputAdapter#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseMoved(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) { dispatchMoveEvent(e); }
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
	 * Dispatch any click events. Here we need fire a click on the underlying
	 * tool tip JPanel if the component does not consume the event.
	 * 
	 * @param e
	 *            The glass pane click event.
	 */
	private void dispatchClickEvent(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) {
			final Component c = getJPanelComponent(jpe);
			if(null != c) {
				if(!fireClick(c, e).isConsumed())
					fireClick(toolTip, e);
			}
		}
	}

	/**
	 * Dispatch the mouse event to the underlying component.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	private void dispatchEvent(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) {
			final Component c = getJPanelComponent(jpe);
			if(null != c) {
				fire(e.getID(), c, e);
				previousC = c;
			}
		}
	}

	/**
	 * Dispatch any move events. Here we need to examine the component beneath
	 * the event and if the component is the same as the last event; simply
	 * re-create the event. If it is not a MOUSE_ENTERED\MOUSE_EXITED event is
	 * dispatched to the component and previous component respectively.
	 * 
	 * @param e
	 *            The mouse move event.
	 */
	private void dispatchMoveEvent(final MouseEvent e) {
		final MouseEvent jpe = convertMouseEvent(e);
		if(jPanelContains(jpe)) {
			final Component c = getJPanelComponent(jpe);
			if(null != c) {
				if(c == previousC) {  fire(e.getID(), c, e); }
				else {
					fireMouseEntered(c, e);
					fireMouseExited(previousC, e);
				}
				previousC = c;
			}
		}
	}

	/**
	 * Fire a mouse event for the component given the original glass pane event.
	 * 
	 * 
	 * @param c
	 *            The component.
	 * @param e
	 *            The glass pane mouse event.s
	 * @return The generated mouse event.
	 */
	private MouseEvent fire(final int id, final Component c, final MouseEvent e) {
		final Point jpPoint =
			SwingUtilities.convertPoint(glassPane, e.getPoint(), toolTip);
		final Point cPoint = SwingUtilities.convertPoint(toolTip, jpPoint, c);
		final MouseEvent ce = new MouseEvent(c, id, e.getWhen(),
				e.getModifiers(), cPoint.x, cPoint.y, e.getClickCount(),
				e.isPopupTrigger());
		c.dispatchEvent(ce);
		return ce;
	}

	/**
	 * Fire a mouse click event for the component; given the original glass pane
	 * event.
	 * 
	 * @param c
	 *            The component.
	 * @param e
	 *            The glass pane mouse event.
	 * @return The generated mouse event.
	 */
	private MouseEvent fireClick(final Component c, final MouseEvent e) {
		return fire(MouseEvent.MOUSE_CLICKED, c, e);
	}

	/**
	 * Fire a mouse entered event for the component given the original glass
	 * pane event.
	 * 
	 * @param c
	 *            The component.
	 * @param e
	 *            The glass pane mouse event.
	 * @return The generated mouse event.
	 */
	private MouseEvent fireMouseEntered(final Component c, final MouseEvent e) {
		return fire(MouseEvent.MOUSE_ENTERED, c, e);
	}

	/**
	 * Fire a mouse exited event for the component given the original glass pane
	 * event.
	 * 
	 * @param c
	 *            The component.
	 * @param e
	 *            The glass pane mouse event.
	 * @return The generated mouse event.
	 */
	private MouseEvent fireMouseExited(final Component c, final MouseEvent e) {
		return fire(MouseEvent.MOUSE_EXITED, c, e);
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
