/*
 * Created On:  20-Apr-07 12:21:31 PM
 */
package com.thinkparity.desdemona.util;

import java.util.Calendar;

import com.thinkparity.codebase.DateUtil;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Date Time Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DateTimeProvider {

    /**
     * Obtain the current date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    public static Calendar getCurrentDateTime() {
        return DateUtil.getInstance();
    }

    /**
     * Create DateTimeProvider.
     *
     */
    private DateTimeProvider() {
        super();
    }
}
