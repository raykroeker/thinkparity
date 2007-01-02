/**
 * Created On: Dec 31, 2006 5:12:59 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.awt.event.ActionEvent;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.LinkAction;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayFlagSeenInfo extends AbstractAction {

    /** The browser application. */
    private final Browser browser;
    
    /** An instance of the link action. */
    private final DisplayFlagSeenInfoLink displayFlagSeenInfoLink;

    /**
     * Create a Create.
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
        final List<Container> containers = getContainerModel().read();
        Long firstNotSeenContainerId = null;
        int countNotSeen = 0;       
        for (final Container container : containers) {
            final Boolean seen = container.isSeen();
            countNotSeen += seen ? 0 : 1;
            if (!seen && countNotSeen==1) {
                firstNotSeenContainerId = container.getId();
            }
        }

        displayFlagSeenInfoLink.setFirstNotSeenContainerId(firstNotSeenContainerId);
        displayFlagSeenInfoLink.setNumberContainersNotSeen(countNotSeen);
        browser.setStatus(displayFlagSeenInfoLink);                   
    }
    
    public class DisplayFlagSeenInfoLink implements LinkAction {
        
        /** The browser application. */
        private final Browser browser;

        /** The container Id */
        private Long firstNotSeenContainerId;
        
        /** The number of containers not seen */
        private int numberContainersNotSeen;
        
        
        public DisplayFlagSeenInfoLink(final Browser browser) {
            super();
            this.browser = browser;
        }
        
        /**
         * Set the container id.
         * 
         * @param containerId
         *            The container Id.
         */
        public void setFirstNotSeenContainerId(final Long firstNotSeenContainerId) {
            this.firstNotSeenContainerId = firstNotSeenContainerId;
        }
        
        /**
         * Set the number of containers not seen.
         * 
         * @param numberContainersNotSeen
         *          The number of containers not seen.
         */
        public void setNumberContainersNotSeen(final int numberContainersNotSeen) {
            this.numberContainersNotSeen = numberContainersNotSeen;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getAction()
         */
        public javax.swing.Action getAction() {
            return new javax.swing.AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    browser.selectTab(MainTitleAvatar.TabId.CONTAINER);
                    browser.showContainer(firstNotSeenContainerId);
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
        public String getIntroText() {
            return localization.getString("ContainerNotSeenIntro");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkType()
         */
        public LinkType getLinkType() {
            if (0 == numberContainersNotSeen) {
                return LinkType.CLEAR_SHOW_ALWAYS;
            } else {
                return LinkType.SHOW_ALWAYS;
            }
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkText()
         */
        public String getLinkText() {
            if (1 == numberContainersNotSeen) {
                return localization.getString("ContainerNotSeenOne", new Object[] {numberContainersNotSeen});
            } else {
                return localization.getString("ContainerNotSeenMany", new Object[] {numberContainersNotSeen});
            }
        }
    }
}
