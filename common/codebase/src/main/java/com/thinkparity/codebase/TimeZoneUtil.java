/*
 * Created On:  20-Feb-07 3:07:42 PM
 */
package com.thinkparity.codebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

/**
 * <b>Title:</b>thinkParity TimeZone Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TimeZoneUtil {

    /** A singleton instance of <code>TimeZoneUtil</code>. */
    private static final TimeZoneUtil INSTANCE;

    static {
        INSTANCE = new TimeZoneUtil();
    }

    /**
     * Obtain an instance of time zone util.
     * 
     * @return An instance of <code>TimeZoneUtil</code>.
     */
    public static TimeZoneUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Create TimeZoneUtil.
     *
     */
    private TimeZoneUtil() {
        super();
    }

    /**
     * Obtain a list of available time zones.
     * 
     * @return A <code>TimeZone[]</code>.
     */
    public TimeZone[] getAvailableTimeZones() {
        final String[] timeZoneIDs = TimeZone.getAvailableIDs();
        final List<TimeZone> availableTimeZones = new ArrayList<TimeZone>(timeZoneIDs.length);
        TimeZone timeZone;
        for (final String timeZoneID : timeZoneIDs) {
            timeZone = TimeZone.getTimeZone(timeZoneID);
            if (isValid(timeZone))
                if (!contains(availableTimeZones, timeZone))
                    availableTimeZones.add(timeZone);
        }
        Collections.sort(availableTimeZones, new Comparator<TimeZone>() {
            public int compare(final TimeZone o1, final TimeZone o2) {
                return Integer.valueOf(o1.getRawOffset()).compareTo(
                        Integer.valueOf(o2.getRawOffset()));
            }
        });
        return availableTimeZones.toArray(new TimeZone[] {});
    }

    /**
     * Determine if the list of locales already contains the locale. A
     * determination is made based solely upon the language and country. The
     * variant is ignored.
     * 
     * @param locales
     *            A <code>List</code> of <code>Locale</code>s.
     * @param locale
     *            A <code>Locale</code>.
     * @return True if the local exists in the list.
     */
    private boolean contains(final List<TimeZone> timeZones, final TimeZone timeZone) {
        for (final TimeZone tz : timeZones) {
            if (tz.getID().equals(timeZone.getID()))
                return true;
        }
        return false;
    }

    /**
     * Determine if a time zone is valid.
     * 
     * @param timeZone
     *            A <code>TimeZone</code>.
     * @return True if the locale is valid.
     */
    private boolean isValid(final TimeZone timeZone) {
        return true;
    }
}
