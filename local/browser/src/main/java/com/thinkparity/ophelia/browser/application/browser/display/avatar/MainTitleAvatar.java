/*
 * Created On: July 29, 2006, 11:30 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.GradientPainter;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Colors.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.util.State;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainTitleAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Creates MainTitleAvatar. */
    public MainTitleAvatar() {
        super("BrowserTitle");
        initComponents();
        installResizer();
        installMoveListener();
        
        // Double click to maximize the browser window
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (e.getClickCount() % 2 == 0) {
                        getController().maximize();
                    }
                }
            }
        });
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadTab();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getPluginRegistry()
     */
    @Override
    protected PluginRegistry getPluginRegistry() {
        return super.getPluginRegistry();
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        // Default avatar background image is not required for this avatar.
        return Boolean.FALSE;
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
     * Obtain the input tab list extension.
     * 
     * @return The input tab list extension.
     */
    private TabListExtension getInputTabListExtension() {
        if (null == input) {
            return null;
        } else {
            return (TabListExtension) ((Data) input).get(DataKey.TAB_LIST_EXTENSION);
        }
    }

    /**
     * Obtain the input tab extension.
     * 
     * @return The input tab extension.
     */
    private TabPanelExtension getInputTabPanelExtension() {
        if (null == input) {
            return null;
        } else {
            return (TabPanelExtension) ((Data) input).get(DataKey.TAB_PANEL_EXTENSION);
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
                .add(searchPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tabPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, searchPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

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
            final TabListExtension tabListExtension = getInputTabListExtension();
            if (null != tabListExtension) {
                tabPanel.selectTab(tabListExtension);
                getController().displayTabExtension(tabListExtension);
            }
            final TabPanelExtension tabPanelExtension = getInputTabPanelExtension();
            if (null != tabPanelExtension) {
                tabPanel.selectTab(tabPanelExtension);
                getController().displayTabExtension(tabPanelExtension);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatarTabPanel tabPanel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, TAB_ID, TAB_LIST_EXTENSION, TAB_PANEL_EXTENSION }

    public enum TabId { CONTACT, CONTAINER }
}
