/*
 * Created On:  18-Dec-06 7:07:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile;

import java.awt.Component;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LocaleRenderer extends DefaultListCellRenderer {

    /** A <code>locale</code>. */
    private final Locale displayLocale;

    /**
     * Create LocaleRenderer.
     * 
     * @param displayLocale
     *            A display <code>Locale</code>.
     */
    public LocaleRenderer(final Locale displayLocale) {
        super();
        this.displayLocale = displayLocale;
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
        final Locale locale = (Locale) value;
        setText(locale.getDisplayCountry(displayLocale));
        return this;
    }
}
