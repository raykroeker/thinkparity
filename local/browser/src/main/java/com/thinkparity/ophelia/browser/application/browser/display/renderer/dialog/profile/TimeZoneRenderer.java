/*
 * Created On:  18-Dec-06 7:07:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile;

import java.awt.Component;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TimeZoneRenderer extends DefaultListCellRenderer {

    private final Boolean displayDaylight;

    private final Locale displayLocale;

    private final Integer displayStyle;

    /**
     * Create TimeZoneRenderer.
     *
     * @param displayDaylight
     * @param displayStyle
     * @param displayLocale
     */
    public TimeZoneRenderer(final Boolean displayDaylight,
            final Integer displayStyle, final Locale displayLocale) {
        super();
        this.displayDaylight = displayDaylight;
        this.displayStyle = displayStyle;
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
        final TimeZone timeZone = (TimeZone) value;
        setText(timeZone.getDisplayName(displayDaylight, displayStyle, displayLocale));
        return this;
    }
}
