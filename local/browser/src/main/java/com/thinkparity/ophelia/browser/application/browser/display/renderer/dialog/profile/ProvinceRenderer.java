/*
 * Created On:  18-Dec-06 7:07:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProvinceRenderer extends DefaultListCellRenderer {

    /**
     * Create TimeZoneRenderer.
     *
     */
    public ProvinceRenderer() {
        super();
    }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     *
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        setText((String) value);
        return super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
    }
}
