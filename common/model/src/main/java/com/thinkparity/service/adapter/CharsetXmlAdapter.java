/*
 * Created On:  30-May-07 2:46:51 PM
 */
package com.thinkparity.service.adapter;

import java.nio.charset.Charset;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CharsetXmlAdapter extends XmlAdapter<String, Charset> {

    /**
     * Create CharsetAdapter.
     *
     */
    public CharsetXmlAdapter() {
        super();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     *
     */
    @Override
    public String marshal(final Charset v) throws Exception {
        return v.name();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     *
     */
    @Override
    public Charset unmarshal(final String v) throws Exception {
        return Charset.forName(v);
    }
}
