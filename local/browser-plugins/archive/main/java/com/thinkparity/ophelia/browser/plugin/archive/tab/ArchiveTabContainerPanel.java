/*
 * Created On:  29-Nov-06 1:32:34 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTabContainerPanel extends ContainerPanel {

    /**
     * Create ArchiveTabPanel.
     * 
     * @param session
     *            A <code>BrowserSession</code>.
     */
    public ArchiveTabContainerPanel(final BrowserSession session) {
        super(session);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel#getId()
     * 
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(container.getUniqueId())
            .toString();
    }
}
