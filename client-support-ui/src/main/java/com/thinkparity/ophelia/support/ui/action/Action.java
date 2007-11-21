/*
 * Created On:  Nov 19, 2007 9:23:31 AM
 */
package com.thinkparity.ophelia.support.ui.action;

import com.thinkparity.ophelia.support.ui.Input;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Action {

    /**
     * Obtain the id.
     * 
     * @return A <code>String</code>.
     */
    String getId();

    /**
     * Invoke the action.
     * 
     * @param input
     *            An <code>Input</code>.
     */
    void invoke(Input input);
}
