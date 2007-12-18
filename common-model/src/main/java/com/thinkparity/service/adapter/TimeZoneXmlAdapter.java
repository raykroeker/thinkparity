/*
 * Created On:  30-May-07 3:28:47 PM
 */
package com.thinkparity.service.adapter;

import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TimeZoneXmlAdapter extends XmlAdapter<String, TimeZone> {

    /**
     * Create TimeZoneAdapter.
     *
     */
    public TimeZoneXmlAdapter() {
        super();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     *
     */
    @Override
    public String marshal(final TimeZone v) throws Exception {
        return v.getID();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     *
     */
    @Override
    public TimeZone unmarshal(final String v) throws Exception {
        return TimeZone.getTimeZone(v);
    }
}
