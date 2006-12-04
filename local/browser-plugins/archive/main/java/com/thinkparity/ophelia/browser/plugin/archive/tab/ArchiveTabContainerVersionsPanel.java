/*
 * Created On:  29-Nov-06 1:32:34 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTabContainerVersionsPanel extends ContainerVersionsPanel {

    /**
     * Create ArchiveTabPanel.
     *
     */
    public ArchiveTabContainerVersionsPanel() {
        super();
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
