/*
 * Created On:  11-Jan-07 9:31:03 AM
 */
package com.thinkparity.ophelia.model.qtags;

import com.thoughtworks.qdox.model.DocletTag;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @qtags.location method
 * @qtags.once
 */
public interface ThinkParityInterfaceMethod extends DocletTag {

    /**
     * Define the interface method visibility.
     * 
     * @return A method's visibility <code>String</code>.
     * @qtags.allowed-value external
     * @qtags.allowed-value internal
     * @qtags.default internal
     */
    public String getVisibility();
}
