/*
 * Created On: Oct 15, 2006 1:16:40 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Script {

    /**
     * Obtain the script name.
     * 
     * @return The script name <code>String</code>.
     */
    public String getName();

    /**
     * Open the script input stream.
     * 
     * @return A script intput stream.
     */
    public InputStream openStream();
}
