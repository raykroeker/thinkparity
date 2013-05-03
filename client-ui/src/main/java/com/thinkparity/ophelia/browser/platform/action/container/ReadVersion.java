/**
 * Created On: 27-Jun-2006 2:25:50 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Read Version<br>
 * <b>Description:</b>The action responsible for reading a container version.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see Browser#runReadContainerVersion(Long, Long)
 */
public final class ReadVersion extends AbstractBrowserAction {

    /**
     * Create ReadVersion.
     * 
     */
    public ReadVersion(final Browser browser) {
        super(ActionId.CONTAINER_READ_VERSION);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        getBrowserApplication().displayContainerVersionInfoDialog(containerId,
                versionId);
    }

    /** Data keys. */
    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
