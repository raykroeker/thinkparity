/**
 * Created On: 12-Oct-06 11:37:23 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.util.Comparator;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerPanelComparator implements Comparator<TabPanel>{

    /**
     * Create a ContainerPanelComparator.
     */
    ContainerPanelComparator() {
        super();
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     * 
     */
    public int compare(final TabPanel o1, final TabPanel o2) {
    
        int compareResult = 0;
        if ((o1 instanceof ContainerPanel) && (o2 instanceof ContainerPanel)) { // Should be true
            ContainerPanel containerPanel1 = (ContainerPanel) o1;
            ContainerPanel containerPanel2 = (ContainerPanel) o2;
            
            // Put local draft first, then by date (recent first).
            compareResult = containerPanel2.getContainer().isLocalDraft().compareTo(containerPanel1.getContainer().isLocalDraft());
            if (0==compareResult) {
                compareResult = containerPanel2.getContainer().getUpdatedOn().compareTo(containerPanel1.getContainer().getUpdatedOn());
            }
        }
        
        return compareResult;
    }
}
