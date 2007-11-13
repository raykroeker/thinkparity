/**
 * Created On: 24-May-07 10:12:51 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.help.HelpTabDispatcher;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpTabAvatar extends TabPanelAvatar<HelpTabModel> {

    /**
     * Create HelpTabAvatar.
     *
     */
    public HelpTabAvatar() {
        super(AvatarId.TAB_HELP, new HelpTabModel());
        model.setLocalization(getLocalization());
        model.setSession(getSession());
        setFilterDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent e) {
                if (null != e.getOldValue()) {
                    ((HelpTabDispatcher) e.getOldValue()).removeListeners(
                            HelpTabAvatar.this);
                }
                if (null != e.getNewValue()) {
                    ((HelpTabDispatcher) e.getNewValue()).addListeners(
                            HelpTabAvatar.this);
                }
            }
        });
    }

    /**
     * Collapse the help topic.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     */
    public void collapseHelpTopic(final Long helpTopicId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.collapsePanel(helpTopicId, Boolean.FALSE);
            }
        });
    }

    /**
     * Expand the help topic.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     */
    public void expandHelpTopic(final Long helpTopicId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showPanel(helpTopicId);
            }
        });
    }

    /**
     * Show the help topic panel.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     */
    private void showPanel(final Long helpTopicId) {
        model.selectPanel(helpTopicId);
        model.expandPanel(helpTopicId, Boolean.FALSE);
        model.scrollPanelToVisible(helpTopicId); 
    }  
}
