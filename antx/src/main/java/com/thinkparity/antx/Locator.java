/*
 * Created On:  27-Jan-07 8:01:07 PM
 */
package com.thinkparity.antx;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
interface Locator {

    /**
     * Attempt to locate a dependency.
     *
     */
    void locate(final String dependencyPath);
}
