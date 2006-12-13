/*
 * Created On:  12-Dec-06 6:58:58 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.util.List;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TabAvatarSortByDelegate {

    /**
     * Obtain a list of the sort by definitions.
     * 
     * @return A <code>List</code> of <code>TabAvatarSortBy</code>.
     */
    public List<TabAvatarSortBy> getSortBy();
}
