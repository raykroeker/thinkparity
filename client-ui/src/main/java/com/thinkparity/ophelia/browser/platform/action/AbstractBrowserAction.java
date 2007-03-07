/**
 * Created On: Mar 6, 2007 5:17:18 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class AbstractBrowserAction extends AbstractAction {

    /**
     * Create AbstractBrowserAction.
     * 
     * @param id
     *            An action id.
     */
    protected AbstractBrowserAction(final ActionId id) {
        super(id);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invokeAction(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invokeAction(Data data) {
        getBrowserApplication().fireActionInvoked();
        super.invokeAction(data);
    }
}
