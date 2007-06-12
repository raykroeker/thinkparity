/*
 * Created On:  5-Jun-07 2:20:21 PM
 */
package com.thinkparity.service.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EMailXmlAdapter extends XmlAdapter<String, EMail> {

    /**
     * Create EMailXmlAdapter.
     *
     */
    public EMailXmlAdapter() {
        super();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     *
     */
    @Override
    public String marshal(final EMail v) throws Exception {
        return v.toString();
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     *
     */
    @Override
    public EMail unmarshal(final String v) throws Exception {
        return EMailBuilder.parse(v);
    }
}
