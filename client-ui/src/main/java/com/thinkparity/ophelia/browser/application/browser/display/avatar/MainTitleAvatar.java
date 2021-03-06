/*
 * Created On: July 29, 2006, 11:30 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.events.ProfileEvent;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Colors.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.event.MainTitleDispatcher;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Main Title Avatar<br>
 * <b>Description:</b>The main title avatar contains a search panel and a tab
 * panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * 
 * NOTE:  Follow the instantiation of the main title avatar to find the error
 * with the netbeans RAD ui.
 */
public final class MainTitleAvatar extends Avatar {

    /** A <code>MainTitleAvatarSearchPanel</code>.*/
    private final MainTitleAvatarSearchPanel searchPanel;

    /** A <code>MainTitleAvatarTabPanel</code>.*/
    private final MainTitleAvatarTabPanel tabPanel;

    /**
     * Create MainTitleAvatar.
     * 
     */
    public MainTitleAvatar() {
        super("BrowserTitle");
        this.searchPanel = new MainTitleAvatarSearchPanel();
        this.searchPanel.setMainTitleAvatar(this);
        this.tabPanel = new MainTitleAvatarTabPanel();
        this.tabPanel.setMainTitleAvatar(this);

        initComponents();
        installResizer();
        installMoveListener();
        new FocusHelper().addFocusListener(this);

        // double click to maximize the browser window
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (e.getClickCount() % 2 == 0) {
                        getController().maximize(!getController().isBrowserWindowMaximized());
                    }
                }
            }
        });

        addPropertyChangeListener("eventDispatcher", new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if (null != evt.getOldValue())
                    ((MainTitleDispatcher) evt.getOldValue()).removeListeners(
                            MainTitleAvatar.this);
                if (null != evt.getNewValue())
                    ((MainTitleDispatcher) evt.getNewValue()).addListeners(
                            MainTitleAvatar.this);
            }
        });
    }

    /**
     * Fire a profile e-mail event.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    public void fireProfileEMailEvent(final ProfileEvent e) {
        reload();
    }

    /**
     * Fire a profile event.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    public void fireProfileEvent(final Profile profile) {
        reload();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() { return AvatarId.MAIN_TITLE; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() { return null; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        // Default avatar background image is not required for this avatar.
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                reloadTab();   
            }
        });
    }

    /**
     * Request focus in the search control.
     */
    public void requestFocusInSearch() {
        searchPanel.requestFocus();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {}

    /**
     * Show all tab panels, ie. clear filter and search on the tab.
     */
    public void showAllTabPanels() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                searchPanel.clearSearch();
                searchPanel.clearFilter();  
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.MIDDLE;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    Browser.MainTitle.BG_GRAD_START,
                    Browser.MainTitle.BG_GRAD_FINISH);
            
            // Draw the logo. Subtract some pixels from height to account for menu bar.
            g2.drawImage(Images.BrowserTitle.LOGO,
                    (getWidth() - Images.BrowserTitle.LOGO.getWidth()) / 2,
                    (getHeight() - Images.BrowserTitle.LOGO.getHeight()) / 2 - 15,
                    Images.BrowserTitle.LOGO.getWidth(),
                    Images.BrowserTitle.LOGO.getHeight(), MainTitleAvatar.this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Obtain the input tab.
     * 
     * @return The input tab.
     */
    private TabId getInputTabId() {
        if (null == input) {
            return null;
        } else {
            return (TabId) ((Data) input).get(DataKey.TAB_ID);
        }
    }

    /**
     * Initialize components.
     */
    private void initComponents() {
        final javax.swing.JLabel midFillerJLabel = new javax.swing.JLabel();;
        final javax.swing.JLabel topFillerJLabel = new javax.swing.JLabel();;
        java.awt.GridBagConstraints gridBagConstraints;
        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        add(tabPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(midFillerJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        add(searchPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(topFillerJLabel, gridBagConstraints);
    }

    /**
     * Reload the tab.
     *
     */
    private void reloadTab() {
        final TabId tabId = getInputTabId();
        if (null != tabId) {
            if (tabPanel.isTabSelected(tabId)) {
                tabPanel.reload();
            } else {
                tabPanel.selectTab(tabId);
                switch (tabId) {
                case CONTACT:
                    getController().displayContactTabAvatar();
                    break;
                case CONTAINER:
                    getController().displayContainerTabAvatar();
                    break;
                case HELP:
                    getController().displayHelpTabAvatar();
                    break;
                default:
                    Assert.assertUnreachable("UNKNOWN TAB");
                }
                searchPanel.clearSearch();
                searchPanel.reloadTabFilter(tabId);
            }
        }
    }

    public enum DataKey { PROFILE, TAB_ID }
    public enum TabId { CONTACT, CONTAINER, HELP }
}
