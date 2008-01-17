/*
 * Created On:  3-Oct-07 4:44:03 PM
 */
package com.thinkparity.ophelia.browser.util.swing;

import java.awt.Component;
import java.text.MessageFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo.CardName;

import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CardNameCellRenderer extends DefaultListCellRenderer {

    /** Localization. */
    private final Localization localization;

    /** A localization context. */
    private final String context;

    /**
     * Create CreditCardNameCellRenderer.
     *
     */
    public CardNameCellRenderer(final String context, final Localization localization) {
        super();
        this.context = context;
        this.localization = localization;
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
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
            setText(localization.getString(MessageFormat.format("{0}.{1}", context,
                    ((CardName) value).name())));
        }
        return this;
    }
}
