/*
 * Sep 16, 2003
 */
package com.thinkparity.codebase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

/**
 * <b>Title:</b>  DateUtil
 * <br><b>Description:</b>  
 * 
 * Updated to JDK 5.0
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class DateUtil {

	/**
	 * Obtain from a Calendar, the desired DateImage as a String.  The default
	 * Locale will be used when obtaining the format.
	 * @param calendar <code>java.util.Calendar</code>
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String format(
		Calendar calendar,
		DateImage dateImage) {
		return format(calendar, dateImage, null);
	}
	
	/**
	 * Obtain from a Calendar, the desired DateImage as a String, using the Locale
	 * to obtain the format.
	 * @param calendar <code>java.util.Calendar</code>
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String format(
		Calendar calendar,
		DateImage dateImage,
		Locale locale) {
		if(null == calendar)
			return null;
		return getSimpleDateFormat(dateImage, locale).format(calendar.getTime());
	}

	public static synchronized String format(
		Calendar calendar,
		String image) {
		return DateUtil.formatCalendar(calendar, image, null);
	}

	public static synchronized String format(
		Calendar calendar,
		String image,
		Locale locale) {
		return DateUtil.formatCalendar(calendar, image, locale);
	}
	
	/**
	 * Format the number of milliseconds from Jan 1, 1970 into a display format.
	 * @param milliseconds <code>long</code>
	 * @param dateImage <code>DateUtil$DateFormat</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String format(
		long milliseconds,
		DateImage dateImage) {
		return format(milliseconds, dateImage, null);
	}

	/**
	 * Format the number of milliseconds from Jan 1, 1970 into a display format.
	 * @param milliseconds <code>long</code>
	 * @param dateImage <code>DateUtil$DateFormat</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String format(
		long milliseconds,
		DateImage dateImage,
		Locale locale) {
		return getSimpleDateFormat(dateImage, locale).format(
			getInstance(milliseconds, locale).getTime());
	}

	/**
	 * Obtain a calendar initialized to the default TimeZone and Locale.
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance() {
		return DateUtil.getInstance((TimeZone) null, (Locale) null);
	}

	/**
	 * Obtain a Calendar instance based upon the default TimeZone and Locale, then
	 * set the time according to Date
	 * @param date <code>java.util.Date</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(Date date) {
		return getInstance(date, null, null);
	}

	/**
	 * Obtain a Calendar instance based upon the Locale and default TimeZone, then
	 * set the time according to Date
	 * @param date <code>java.util.Date</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 */	
	public static synchronized Calendar getInstance(Date date, Locale locale) {
		return getInstance(date, null, locale);
	}

	/**
	 * Obtain a Calendar instance based upon the TimeZone and default Locale, then
	 * set the time according to Date
	 * @param date <code>java.util.Date</code>
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(
		Date date,
		TimeZone timeZone) {
		return getInstance(date, timeZone, null);
	}	

	/**
	 * Obtain a Calendar instance based upon the TimeZone and Locale, then set the
	 * time according to Date
	 * @param date <code>java.util.Date</code>
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(
		Date date,
		TimeZone timeZone,
		Locale locale) {
		if(null == date)
			return null;
		Calendar calendar = getInstance(timeZone, locale);
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * Obtain a Calendar instance based upon the default TimeZone and Locale, then
	 * set the time according to milliseconds
	 * @param milliseconds <code>long</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(long milliseconds) {
		return getInstance(milliseconds, null, null);
	}

	/**
	 * Obtain a Calendar instance based upon the Locale and default TimeZone, then
	 * set the time according to milliseconds
	 * @param milliseconds <code>long</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(
		long milliseconds,
		Locale locale) {
		return getInstance(milliseconds, null, locale);
	}

	/**
	 * Obtain a Calendar instance based upon the TimeZone and default Locale, then
	 * set the time according to milliseconds
	 * @param milliseconds <code>long</code>
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(
		long milliseconds,
		TimeZone timeZone) {
		return getInstance(milliseconds, timeZone, null);
	}

	/**
	 * Obtain a Calendar instance based upon the TimeZone and Locale, then set the
	 * time according to milliseconds
	 * @param milliseconds <code>long</code>
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 */
	public static synchronized Calendar getInstance(
		long milliseconds,
		TimeZone timeZone,
		Locale locale) {
		Calendar calendar = getInstance(timeZone, locale);
		calendar.setTimeInMillis(milliseconds);
		return calendar;
	}

	/**
	 * Determine whether or not calendar is the first day of the month.  This is done
	 * by comparing the actual minimum value of the 
	 * <code>java.util.Calendar.DAY_OF_MONTH</code> member against its 
	 * value.
	 * @param calendar <code>java.util.Calendar</code>
	 * @return <code>boolean</code>
	 */
	public static synchronized boolean isFirstDayOfMonth(Calendar calendar) {
		return calendar.getActualMinimum(java.util.Calendar.DAY_OF_MONTH)
			== calendar.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * Determine whether or not calendar is the first day of the week.  This is done 
	 * by comparing the <code>java.util.Calendar.getFirstDayOfWeek()</code> 
	 * value with the <code>java.util.Calendar.DAY_OF_WEEK</code> value
	 * @param calendar <code>java.util.Calendar</code>
	 * @return <code>boolean</code>
	 */
	public static synchronized boolean isFirstDayOfWeek(Calendar calendar) {
		return calendar.getActualMinimum(java.util.Calendar.DAY_OF_WEEK)
			== calendar.get(java.util.Calendar.DAY_OF_WEEK);
	}

	/**
	 * Determine whether or not calendar is the last day of the month.  This is done 
	 * by comparing the actual maximum value of the 
	 * <code>java.util.Calendar.DAY_OF_MONTH</code> member against is value.
	 * @param calendar <code>java.util.Calendar</code>
	 * @return <code>boolean</code>
	 */
	public static synchronized boolean isLastDayOfMonth(Calendar calendar) {
		return calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
			== calendar.get(java.util.Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Determine whether or not calendar is the last day of the week.  This is done 
	 * by comparing the <code>java.util.Calendar.getFirstDayOfWeek()</code>
	 * value with the <code>java.util.Calendar.DAY_OF_WEEK</code> value plus 
	 * the actual maximum of the same value.
	 * @param calendar <code>java.util.Calendar</code>
	 * @return <code>boolean</code>
	 */
	public static synchronized boolean isLastDayOfWeek(Calendar calendar) {
		return calendar.getActualMaximum(java.util.Calendar.DAY_OF_WEEK)
			== calendar.get(java.util.Calendar.DAY_OF_WEEK);
	}

    /**
     * Determine whether or not the dates occur on the same day.
     * 
     * @param c1
     *            A calendar.
     * @param c2
     *            A calendar.
     * @return True if the year/month/day all match.
     */
    public static Boolean isSameDay(final Calendar c1, final Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
            && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Determine whether or not the dates occur within the same month.
     * 
     * @param c1
     *            A calendar.
     * @param c2
     *            A calendar.
     * @return True if the difference between the dates is less than 7 days.
     */
    public static Boolean isSameMonth(final Calendar c1, final Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    /**
     * Determine whether or not the dates occur within the same month.
     * 
     * @param c1
     *            A calendar.
     * @param c2
     *            A calendar.
     * @return True if the difference between the dates is less than 7 days.
     */
    public static Boolean isSameYear(final Calendar c1, final Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    /**
     * Determine whether or not the dates occur within a week.
     * 
     * @param c1
     *            A calendar.
     * @param c2
     *            A calendar.
     * @return True if the difference between the dates is less than 7 days.
     */
    public static Boolean isWithinWeek(final Calendar c1, final Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
            && Math.abs(c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH)) < 7;
    }

	/**
	 * Parse the string for a <code>java.util.Calendar</code> which matches 
	 * dateImage.  Uses the default <code>java.util.TimeZone</code> and 
	 * <code>java.util.Locale</code>
	 * @param string <code>java.lang.String</code>
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @return <code>java.util.Calendar</code>
	 * @throws ParseException
	 */
	public static synchronized Calendar parse(
		String string,
		DateImage dateImage)
		throws ParseException {
		return parse(string ,dateImage, null, null);
	}

    /**
	 * Parse the string for a <code>java.util.Calendar</code> which matches 
	 * dateImage.  Uses timeZone and the default <code>java.util.Locale</code>
	 * @param string <code>java.lang.String</code>
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @return <code>java.util.Calendar</code>
	 * @throws ParseException
	 */
	public static synchronized Calendar parse(
		String string,
		DateImage dateImage,
		TimeZone timeZone)
		throws ParseException {
		return parse(string, dateImage, null, null);
	}

	/**
	 * Parse the string for a <code>java.util.Calendar</code> which matches 
	 * dateImage.  Uses timeZone and locale
	 * @param string <code>java.lang.String</code>
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 * @throws ParseException
	 */
	public static synchronized Calendar parse(
		String string,
		DateImage dateImage,
		TimeZone timeZone,
		Locale locale)
		throws ParseException {
		if(null == string)
			return null;
		return getInstance(getSimpleDateFormat(dateImage, locale).parse(string), timeZone, locale);
	}

	private static synchronized String formatCalendar(
		Calendar calendar,
		String image,
		Locale locale) {
		return getSimpleDateFormat(image, locale).format(calendar.getTime());
	}

	/**
	 * Obtain a Calendar instance based upon a TimeZone and Locale.  If either are
	 * not specified, the defaults will be used.
	 * @param timeZone <code>java.util.TimeZone</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.util.Calendar</code>
	 */
	private static synchronized Calendar getInstance(
		TimeZone timeZone,
		Locale locale) {
		if(null == locale)
			locale = Locale.getDefault();
		if(null == timeZone)
			timeZone = TimeZone.getDefault();
		return Calendar.getInstance(timeZone, locale);
	}
	
	/**
	 * Obtain a SimpleDateFormat to use for formatting a Date according to
	 * DateImage.  If the Locale is not passed, the system default will be used.
	 * @param dateImage <code>DateUtil$DateImage</code>
	 * @param locale <code>java.util.Locale</code>
	 * @return <code>java.text.SimpleDateFormat</code>
	 */	
	private static synchronized SimpleDateFormat getSimpleDateFormat(
		DateImage dateImage,
		Locale locale) {
		return DateUtil.getSimpleDateFormat(dateImage.toString(), locale);
	}

	private static synchronized SimpleDateFormat getSimpleDateFormat(
		String image,
		Locale locale) {
		if(null == locale)
			locale = Locale.getDefault();
		return new SimpleDateFormat(image, locale);
	}
	
	/**
	 * Create a new DateUtil [Singleton]
	 */
	private DateUtil() {super();}
	
	/**
	 * <b>Title:</b>  DateImage
	 * <br><b>Description:</b>  A discrete set of images used for
	 * parsing dates.  To create a new image, used the following symbols:
	 * <pre>
	 * Symbol   Meaning                 Presentation        Example
	 * ------   -------                 ------------        -------
	 * G        era designator          (Text)              AD
	 * y        year                    (Number)            1996
	 * M        month in year           (Text & Number)     July & 07
	 * d        day in month            (Number)            10
	 * h        hour in am/pm (1~12)    (Number)            12
	 * H        hour in day (0~23)      (Number)            0
	 * m        minute in hour          (Number)            30
	 * s        second in minute        (Number)            55
	 * S        millisecond             (Number)            978
	 * E        day in week             (Text)              Tuesday
	 * D        day in year             (Number)            189
	 * F        day of week in month    (Number)            2 (2nd Wed in July)
	 * w        week in year            (Number)            27
	 * W        week in month           (Number)            2
	 * a        am/pm marker            (Text)              PM
	 * k        hour in day (1~24)      (Number)            24
	 * K        hour in am/pm (0~11)    (Number)            0
	 * z        time zone               (Text)              Pacific Standard Time
	 * '        escape for text         (Delimiter)
	 * ''       single quote            (Literal)           '
	 * </pre>
	 * @author raykroeker@gmail.com
	 * @version 1.0.0
	 */
	public static final class DateImage extends Enum {
		/**
		 * Sunday, January 1, 1900
		 */
		public static final DateImage DayMonthYear =
			new DateImage("EEEE, MMMM d, yyyy");

		/**
		 * 01
		 */
		public static final DateImage DayOfMonth = new DateImage("dd");

		/**
		 * 01.01.1900
		 */
		public static final DateImage FileSafeDate =
			new DateImage("MM.dd.yyyy");
		
		/**
		 * 01.01.1900_00.00.00
		 */
		public static final DateImage FileSafeDateTime =
			new DateImage("MM.dd.yyyy_HH.mm.ss");

		/**
		 * 00:00
		 */
		public static final DateImage HourMinute = new DateImage("HH:mm");

		/**
		 * 00:00:00
		 */
		public static final DateImage HourMinuteSecond =
			new DateImage("HH:mm:ss");
	
		/**
		 * 00:00:00.000
		 */
		public static final DateImage HourMinuteSecondMilli =
			new DateImage("HH:mm:ss.SSS");
		
		/**
		 * 1900-01-01 00:00:00.000
		 */
		public static final DateImage ISO =
			new DateImage("yyyy-MM-dd HH:mm:ss.SSS");
		
		/**
		 * January 1900
		 */
		public static final DateImage MonthYear = new DateImage("MMMM yyyy");

		/**
		 * 1900-01-01
		 */
		public static final DateImage MySQLDate =
			new DateImage("yyyy-MM-dd");

		/**
		 * 1900-01-01 00:00:00
		 */
		public static final DateImage MySQLDateTime =
			new DateImage("yyyy-MM-dd HH:mm:ss");

		/**
		 * 00:00:00
		 */
		public static final DateImage MySQLTime = new DateImage("HH:mm:ss");

		/**
		 * 01 01 1900; 00:00:00
		 */
		public static final DateImage OracleDateTime =
			new DateImage("MM dd yyyy; HH:mm:ss");

		/**
		 * 1
		 */
		public static final DateImage SingleDayOfMonth = new DateImage("d");
		
		/**
		 * 19000101/0000000
		 */
		public static final DateImage SysLogDateTime =
			new DateImage("yyyyMMdd/HHmmssSSS");
		
		/**
		 * 01-01-1900
		 */
		public static final DateImage WebDate = new DateImage("MM-dd-yyyy");
		
		/**
		 * 01-01-1900 12:00 AM
		 */
		public static final DateImage WebDateTime =
			new DateImage("MM-dd-yyyy hh:mm a");
		
		/**
		 * 12:00 AM
		 */
		public static final DateImage WebTime = new DateImage("hh:mm a");
			
		/**
		 * 1900 01 01 00:00
		 */
		public static final DateImage YearMonthDayHourMinute =
			new DateImage("yyyy MM dd HH:mm");

		private static final long serialVersionUID = 1;
		
		/**
		 * Create a custom date image at runtime.
		 * @param dateImage <code>java.lang.String</code>
		 * @return <code>DateUtil$DateImate</code>
		 */
		public static synchronized final DateUtil.DateImage getInstance(
			String dateImage) {
			DateUtil.DateImage existingDateImage =
				(DateUtil.DateImage) Enum.find(DateUtil.DateImage.class, dateImage);
			return null == existingDateImage
				? DateUtil.DateImage.getInstance(dateImage)
				: existingDateImage;
		}

		/**
		 * Create a new DateImage
		 */
		private DateImage(String image) {super(image, false);}
	}
	
	/**
	 * <b>Title:</b>  DayOfWeek
	 * <br><b>Description:</b>  Provides an enum of the days of the week
	 * @author raykroeker@gmail.com
	 * @version 1.0.0
	 */
	public static final class DayOfWeek extends Enum {
		/**
		 * Friday
		 */
		public static final DayOfWeek Friday =
			new DayOfWeek("Friday", "F", java.util.Calendar.FRIDAY);

		/**
		 * Monday
		 */
		public static final DayOfWeek Monday =
			new DayOfWeek("Monday", "M", java.util.Calendar.MONDAY);

		/**
		 * Saturday
		 */
		public static final DayOfWeek Saturday =
			new DayOfWeek("Saturday", "S", java.util.Calendar.SATURDAY);

		/**
		 * Sunday
		 */		
		public static final DayOfWeek Sunday =
			new DayOfWeek("Sunday", "S", java.util.Calendar.SUNDAY);

		/**
		 * Thursday
		 */
		public static final DayOfWeek Thursday =
			new DayOfWeek("Thursday", "T", java.util.Calendar.THURSDAY);

		/**
		 * Tuesday
		 */
		public static final DayOfWeek Tuesday =
			new DayOfWeek("Tuesday", "T", java.util.Calendar.TUESDAY);

		/**
		 * Wednesday
		 */
		public static final DayOfWeek Wednesday =
			new DayOfWeek("Wednesday", "W", java.util.Calendar.WEDNESDAY);

		private static final long serialVersionUID = 1;

		/**
		 * Find by <code>java.util.Calendar.DAY_OF_WEEK</code> variable.
		 * @param calendarDayOfWeek <code>int</code>
		 * @return <code>DateUtil$DayOfWeek</code>
		 */
		public static DayOfWeek findByCalendarDayOfWeek(int calendarDayOfWeek) {
			Vector<Enum> allDaysOfWeek = Enum.getList(DayOfWeek.class);
			DayOfWeek dayOfWeek;
			for(int i = 0; i < allDaysOfWeek.size(); i++) {
				dayOfWeek = (DayOfWeek) allDaysOfWeek.get(i);
				if(dayOfWeek.getCalendarDayOfWeek() == calendarDayOfWeek)
					return dayOfWeek;
			}
			return null;
		}

		/**
		 * Abbrivation of the day of the week
		 */
		private String abbrevation;
		
		/**
		 * <code>java.util.Calendar.DAY_OF_WEEK</code>
		 */
		private int calendarDayOfWeek;

		/**
		 * Create a new DayOfWeek
		 * @param dayOfWeek <code>java.lang.String</code>
		 */
		private DayOfWeek(String dayOfWeek, String abbrivation, int calendarDayOfWeek) {
			super(dayOfWeek);
			this.abbrevation = abbrivation;
			this.calendarDayOfWeek = calendarDayOfWeek;
		}

		/**
		 * Obtain the abbrevation
		 * @return <code>java.lang.String</code>
		 */
		public String getAbbreviation() {return abbrevation;}
		
		/**
		 * Obtain the <code>java.util.Calendar.DAY_OF_WEEK</code>
		 * @return <code>int</code>
		 */
		public int getCalendarDayOfWeek() {return calendarDayOfWeek;}

	}
}
