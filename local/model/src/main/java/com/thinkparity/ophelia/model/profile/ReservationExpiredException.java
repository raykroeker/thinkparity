/*
 * Created On:  30-Mar-07 2:53:39 PM
 */
package com.thinkparity.ophelia.model.profile;

import java.text.MessageFormat;
import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity OpheliaModel Reservation Expired Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReservationExpiredException extends Exception {

    /** Exception message format pattern <code>String</code>. */
    private static final String PATTERN;

    static {
        PATTERN = "Profile reservation expired on {0,date,yyyy-MM-dd HH:mm:ss.SSS Z}.";
    }

    /**
     * Create ReservationExpiredException.
     *
     */
    ReservationExpiredException(final Calendar expiredOn) {
        super(MessageFormat.format(PATTERN, expiredOn));
    }
}
