/**
 * Created On: 12-Sep-06 3:01:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.util.Comparator;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerCell;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerCellComparator implements Comparator<ContainerCell>{


    /**
     * Create a ContainerCellComparator.
     */
    ContainerCellComparator() {
        super();
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     * 
     */
    public int compare(final ContainerCell o1, final ContainerCell o2) {
        
        // Put local draft first, then other drafts, finally by date (recent first).
        int compareResult = o2.isLocalDraft().compareTo(o1.isLocalDraft());
        if (0==compareResult) {
            compareResult = o2.isDraft().compareTo(o1.isDraft());
        }
        if (0==compareResult) {
            compareResult = o2.getUpdatedOn().compareTo(o1.getUpdatedOn());
        }
        
        return compareResult;
    }
}
