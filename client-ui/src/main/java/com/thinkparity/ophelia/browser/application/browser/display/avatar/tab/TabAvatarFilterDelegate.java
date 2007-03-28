/**
 * Created On: Mar 27, 2007 1:20:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.util.List;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface TabAvatarFilterDelegate {

    /**
     * Obtain a list of the filter by definitions.
     * 
     * @return A <code>List</code> of <code>TabAvatarFilterBy</code>.
     */
    public List<TabAvatarFilterBy> getFilterBy();

    /**
     * Determine if a filter is currently selected.
     */
    public Boolean isFilterApplied();
}
