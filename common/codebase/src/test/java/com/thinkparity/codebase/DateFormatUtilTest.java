/*
 * Created On: Nov 24, 2006 7:29:54 AM
 */
package com.thinkparity.codebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.DateUtil.DateImage;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DateFormatUtilTest extends CodebaseTestCase {

    private static final String NAME = "Date format util test";

    private List<Fixture> data;

    /**
     * Create FuzzyDateTest.
     * 
     */
    public DateFormatUtilTest() {
        super(NAME);
    }

    public void testFormat() {
        final String pattern = "yyyy-MM-dd HH:mm:ss.SSS Z";
        for (final Fixture datum : data) {
            logger.logInfo("sameDay:{2} {0} {1}",
                    datum.locale.getDisplayName(), datum.timeZone.getID(),
                    datum.formatUtil.format(pattern, datum.sameDay));
        }
        for (final Fixture datum : data) {
            logger.logInfo("sameMonth:{2} {0} {1}",
                    datum.locale.getDisplayName(), datum.timeZone.getID(),
                    datum.formatUtil.format(pattern, datum.sameMonth));
        }
        for (final Fixture datum : data) {
            logger.logInfo("sameYear:{2} {0} {1}",
                    datum.locale.getDisplayName(), datum.timeZone.getID(),
                    datum.formatUtil.format(pattern, datum.sameYear));
        }
        for (final Fixture datum : data) {
            logger.logInfo("withinWeek:{2} {0} {1}",
                    datum.locale.getDisplayName(), datum.timeZone.getID(),
                    datum.formatUtil.format(pattern, datum.withinWeek));
        }
        for (final Fixture datum : data) {
            logger.logInfo("other:{2} {0} {1}",
                    datum.locale.getDisplayName(), datum.timeZone.getID(),
                    datum.formatUtil.format(pattern, datum.other));
        }

    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        data = new ArrayList<Fixture>(3);
        final Calendar now = DateUtil.getInstance();
        setUp(now, Locale.getDefault(), TimeZone.getTimeZone("GMT"));
        setUp(now, Locale.getDefault(), TimeZone.getTimeZone("America/Vancouver"));
        setUp(now, Locale.getDefault(), TimeZone.getTimeZone("America/Yellowknife"));
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    /**
     * Setup a reference to a fixture using the specified instance of the
     * calendar the locale and the time zone.
     * 
     * @param datum
     *            A reference to <code>Fixture</code>.
     * @param now
     *            An instance of <code>Calendar</code>.
     * @param locale
     *            A <code>Locale</code>.
     * @param timeZone
     *            A <code>TimeZone</code>.
     * @throws Exception
     */
    private void setUp(final Calendar now, final Locale locale,
            final TimeZone timeZone) throws Exception {
        final Calendar relativeNow = DateUtil.getInstance(now.getTime(), timeZone, locale);
        final int dayOfMonth = relativeNow.get(Calendar.DAY_OF_MONTH);
        final int monthOfYear = relativeNow.get(Calendar.MONTH);
        final int dayOfWeek = relativeNow.get(Calendar.DAY_OF_WEEK);

        final Fixture datum = new Fixture();
        datum.formatUtil = DateFormatUtil.getInstance(locale, timeZone);
        datum.locale = locale;
        datum.other = DateUtil.parse("1978-09-29 10:01:00.000", DateImage.ISO, timeZone, locale);
        datum.sameDay = (Calendar) relativeNow.clone();
        datum.sameMonth = (Calendar) relativeNow.clone();
        datum.sameMonth.set(Calendar.DAY_OF_MONTH, dayOfMonth < 16 ? dayOfMonth + 7 : dayOfMonth - 7);        
        datum.sameYear =  (Calendar) relativeNow.clone();
        datum.sameYear.set(Calendar.MONTH, monthOfYear < 7 ? monthOfYear + 3 : monthOfYear - 3);
        datum.timeZone = timeZone;
        datum.withinWeek =  (Calendar) relativeNow.clone();
        datum.withinWeek.set(Calendar.DAY_OF_WEEK, dayOfWeek < 4 ? dayOfWeek + 2 : dayOfWeek - 2);
        data.add(datum);
    }

    /**
     * <b>Title:</b>thinkParity Date Format Util Test Fixture<br>
     * <b>Description:</b>Contains the format util; the locale; a series of 5
     * dates and a time zone.<br>
     */
    private static class Fixture extends CodebaseTestCase.Fixture {
        private DateFormatUtil formatUtil;
        private Locale locale;
        private Calendar other;
        private Calendar sameDay;
        private Calendar sameMonth;
        private Calendar sameYear;
        private TimeZone timeZone;
        private Calendar withinWeek;
    }
}
