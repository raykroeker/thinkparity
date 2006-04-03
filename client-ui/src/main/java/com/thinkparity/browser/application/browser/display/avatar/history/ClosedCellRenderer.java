/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.JList;

import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ClosedCellRenderer extends HistoryItemCellRenderer {

	/**
	 * The closed background image.
	 * 
	 */
	private static final BufferedImage BG;

	/**
	 * The closed selected background image.
	 * 
	 */
	private static final BufferedImage BG_SEL;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		BG = ImageIOUtil.read("DocumentHistoryCellClosed.png");
		BG_SEL = ImageIOUtil.read("DocumentHistoryCellClosedSelected.png");
	}

	/**
	 * Create a CellRenderer.
	 * 
	 */
	public ClosedCellRenderer() { super(); }

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		setBackground(isSelected ? BG_SEL : BG);
		setText((DisplayHistoryItem) value);
		return this;
	}
}
