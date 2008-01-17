/**
 * Created On: 28-Dec-07 1:59:11 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.thinkparity.codebase.model.user.UserVCard;

import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class IndustryRenderer extends DefaultListCellRenderer {

    /** Localization. */
    private final Localization localization;

    /**
     * Create IndustryRenderer.
     * 
     * @param localization
     *            A <code>Localization</code>.
     */
    public IndustryRenderer(final Localization localization) {
        super();
        this.localization = localization;
    }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     *
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        if (null == value) {
            // value can be null if the selection is -1
            setText(" ");
        } else {
            setText(localization.getString(((UserVCard.Industry) value).name()));
        }
        return this;
    }
}
