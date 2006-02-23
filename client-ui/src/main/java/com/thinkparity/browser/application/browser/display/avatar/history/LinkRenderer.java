/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LinkRenderer implements TableCellRenderer {

	private static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The browser.
	 * 
	 */
	private final Browser browser;

	/**
	 * The rendering component.
	 * 
	 */
	private final JLabel jLabelLink;

	/**
	 * The original link cursor.
	 * 
	 */
	private final Cursor originalCursor;

	/**
	 * Create a LinkRenderer.
	 */
	public LinkRenderer(final Browser browser) {
		super();
		this.browser = browser;
		this.jLabelLink = LabelFactory.createLink("", UIConstants.DefaultFont);
		this.originalCursor = jLabelLink.getCursor();
	}

	/**
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, boolean, int, int)
	 * 
	 */
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column) {
		if(hasFocus) { applyHandCursor(); }
		else { applyOriginalCursor(); }
		jLabelLink.setText((String) value);
		return jLabelLink;
	}

	private void applyHandCursor() { jLabelLink.setCursor(handCursor); }

	private void applyOriginalCursor() { jLabelLink.setCursor(originalCursor); }
}
