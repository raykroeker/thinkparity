/**
 * Created On: Dec 31, 2006 5:12:59 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.AbstractLinkAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayFlagSeenInfo extends AbstractAction {

    /** The browser application. */
    private final Browser browser;
    
    /** An instance of the link action. */
    private final DisplayFlagSeenInfoLink displayFlagSeenInfoLink;
    
    /** A user search expression <code>String</code>. */
    private String searchExpression;

    /**
     * Create a DisplayFlagSeenInfo.
     * 
     * @param browser
     *            The browser application.
     */
    public DisplayFlagSeenInfo(final Browser browser) {
        super(ActionId.CONTAINER_DISPLAY_FLAG_SEEN_INFO);
        this.browser = browser;
        this.displayFlagSeenInfoLink = new DisplayFlagSeenInfoLink(browser);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final List<Container> containers;
        searchExpression = (String) data.get(DataKey.SEARCH_EXPRESSION);
        
        // Prepare the list of containers
        if (isSearchApplied()) {
            final List<Long> containerIds = getContainerModel().search(searchExpression);
            containers = new ArrayList<Container>();
            for (final Long containerId : containerIds) {
                final Container container = getContainerModel().read(containerId);
                if (!containers.contains(container)) {
                    containers.add(container);
                }
            }
        } else {
            containers = getContainerModel().read();            
        }

        // Build the list of containers that have not been seen
        final List<Long> containerIds = new ArrayList<Long>();
        for (final Container container : containers) {
            if (!container.isSeen()) {
                containerIds.add(container.getId());
            }
        }

        // Prepare the LinkAction
        displayFlagSeenInfoLink.setContainerIds(containerIds);
        browser.setStatus(displayFlagSeenInfoLink);                   
    }
    
    /**
     * Determine if a search is applied.
     * 
     * @return True if a search expression is set.
     */
    private boolean isSearchApplied() { 
        return null != searchExpression;
    }    
    
    public class DisplayFlagSeenInfoLink extends AbstractLinkAction {
        
        /** The browser application. */
        private final Browser browser;
        
        /** The list of not-seen containers. */
        private List<Long> containerIds;        
        
        public DisplayFlagSeenInfoLink(final Browser browser) {
            super();
            this.browser = browser;
        }
        
        /**
         * Set the list of not-seen container ids.
         */
        public void setContainerIds(final List<Long> containerIds) {
            this.containerIds = containerIds;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getAction()
         */
        public javax.swing.Action getAction() {
            return new javax.swing.AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    browser.selectTab(MainTitleAvatar.TabId.CONTAINER);
                    browser.showContainer(containerIds, 0);
                }
            };
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getActionName()
         */
        public String getActionName() {
            return ("DisplayFlagSeenInfoLink");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getIntroText()
         */
        public String getIntroText(final Boolean displayedFirst) {
            if (displayedFirst) {
                return localization.getString("ContainerNotSeenIntro");
            } else {
                return localization.getString("ContainerNotSeenIntroNotFirst");      
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkType()
         */
        public LinkType getLinkType() {
            if (0 == containerIds.size()) {
                return LinkType.CLEAR;
            } else {
                return LinkType.SHOW_ALWAYS;
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkText()
         */
        public String getLinkText() {
            if (1 == containerIds.size()) {
                return localization.getString("ContainerNotSeenOne", new Object[] {containerIds.size()});
            } else {
                return localization.getString("ContainerNotSeenMany", new Object[] {containerIds.size()});
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getPriority()
         */
        public LinkPriority getPriority() {
            return LinkPriority.HIGH;
        }        
    }
    
    public enum DataKey { SEARCH_EXPRESSION }
}
