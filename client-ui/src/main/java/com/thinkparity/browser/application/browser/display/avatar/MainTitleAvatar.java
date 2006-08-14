/*
 * Created On: July 29, 2006, 11:30 AM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.thinkparity.codebase.swing.GradientPainter;

import com.thinkparity.browser.Constants.Images;
import com.thinkparity.browser.Constants.Colors.Browser;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.profile.Profile;

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
                getController().moveBrowserWindow(
                        new Point(
                                e.getPoint().x - offsetX,
                                e.getPoint().y - offsetY));
            }
            public void mousePressed(MouseEvent e) {
                offsetX = e.getPoint().x;
                offsetY = e.getPoint().y;
            }
        };
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
        initComponents();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() { return AvatarId.MAIN_TITLE; }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() { return null; }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadTab();
        reloadProfile();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
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
                    Browser.MainStatus.BG_GRAD_START,
                    Browser.MainStatus.BG_GRAD_FINISH);
            g2.drawImage(Images.BrowserTitle.LOGO,
                    (getWidth() - Images.BrowserTitle.LOGO.getWidth()) / 2,
                    (getHeight() - Images.BrowserTitle.LOGO.getHeight()) / 2,
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
     * Obtain the input tab.
     * 
     * @return The input tab.
     */
    private MainTitleAvatar.Tab getInputTab() {
        if(null == input) {
            return null;
        } else {
            return (MainTitleAvatar.Tab) ((Data) input).get(DataKey.TAB);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarButtonPanel buttonPanel;
        com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarMenuPanel menuPanel;
        com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarSearchPanel searchPanel;

        buttonPanel = new com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarButtonPanel();
        searchPanel = new com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarSearchPanel();
        menuPanel = new com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarMenuPanel();
        tabPanel = new com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarTabPanel();

        buttonPanel.setMainTitleAvatar(this);

        searchPanel.setMainTitleAvatar(this);

        menuPanel.setMainTitleAvatar(this);

        tabPanel.setMainTitleAvatar(this);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(tabPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(searchPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(menuPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(menuPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(searchPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
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
        tabPanel.setTab(getInputTab());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatarTabPanel tabPanel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, TAB }

    public enum Tab { CONTACT, CONTAINER }
}
