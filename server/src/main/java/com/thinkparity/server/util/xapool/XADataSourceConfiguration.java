/*
 * Created On:  26-Mar-07 7:31:41 PM
 */
package com.thinkparity.codebase.model.util.xapool;

import java.util.Properties;

/**
 * <b>Title:</b>thinkParity CommonModel Transactional Data Source Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XADataSourceConfiguration extends Properties {

    /**
     * Create XADataSourceConfiguration.
     *
     */
    public XADataSourceConfiguration() {
        super();
    }

    public String getProperty(final Key key) {
        return super.getProperty(key.name());
    }

    public String getProperty(final Key key, final String defaultValue) {
        return super.getProperty(key.name(), defaultValue);
    }

    public synchronized String setProperty(final Key key, String value) {
        return (String) setProperty(key.name(), value);
    }

    public enum Key { DRIVER, PASSWORD, URL, USER }
}
