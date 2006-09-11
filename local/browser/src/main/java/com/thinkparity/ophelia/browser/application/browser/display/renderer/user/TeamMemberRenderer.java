/*
 * Created On: Aug 11, 2006 4:14:59 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.user;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;


import com.thinkparity.ophelia.browser.util.ModelUtil;
import com.thinkparity.ophelia.model.user.TeamMember;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TeamMemberRenderer extends DefaultListCellRenderer {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create TeamMemberRenderer. */
    public TeamMemberRenderer() { super(); }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        final JLabel jLabel = (JLabel) super.getListCellRendererComponent(list, value,
                index, isSelected, cellHasFocus);
        jLabel.setText(ModelUtil.getName((TeamMember) value));
        return jLabel;
    }
}
