/*
 * Created On: Aug 14, 2006 12:43:25 PM
 */
package com.thinkparity.codebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class FuzzyDateFormat {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The default date pattern. */
    private final String defaultPattern;

    /** The pattern to use when the target occurs today. */
    private final String sameDayPattern;

    /** The pattern to use when the target occurs within the same month. */
    private final String sameMonthPattern;

    /** The pattern to use when the target occurs within the same year. */
    private final String sameYearPattern;

    /** The pattern to use when the target occurs within the same week. */
    private final String withinWeekPattern;

    /** Create FuzzyDateFormat. */
    public FuzzyDateFormat() {
        this("'on' MMM d, yyyy", "'at' hh:mm a", "'on' MMM d", "'on' MMM d", "'on' EEE");
    }

    /**
     * Create FuzzyDateUtil.
     * 
     * @param deafultPattern
     *            The default date pattern.
     * @param sameDayPattern
     *            The pattern to use when the target occurs today.
     * @param sameMonthPattern
     *            The pattern to use when the target occurs within the same
     *            month.
     * @param sameYearPattern
     *            The pattern to use when the target occurs within the same
     *            year.
     * @param withinWeekPattern
     *            The pattern to use when the target occurs within the same
     *            week.
     */
    public FuzzyDateFormat(final String defaultPattern,
            final String sameDayPattern, final String sameMonthPattern,
            final String sameYearPattern, final String withinWeekPattern) {
        super();
        this.defaultPattern = defaultPattern;
        this.sameDayPattern = sameDayPattern;
        this.sameMonthPattern = sameMonthPattern;
        this.sameYearPattern = sameYearPattern;
        this.withinWeekPattern = withinWeekPattern;
    }

    /**
     * Format the calendar.
     * 
     * @param calendar
     *            A calendar.
     * @return A fuzzy date/time format.
     */
    public String format(final Calendar target) {
        final Calendar now = DateUtil.getInstance();
        final String pattern;
        if (DateUtil.isSameDay(now, target)) {
            pattern = sameDayPattern;
        } else if (DateUtil.isWithinWeek(now, target)) {
            pattern = withinWeekPattern;
        } else if (DateUtil.isSameMonth(now, target)) {
            pattern = sameMonthPattern;
        } else if (DateUtil.isSameYear(now, target)) {
            pattern = sameYearPattern;
        } else {
            pattern = defaultPattern;
        }
        return new SimpleDateFormat(pattern).format(target.getTime());
    }
}
