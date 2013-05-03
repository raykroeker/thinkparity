/*
 * Created On:  Nov 19, 2007 11:32:29 AM
 */
package com.thinkparity.ophelia.support.ui.action.main;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Quit extends AbstractAction {

    /**
     * Create Quit.
     *
     */
    public Quit() {
        super("/main/quit");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.action.Input)
     *
     */
    public void invoke(final Input input) {
        getContext().lookupWindow("/main/main").close();
    }
}
