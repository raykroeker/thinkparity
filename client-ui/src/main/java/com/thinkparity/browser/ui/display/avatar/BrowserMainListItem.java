/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.component.LabelFactory;

import com.thinkparity.codebase.JVMUniqueId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class BrowserMainListItem extends AbstractJPanel implements MouseInputListener {

	/**
	 * Background color of the list item.
	 * 
	 */
	private static final Color listItemBackground;

	/**
	 * Background color of the selected list item.
	 * 
	 */
	private static final Color listItemBackgroundSelect;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		listItemBackground = new Color(237, 241, 244, 255);
		listItemBackgroundSelect = new Color(215, 231, 244, 255);
	}

	/**
	 * The main controller.
	 * 
	 * @see #getController()
	 */
	private Controller controller;

	/**
	 * The list item id.
	 * 
	 */
	private final JVMUniqueId listItemId;

	/**
	 * The list item label.
	 * 
	 */
	private JLabel listItemJLabel;

	/**
	 * Create a BrowserMainListItem.
	 * 
	 * @param listItemIcon
	 *            The list item icon.
	 * @param listItemText
	 *            The list item text.
	 * @param listItemFont
	 *            The list item font.
	 */
	BrowserMainListItem(final ImageIcon listItemIcon, final String listItemText,
			final Font listItemFont) {
		super("BrowserMainListItem", listItemBackground);
		this.listItemId = JVMUniqueId.nextId();
		setLayout(new GridBagLayout());
		addMouseMotionListener(this);
		addMouseListener(this);

		final GridBagConstraints c = new GridBagConstraints();
		final JLabel listItemIconJLabel = LabelFactory.create();
		listItemIconJLabel.setIcon(listItemIcon);
		listItemIconJLabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		c.insets = new Insets(0, 16, 0, 0);
		add(listItemIconJLabel, c.clone());

		// h:  20 px
		// x indent:  40 px
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(4, 16, 4, 0);
		listItemJLabel = LabelFactory.create(listItemText, listItemFont);
		add(listItemJLabel, c.clone());
	}

	protected BrowserMainListItem(final ImageIcon listItemIcon,
			final String listItemText) {
		this(listItemIcon, listItemText, null);
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseClicked(final MouseEvent e) {
		if(2 == e.getClickCount()) { fireDoubleClick(e); }
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseDragged(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseEntered(final MouseEvent e) { fireSelect(); }

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseExited(final MouseEvent e) {
		if(!isWithinListItem(e)) { fireUnselect(); }
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseMoved(final MouseEvent e) {}

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
	 * The user has double clicked on the main list item.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	protected abstract void fireDoubleClick(final MouseEvent e);

	/**
	 * The main list item has been selected.
	 *
	 */
	protected abstract void fireSelect();

	/**
	 * The main list item has been unselected.
	 * 
	 */
	protected abstract void fireUnselect();

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	protected Controller getController() {
		if(null == controller) { controller = Controller.getInstance(); }
		return controller;
	}

	/**
	 * Select this list item.
	 *
	 */
	protected void highlightListItem(final Boolean doHighlight) {
		if(doHighlight) { setBackground(listItemBackgroundSelect); }
		else { setBackground(listItemBackground); }
		repaint();
	}

	/**
	 * Set the font of the list item
	 * 
	 */
	protected void setListItemFont(final Font font) {
		listItemJLabel.setFont(font);
	}

	/**
	 * Obtain the list item id.
	 * 
	 * @return The list item id.
	 */
	JVMUniqueId getId() { return listItemId; }

	/**
	 * Determine whether the mouse event lies within the main list item.
	 * 
	 * @param e
	 *            The mouse event.
	 * @return True if the mouse event lies within the main list item; false
	 *         otherwise.
	 */
	private Boolean isWithinListItem(final MouseEvent e) {
		if(e.getPoint().x >= 0 && e.getPoint().x <= getBounds().width - 1)
			if(e.getPoint().y >= 0 && e.getPoint().y <= getBounds().height - 1)
				return Boolean.TRUE;
		return Boolean.FALSE;
	}
}
