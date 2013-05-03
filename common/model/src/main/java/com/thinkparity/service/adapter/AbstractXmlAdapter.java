/*
 * Created On:  5-Jun-07 11:59:13 AM
 */
package com.thinkparity.service.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractXmlAdapter<ValueType, BoundType> extends
        XmlAdapter<ValueType, BoundType> {

    /** A log4j wrapper. */
    protected static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper("SERVICE");
    }

    /**
     * Create Adapter.
     *
     */
    protected AbstractXmlAdapter() {
        super();
    }
}
