/**
 * Created On: 12-Oct-06 11:37:23 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.util.Comparator;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar.SortColumn;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar.SortDirection;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerPanelComparator implements Comparator<TabPanel>{
    
    /** The sort element. */
    private SortColumn sortColumn = SortColumn.NONE;
    
    /** The sort direction. */
    private SortDirection sortDirection = SortDirection.NONE;

    /**
     * Create a ContainerPanelComparator.
     */
    ContainerPanelComparator() {
        super();
        this.sortColumn = SortColumn.NONE;
        this.sortDirection = SortDirection.NONE;
    }
    
    /**
     * Create a ContainerPanelComparator.
     */
    ContainerPanelComparator(final SortColumn sortColumn, final SortDirection sortDirection) {
        super();
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
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
            
            // Default is put local draft first, then by date (recent first).
            if (sortColumn==SortColumn.NONE || sortDirection==SortDirection.NONE) {
                compareResult = containerPanel2.getContainer().isLocalDraft().compareTo(containerPanel1.getContainer().isLocalDraft());
            } else if (sortColumn==SortColumn.BOOKMARK) {
                if (sortDirection==SortDirection.DOWN) {
                    compareResult = containerPanel2.getContainer().isBookmarked().compareTo(containerPanel1.getContainer().isBookmarked());
                } else {
                    compareResult = containerPanel1.getContainer().isBookmarked().compareTo(containerPanel2.getContainer().isBookmarked());
                }
            } else if (sortColumn==SortColumn.CONTAINER_NAME) {
                if (sortDirection==SortDirection.DOWN) {
                    compareResult = containerPanel2.getContainer().getName().compareToIgnoreCase(containerPanel1.getContainer().getName());
                } else {
                    compareResult = containerPanel1.getContainer().getName().compareToIgnoreCase(containerPanel2.getContainer().getName());
                }
            } else if (sortColumn==SortColumn.CONTAINER_DATE) {
                if (sortDirection==SortDirection.DOWN) {
                    compareResult = containerPanel2.getContainer().getUpdatedOn().compareTo(containerPanel1.getContainer().getUpdatedOn());
                } else {
                    compareResult = containerPanel1.getContainer().getUpdatedOn().compareTo(containerPanel2.getContainer().getUpdatedOn());
                }
            } else if (sortColumn==SortColumn.DRAFT_OWNER) {
                if (sortDirection==SortDirection.DOWN) {
                    compareResult = containerPanel1.getContainer().isDraft().compareTo(containerPanel2.getContainer().isDraft());
                    if ((0==compareResult) && (containerPanel2.getContainer().isDraft())) {
                        compareResult = containerPanel2.getDraft().getOwner().getName().compareToIgnoreCase(
                                        containerPanel1.getDraft().getOwner().getName());                       
                    }
                } else {
                    compareResult = containerPanel2.getContainer().isDraft().compareTo(containerPanel1.getContainer().isDraft());
                    if ((0==compareResult) && (containerPanel1.getContainer().isDraft())) {
                        compareResult = containerPanel1.getDraft().getOwner().getName().compareToIgnoreCase(
                                        containerPanel2.getDraft().getOwner().getName());                       
                    }
                }
            }
            
            // For all sorts, always go by date as a last resort.
            if (0==compareResult) {
                compareResult = containerPanel2.getContainer().getUpdatedOn().compareTo(containerPanel1.getContainer().getUpdatedOn());
            }
        }
        
        return compareResult;
    }
}
