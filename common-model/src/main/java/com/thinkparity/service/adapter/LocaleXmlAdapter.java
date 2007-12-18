/*
 * Created On:  30-May-07 3:02:55 PM
 */
package com.thinkparity.service.adapter;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LocaleXmlAdapter extends XmlAdapter<String, Locale> {

    /**
     * Create LocaleAdapter.
     *
     */
    public LocaleXmlAdapter() {
        super();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     *
     */
    @Override
    public String marshal(final Locale v) throws Exception {
        return new StringBuilder(16)
            .append(v.getISO3Country())
            .append(':')
            .append(v.getISO3Language())
            .toString();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     *
     */
    @Override
    public Locale unmarshal(final String v) throws Exception {
        final int index = v.indexOf(':');
        return new Locale(v.substring(0, index), v.substring(index + 1));
    }
}
