/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ArchiveCell extends DefaultTabCell {

    /**
     * Create ArchiveCell.
     * 
     */
    ArchiveCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    @Override
    public abstract String getTextNoClipping(final TextGroup textGroup);
}
