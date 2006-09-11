/*
 * Created On: Jun 2, 2006 6:03:46 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Selection list cell renderer for profiles.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ProfileListCellRenderer extends DefaultListCellRenderer {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        final JLabel jLabel = (JLabel) super.getListCellRendererComponent(list, value,
                index, isSelected, cellHasFocus);
        final Profile profile = (Profile) value;
        jLabel.setText(new StringBuffer(profile.getLastModified()).append(" - ").append(profile.getName()).toString());
        return jLabel;
    }
}
