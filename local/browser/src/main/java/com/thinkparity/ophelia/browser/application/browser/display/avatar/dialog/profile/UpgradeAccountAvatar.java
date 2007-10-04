/*
 * UpgradeAccountAvatar.java
 *
 * Created on July 27, 2007, 4:23 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Upgrade Account Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 */
public class UpgradeAccountAvatar extends Avatar implements
        UpgradeAccountDelegate {

    /** An avatar registry. */
    private final AvatarRegistry avatarRegistry;

    /** A <code>CardLayout</code>. */
    private final java.awt.CardLayout cardLayout;

    /** The current <code>UpgradeAccountPage</code>. */
    private UpgradeAccountPage currentPage;

    /** The list of <code>UpgradeAccountPage</code>s. */
    private final List<UpgradeAccountPage> upgradeAccountPages;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton cancelJButton = ButtonFactory.create();
    private final javax.swing.JPanel contentJPanel = new javax.swing.JPanel();
    private final javax.swing.JButton nextJButton = ButtonFactory.create();
    private final javax.swing.JButton prevJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** Creates new form UpgradeAccountAvatar */
    public UpgradeAccountAvatar() {
        super("UpgradeAccountAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.avatarRegistry = new AvatarRegistry();
        this.cardLayout = new java.awt.CardLayout();
        this.upgradeAccountPages = new ArrayList<UpgradeAccountPage>();
        initComponents();
        bindKeys();
        contentJPanel.setLayout(cardLayout);
        initUpgradeAccountAvatar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountDelegate#enableNextButton(java.lang.Boolean)
     */
    public void enableNextButton(final Boolean enable) {
        nextJButton.setEnabled(enable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT;
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("UpgradeAccountAvatar#getState");
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload() */
    public void reload() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                for (final UpgradeAccountPage page : upgradeAccountPages) {
                    page.reload();
                }
                setFirstPage();  
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("UpgradeAccountAvatar#setState");
    }

    /**
     * Bind keys to actions.
     */
    private void bindKeys() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Next", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                if (cancelJButton.isFocusOwner()) {
                    cancelJButtonActionPerformed(e);
                } else if (prevJButton.isFocusOwner()) {
                    prevJButtonActionPerformed(e);
                } else {
                    nextJButtonActionPerformed(e);
                }
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Check the registry for the avatar; if it does not exist create it;
     * otherwise just return it.
     * 
     * @param id
     *            The avatar id.
     * @return The avatar.
     */
    private Avatar getAvatar(final AvatarId id) {
        if (avatarRegistry.contains(id)) {
            return avatarRegistry.get(id);
        } else {
            return AvatarFactory.create(id);
        }
    }

    /**
     * Get the upgrade account page.
     * 
     * @param id
     *            The <code>AvatarId</code>.
     * @return The <code>UpgradeAccountPage</code>.
     */
    private UpgradeAccountPage getUpgradeAccountPage(final AvatarId id) {
        final Avatar avatar = getAvatar(id);
        ((UpgradeAccountPage)avatar).setUpgradeAccountDelegate(this);
        return (UpgradeAccountPage)avatar;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentJPanel.setOpaque(false);
        javax.swing.GroupLayout contentJPanelLayout = new javax.swing.GroupLayout(contentJPanel);
        contentJPanel.setLayout(contentJPanelLayout);
        contentJPanelLayout.setHorizontalGroup(
            contentJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        contentJPanelLayout.setVerticalGroup(
            contentJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        prevJButton.setFont(Fonts.DialogButtonFont);
        prevJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.PrevButton"));
        prevJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevJButtonActionPerformed(evt);
            }
        });

        nextJButton.setFont(Fonts.DialogButtonFont);
        nextJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.NextButton"));
        nextJButton.setPreferredSize(new java.awt.Dimension(85, 23));
        nextJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.CancelButton"));
        cancelJButton.setPreferredSize(new java.awt.Dimension(85, 23));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addComponent(prevJButton)
                .addGap(0, 0, 0)
                .addComponent(nextJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cancelJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(contentJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, nextJButton, prevJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(contentJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prevJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the upgrade account avatar pages.
     */
    private void initUpgradeAccountAvatar() {
        registerPage(getUpgradeAccountPage(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_INTRO));
        registerPage(getUpgradeAccountPage(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_AGREEMENT));
        registerPage(getUpgradeAccountPage(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_PAYMENT));
        registerPage(getUpgradeAccountPage(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_SUMMARY));
    }

    /**
     * Find the UpgradeAccountPage given the page name.
     * 
     * @param pageName
     *            The page name <code>String</code>.
     * @return An <code>UpgradeAccountPage</code>.
     */
    private UpgradeAccountPage lookupPage(final String pageName) {
        Assert.assertNotNull("Null page in upgrade account dialog.", pageName);
        UpgradeAccountPage foundPage = null;
        for (final UpgradeAccountPage page : upgradeAccountPages) {
            if (page.getPageName().equals(pageName)) {
                foundPage = page;
            }
        }
        Assert.assertNotNull("Invalid page in upgrade account dialog.", foundPage);
        return foundPage;
    }

    private void nextJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextJButtonActionPerformed
        if (currentPage.isNextOk()) {
            if (currentPage.isLastPage()) {
                disposeWindow();
            } else {
                setPage(lookupPage(currentPage.getNextPageName()));
            }
        }
    }//GEN-LAST:event_nextJButtonActionPerformed

    private void prevJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevJButtonActionPerformed
        Assert.assertNotTrue("Invalid first page in upgrade account dialog.", currentPage.isFirstPage());
        setPage(lookupPage(currentPage.getPreviousPageName()));
    }//GEN-LAST:event_prevJButtonActionPerformed

    /**
     * Register a page for the card layout in the upgrade account avatar.
     * 
     * @param upgradeAccountPage
     *            An <code>UpgradeAccountPage</code>.
     */
    private void registerPage(final UpgradeAccountPage upgradeAccountPage) {
        upgradeAccountPages.add(upgradeAccountPage);
        contentJPanel.add((Component)upgradeAccountPage, upgradeAccountPage.getPageName());
    }

    /**
     * Reload the cancel button.
     */
    private void reloadCancelButton() {
        cancelJButton.setVisible(!currentPage.isLastPage().booleanValue());
    }

    /**
     * Reload the next button.
     */
    private void reloadNextButton() {
        if (null != currentPage.getNextButtonTextKey()) {
            nextJButton.setText(getString(currentPage.getNextButtonTextKey()));
        } else {
            nextJButton.setText(getString("NextButton"));
        }
    }

    /**
     * Reload the previous button.
     */
    private void reloadPrevButton() {
        prevJButton.setVisible(!currentPage.isFirstPage().booleanValue() &&
                !currentPage.isLastPage().booleanValue() &&
                null!=currentPage.getPreviousPageName());
    }

    /**
     * Set the first page in the card layout.
     */
    private void setFirstPage() {
        for (final UpgradeAccountPage page : upgradeAccountPages) {
            if (page.isFirstPage()) {
                setPage(page);
                break;
            }
        }
    }

    /**
     * Set the page.
     * 
     * @param page
     *            An <code>UpgradeAccountPage</code>.
     */
    private void setPage(final UpgradeAccountPage page) {
        this.currentPage = page;
        cardLayout.show(contentJPanel, page.getPageName());
        reloadNextButton();
        reloadPrevButton();
        reloadCancelButton();
        page.refresh();
        page.validateInput();
        page.setDefaultFocus();
    }
}
