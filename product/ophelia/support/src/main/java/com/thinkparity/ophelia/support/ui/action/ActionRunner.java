/*
 * Created On:  Nov 19, 2007 12:10:22 PM
 */
package com.thinkparity.ophelia.support.ui.action;

import com.thinkparity.ophelia.support.ui.Input;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ActionRunner {

    /**
     * Run an action
     * 
     */
    void run();

    /**
     * Run an action
     * 
     * @param input
     *            A <code>Input</code>.
     */
    void run(Input input);
}
