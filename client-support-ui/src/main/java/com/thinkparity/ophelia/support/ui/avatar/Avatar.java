/*
 * Created On:  Nov 19, 2007 10:05:18 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import com.thinkparity.ophelia.support.ui.Input;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Avatar {

    /**
     * Obtain the avatar id.
     * 
     * @return A <code>String</code>.
     */
    String getId();

    /**
     * Reload the avatar.
     *
     */
    void reload();

    /**
     * Set the avatar input.
     * 
     * @param input
     *            An <code>Input</code>.
     */
    void setInput(Input input);
}
