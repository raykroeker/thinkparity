/**
 * 
 */
package com.thinkparity.cordelia.ui.application.admin.avatar.user;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond
 *
 */
public final class UserRenderer extends DefaultListCellRenderer {

    /**
     * Create UserRenderer.
     *  
     */
    public UserRenderer() {
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
        setText(((User) value).getName());
        return this;
    }
}
