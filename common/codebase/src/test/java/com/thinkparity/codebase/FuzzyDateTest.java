/*
 * Created On: Nov 24, 2006 7:29:54 AM
 */
package com.thinkparity.codebase;

import java.util.Calendar;

import com.thinkparity.codebase.DateUtil.DateImage;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class FuzzyDateTest extends CodebaseTestCase {

    private static final String NAME = "Fuzzy Date Test";

    private Fixture datum;

    public void testFormat() {
        final FuzzyDateFormat format = new FuzzyDateFormat();
        logger.logVariable("sameDay", format.format(datum.sameDay));
        logger.logVariable("sameMonth", format.format(datum.sameMonth));
        logger.logVariable("sameYear", format.format(datum.sameYear));
        logger.logVariable("withinWeek", format.format(datum.withinWeek));
        logger.logVariable("other", format.format(datum.other));
    }

    /**
     * Create FuzzyDateTest.
     * 
     */
    public FuzzyDateTest() {
        super(NAME);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Calendar today, sameDay, sameMonth, sameYear, withinWeek, other;
        today = DateUtil.getInstance();
        final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        final int monthOfYear = today.get(Calendar.MONTH);
        final int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        sameDay = (Calendar) today.clone();
        sameMonth = (Calendar) today.clone();
        sameMonth.set(Calendar.DAY_OF_MONTH, dayOfMonth < 16 ? dayOfMonth + 7 : dayOfMonth - 7);
        sameYear = (Calendar) today.clone();
        sameYear.set(Calendar.MONTH, monthOfYear < 7 ? monthOfYear + 3 : monthOfYear - 3);
        withinWeek = (Calendar) today.clone();
        withinWeek.set(Calendar.DAY_OF_WEEK, dayOfWeek < 4 ? dayOfWeek + 2 : dayOfWeek - 2);
        other = DateUtil.parse("1978-09-29 10:01:00.000", DateImage.ISO);
        datum = new Fixture(sameDay, sameMonth, sameYear, withinWeek, other);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }
    private static class Fixture extends CodebaseTestCase.Fixture {
        private final Calendar other;
        private final Calendar sameDay;
        private final Calendar sameMonth;
        private final Calendar sameYear;
        private final Calendar withinWeek;
        private Fixture(final Calendar sameDay, final Calendar sameMonth,
                final Calendar sameYear, final Calendar withinWeek,
                final Calendar other) {
            this.sameDay = sameDay;
            this.sameMonth = sameMonth;
            this.sameYear = sameYear;
            this.withinWeek = withinWeek;
            this.other = other;
        }
    }
}
