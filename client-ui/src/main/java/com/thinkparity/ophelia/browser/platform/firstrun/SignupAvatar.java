/*
 * SignupAvatar.java
 *
 * Created on April 1, 2007, 11:14 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 *
 * @author  user
 */
public class SignupAvatar extends Avatar implements SignupDelegate {

    /** A <code>CardLayout</code>. */
    final java.awt.CardLayout cardLayout;

    /** The current <code>SignupPage</code>. */
    private SignupPage currentPage;

    /** Signup cancelled flag <code>Boolean</code>. */
    private Boolean cancelled;

    /** The list of <code>SignupPage</code>s. */
    private final List<SignupPage> signupPages;

    /** The <code>WindowTitle</code>. */
    private WindowTitle windowTitle;

    /** Creates new form SignupAvatar */
    public SignupAvatar() {
        super("SignupAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        signupPages = new ArrayList<SignupPage>();
        cancelled = Boolean.FALSE;
        initComponents();
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Next", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                nextJButtonActionPerformed(e);
            }
        });
        this.cardLayout = new java.awt.CardLayout();
        contentJPanel.setLayout(cardLayout);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate#enableCancelButton(java.lang.Boolean)
     */
    public void enableCancelButton(final Boolean enable) {
        cancelJButton.setEnabled(enable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate#enableNextButton(java.lang.Boolean)
     */
    public void enableNextButton(final Boolean enable) {
        nextJButton.setEnabled(enable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate#getSharedLocalization()
     */
    public Localization getSharedLocalization() {
        return getLocalization();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP;
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("SignupAvatar#getState");
    }

    /**
     * Determine if signup has been cancelled.
     * 
     * @return true if the signup has been cancelled, false otherwise.
     */
    public Boolean isCancelled() {
        return cancelled;
    }

    /**
     * Register a page for the card layout in the signup avatar.
     * 
     * @param signupPage
     *            A <code>SignupPage</code>.
     */
    public void registerPage(final SignupPage signupPage) {
        signupPages.add(signupPage);
        contentJPanel.add((Component)signupPage, signupPage.getPageName());
    }

    /**
     * Set the next page.
     */
    public void setNextPage() {
        setPage(lookupPage(currentPage.getNextPageName()));
        currentPage.reloadData();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("SignupAvatar#setState");
    }

    /**
     * Set the window title.
     */
    public void setWindowTitle(final WindowTitle windowTitle) {
        this.windowTitle = windowTitle;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // These images help to make the rounded corners look good.
        // Note that top left and top right are drawn by the window title.
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_LEFT,
                0,
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(), this);
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_RIGHT,
                getSize().width - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(), this);
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload() */
    public void reload() {
        for (final SignupPage signupPage : signupPages) {
            signupPage.reload();
        }
        setFirstPage();
    }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        cancelled = Boolean.TRUE;
        synchronized (this) {
            notifyAll();
        }
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        contentJPanel = new javax.swing.JPanel();
        prevJButton = ButtonFactory.create();
        nextJButton = ButtonFactory.create();
        cancelJButton = ButtonFactory.create();

        contentJPanel.setOpaque(false);
        javax.swing.GroupLayout contentJPanelLayout = new javax.swing.GroupLayout(contentJPanel);
        contentJPanel.setLayout(contentJPanelLayout);
        contentJPanelLayout.setHorizontalGroup(
            contentJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );
        contentJPanelLayout.setVerticalGroup(
            contentJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        prevJButton.setFont(Fonts.DialogButtonFont);
        prevJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.PrevButton"));
        prevJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevJButtonActionPerformed(evt);
            }
        });

        nextJButton.setFont(Fonts.DialogButtonFont);
        nextJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.NextButton"));
        nextJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.CancelButton"));
        cancelJButton.setPreferredSize(new java.awt.Dimension(69, 23));
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
                .addContainerGap(148, Short.MAX_VALUE)
                .addComponent(prevJButton)
                .addGap(0, 0, 0)
                .addComponent(nextJButton)
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
                    .addComponent(nextJButton)
                    .addComponent(prevJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Find the SignupPage give the page name.
     * 
     * @param pageName
     *            The page name <code>String</code>.
     * @return A <code>SignupPage</code>.
     */
    private SignupPage lookupPage(final String pageName) {
        Assert.assertNotNull("Null page in signup dialog.", pageName);
        SignupPage foundPage = null;
        for (final SignupPage signupPage : signupPages) {
            if (signupPage.getPageName().equals(pageName)) {
                foundPage = signupPage;
            }
        }
        Assert.assertNotNull("Invalid page in signup dialog.", foundPage);
        return foundPage;
    }

    private void nextJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextJButtonActionPerformed
        if (currentPage.isNextOk()) {
            currentPage.saveData();
            if (currentPage.isLastPage()) {
                signupComplete();
            } else {
                setNextPage();
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }//GEN-LAST:event_nextJButtonActionPerformed

    private void prevJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevJButtonActionPerformed
        Assert.assertNotTrue("Invalid first page in signup dialog.", currentPage.isFirstPage());
        setPage(lookupPage(currentPage.getPreviousPageName()));
    }//GEN-LAST:event_prevJButtonActionPerformed

    /**
     * Reload the next button.
     */
    private void reloadNextButton() {
        nextJButton.setText(currentPage.isLastPage() ? getString("FinishButton") : getString("NextButton"));
    }

    /**
     * Reload the previous button.
     */
    private void reloadPrevButton() {
        prevJButton.setVisible(!currentPage.isFirstPage().booleanValue() &&
                null!=currentPage.getPreviousPageName());
    }

    /**
     * Reload the window title.
     */
    private void reloadWindowTitle() {
        Assert.assertNotNull("Null window title in signup dialog.", windowTitle);
        windowTitle.setTitleText(((Avatar)currentPage).getAvatarTitle());
    }

    /**
     * Set the first page in the card layout.
     */
    private void setFirstPage() {
        for (final SignupPage signupPage : signupPages) {
            if (signupPage.isFirstPage()) {
                setPage(signupPage);
                break;
            }
        }
    }

    /**
     * Set the page.
     * 
     * @param page
     *            A <code>SignupPage</code>.
     */
    private void setPage(final SignupPage page) {
        this.currentPage = page;
        cardLayout.show(contentJPanel, page.getPageName());
        reloadNextButton();
        reloadPrevButton();
        reloadWindowTitle();
        page.validateInput();
    }

    /**
     * Sign up.
     */
    private void signupComplete() {
        cancelled = Boolean.FALSE;
        // NOTE The wizard always ends in login. Login is responsible for disposing the window.
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JPanel contentJPanel;
    private javax.swing.JButton nextJButton;
    private javax.swing.JButton prevJButton;
    // End of variables declaration//GEN-END:variables
}
