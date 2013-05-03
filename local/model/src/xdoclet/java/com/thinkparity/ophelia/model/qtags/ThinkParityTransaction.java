/*
 * Created On:  11-Jan-07 11:59:44 AM
 */
package com.thinkparity.ophelia.model.qtags;

import com.thoughtworks.qdox.model.DocletTag;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @qtags.location class
 * @qtags.location method
 * @qtags.once
 */
public interface ThinkParityTransaction extends DocletTag {

    /**
     * Obtain the transaction type.
     * 
     * @return The transaction type <code>String</code>.
     * @qtags.allowed-value never
     * @qtags.allowed-value required
     * @qtags.allowed-value requires-new
     * @qtags.allowed-value supported
     * @qtags.required
     */
    public String getType();
}
