/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DefaultRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1;

	/**
	 * Create a DefaultRenderer.
	 */
	public DefaultRenderer() {
		super();
	}

	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
