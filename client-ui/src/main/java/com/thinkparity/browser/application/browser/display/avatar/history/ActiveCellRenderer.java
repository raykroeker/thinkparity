/*
 * Mar 27, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.JList;

import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.model.parity.model.document.history.HistoryItem;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ActiveCellRenderer extends HistoryItemCellRenderer {

	/**
	 * The active history item bg image.
	 * 
	 */
	private static final BufferedImage BG;

	/**
	 * The active selected history item bg image.
	 * 
	 */
	private static final BufferedImage BG_SEL;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		BG = ImageIOUtil.read("DocumentCell.png");
		BG_SEL = ImageIOUtil.read("DocumentCellSelected.png");
	}

	/**
	 * Create a ActiveCellRenderer.
	 * 
	 */
	public ActiveCellRenderer() { super(); }

	/**
     * @see com.thinkparity.browser.application.browser.display.avatar.history.HistoryItemCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     * 
     */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		setBackground(isSelected ? BG_SEL : BG);
		setText((HistoryItem) value);
		return this;
	}
}
