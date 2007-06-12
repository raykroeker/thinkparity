/*
 * Created On:  26-May-07 12:17:32 PM
 */
package com.thinkparity.codebase.net.online;

import java.util.Properties;

/**
 * <b>Title:</b>thinkParity CommonCodebase Online Validator<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Validator {

    /**
     * Initialize the validator.
     * 
     * @param properties
     *            A validator configuration <code>Properties</code>.
     */
    public void initialize(final Properties properties);

    /**
     * Validate the online status.
     * 
     * @return True if online.
     */
    public Boolean validate();
}
