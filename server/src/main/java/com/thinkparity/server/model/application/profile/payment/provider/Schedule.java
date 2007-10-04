/*
 * Created On:  9-Aug-07 4:00:15 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Provider Schedule<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Schedule {

    /** An interval. */
    private Interval interval;

    /** A iterations. */
    private Short iterations;

    /** A period. */
    private Short period;

    /** A start date. */
    private Calendar startDate;

    /**
     * Create Schedule.
     *
     */
    public Schedule() {
        super();
    }

    /**
     * Obtain the interval.
     *
     * @return A <code>Interval</code>.
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Obtain the period.
     *
     * @return A <code>Short</code>.
     */
    public Short getPeriod() {
        return period;
    }

    /**
     * Obtain the iterations.
     *
     * @return A <code>Short</code>.
     */
    public Short getIterations() {
        return iterations;
    }

    /**
     * Obtain the startDate.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * Set the interval.
     *
     * @param interval
     *		A <code>Interval</code>.
     */
    public void setInterval(final Interval interval) {
        this.interval = interval;
    }

    /**
     * Set the period.
     *
     * @param period
     *		A <code>Short</code>.
     */
    public void setPeriod(final Short period) {
        this.period = period;
    }

    /**
     * Set the iterations.
     *
     * @param iterations
     *		A <code>Short</code>.
     */
    public void setIterations(final Short recurrance) {
        this.iterations = recurrance;
    }

    /**
     * Set the startDate.
     *
     * @param startDate
     *		A <code>Calendar</code>.
     */
    public void setStartDate(final Calendar startDate) {
        this.startDate = startDate;
    }

    /** <b>Title:</b>Schedule Interval<br> */
    public enum Interval { DAY, MONTH, WEEK }
}
