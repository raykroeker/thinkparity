/*
 * Created On: July 29, 2006, 11:30 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.swing.GradientPainter;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Colors.Browser;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension;
import com.thinkparity.ophelia.browser.platform.util.State;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainTitleAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Used to drag the window by this avatar. */
    private final MouseInputAdapter mouseInputAdapter;

    /** Creates MainTitleAvatar. */
    public MainTitleAvatar() {
        super("BrowserTitle");
        this.mouseInputAdapter = new MouseInputAdapter() {
            int offsetX;
            int offsetY;
            public void mouseDragged(final MouseEvent e) {
                if (!isResizeDragging()) {
                    getController().moveBrowserWindow(
                            new Point(
                                    e.getPoint().x - offsetX,
                                    e.getPoint().y - offsetY));
                }
            }
            public void mousePressed(MouseEvent e) {
                offsetX = e.getPoint().x;
                offsetY = e.getPoint().y;
            }
        };
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
        setResizeEdges(Resizer.FormLocation.MIDDLE);
        initComponents();
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getPluginRegistry()
     */
    @Override
    protected PluginRegistry getPluginRegistry() {
        return super.getPluginRegistry();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadTab();
        reloadProfile();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {}

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
            // Note subtract 10 pixels from height to account for menu bar
            g2.drawImage(Images.BrowserTitle.LOGO,
                    (getWidth() - Images.BrowserTitle.LOGO.getWidth()) / 2,
                    (getHeight() - Images.BrowserTitle.LOGO.getHeight()) / 2 - 10,
                    Images.BrowserTitle.LOGO.getWidth(),
                    Images.BrowserTitle.LOGO.getHeight(), MainTitleAvatar.this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Obtain the input profile.
     * 
     * @return The input profile.
     */
    private Profile getInputProfile() {
        if(null == input) {
            return null;
        } else {
            return (Profile) ((Data) input).get(DataKey.PROFILE);
        }
    }

    /**
     * Obtain the input tab extension.
     * 
     * @return The input tab extension.
     */
    private TabExtension getInputTabExtension() {
        if (null == input) {
            return null;
        } else {
            return (TabExtension) ((Data) input).get(DataKey.TAB_EXTENSION);
        }
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatarSearchPanel searchPanel;

        searchPanel = new com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatarSearchPanel();
        tabPanel = new com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatarTabPanel();

        searchPanel.setMainTitleAvatar(this);

        tabPanel.setMainTitleAvatar(this);
        tabPanel.initPluginTabs(getPluginRegistry());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tabPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(searchPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, searchPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tabPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the profile.
     *
     */
    private void reloadProfile() {
        tabPanel.setProfile(getInputProfile());
    }

    /**
     * Reload the tab.
     *
     */
    private void reloadTab() {
        final TabId tabId = getInputTabId();
        if (null != tabId) {
            tabPanel.selectTab(tabId);
            switch (tabId) {
            case CONTACT:
                getController().displayTabContactAvatar();
                break;
            case CONTAINER:
                getController().displayTabContainerAvatar();
                break;
            default:
                Assert.assertUnreachable("UNKNOWN TAB");
            }
        } else {
            final TabExtension tabExtension = getInputTabExtension();
            if (null != tabExtension) {
                tabPanel.selectTab(tabExtension);
                getController().displayTabExtension(tabExtension);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatarTabPanel tabPanel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, TAB_ID, TAB_EXTENSION }

    public enum TabId { CONTACT, CONTAINER }
}
