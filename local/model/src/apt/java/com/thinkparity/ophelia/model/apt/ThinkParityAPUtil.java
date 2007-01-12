/*
 * Created On:  11-Jan-07 9:09:17 PM
 */
package com.thinkparity.ophelia.model.apt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ThinkParityAPUtil {

    /**
     * Obtain the current date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    static Calendar getCurrentDateTime() {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * Format a date/time.
     * 
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A formatted date/time.
     */
    static String format(final Calendar calendar) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                calendar.getTime());
    }

    /**
     * Create ThinkParityAPUtil.
     *
     */
    private ThinkParityAPUtil() {
        super();
    }
}
