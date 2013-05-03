/*
 * Created On:  22-Jan-07 10:07:52 AM
 */
package com.thinkparity.codebase;

import java.text.DateFormat;
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
public class DateFormatUtil {

    /**
     * Obtain a date format utility.
     * 
     * @param locale
     *            The <code>Locale</code> to format the date/time in.
     * @param timeZOne
     *            The <code>TimeZone</code> to format the date/time in.
     * @return A <code>DateFormatUtil</code>.
     */
    public static DateFormatUtil getInstance(final Locale locale,
            final TimeZone timeZone) {
        return new DateFormatUtil(locale, timeZone);
    }

    /** The <code>SimpleDateFormat</code> to use. */
    private final SimpleDateFormat format;

    /**
     * Create DateFormatUtil.
     *
     */
    private DateFormatUtil(final Locale locale, final TimeZone timeZone) {
        super();
        this.format = new SimpleDateFormat("", locale);
        this.format.setTimeZone(timeZone);
    }

    /**
     * Format the calendar.
     * 
     * @param pattern
     *            A date format pattern <code>String</code>.
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A formatted date/time.
     */
    public String format(final String pattern, final Calendar calendar) {
        return getFormat(pattern).format(calendar.getTime());
    }


    /**
     * Obtain a date format.
     * 
     * @param pattern
     *            A date format pattern <code>String</code>.
     * @return A <code>DateFormat</code>.
     */
    private DateFormat getFormat(final String pattern) {
        format.applyPattern(pattern);
        return format;
    }
}
