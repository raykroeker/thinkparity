/*
 * Created On:  5-Jun-07 11:57:44 AM
 */
package com.thinkparity.service.adapter;

import java.util.Calendar;

import com.thinkparity.codebase.Constants;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CalendarXmlAdapter extends AbstractXmlAdapter<String, Calendar> {

    /**
     * Create CalendarAdapter.
     *
     */
    public CalendarXmlAdapter() {
        super();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     *
     */
    @Override
    public String marshal(final Calendar v) throws Exception {
        final Calendar universal = (Calendar) v.clone();
        universal.setTimeZone(Constants.UNIVERSAL_TIME_ZONE);
        return String.valueOf(universal.getTimeInMillis());
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     *
     */
    @Override
    public Calendar unmarshal(final String v) throws Exception {
        final Long millis = Long.valueOf(v);
        final Calendar c = Calendar.getInstance(Constants.UNIVERSAL_TIME_ZONE);
        c.setTimeInMillis(millis.longValue());
        c.setTimeZone(Constants.DEFAULT_TIME_ZONE);
        return c;
    }
}
