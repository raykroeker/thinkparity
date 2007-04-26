/*
 * Created On:  2007-04-25 14:47
 */
package com.thinkparity.ophelia.browser.platform.firewall;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.firewall.FirewallAccessException;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

import org.jdesktop.jdic.desktop.DesktopException;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Firewall Access Error<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class FirewallAccessErrorAvatar extends Avatar {

    /** A <cod>FirewallHelper</code>. */
    private FirewallHelper firewallHelper;

    /**
     * Create FirewallAccessErrorAvatar.
     *
     */
    public FirewallAccessErrorAvatar() {
        super(AvatarId.DIALOG_PLATFORM_FIREWALL_ACCESS_ERROR);
        initComponents();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     *
     */
    @Override
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_FIREWALL_ACCESS_ERROR;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     *
     */
    @Override
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     *
     */
    @Override
    public void setState(final State state) {
    }

    /**
     * Set the firewall helper.
     * 
     * @param firewallHelper
     *            A <code>FirewallHelper</code>.
     */
    void setFirewallHelper(final FirewallHelper firewallHelper) {
        this.firewallHelper = firewallHelper;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel welcomeJLabel = LabelFactory.create(Fonts.DialogFontBold);
        final javax.swing.JLabel messageJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel learnMoreJLabel = LabelFactory.createLink("", Fonts.DialogFont);
        final javax.swing.JButton continueJButton = ButtonFactory.create(Fonts.DialogButtonFont);
        final javax.swing.JButton cancelJButton = new javax.swing.JButton();

        welcomeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("FirewallAccessErrorAvatar.WelcomeLabel"));

        messageJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("FirewallAccessErrorAvatar.MessageLabel"));

        learnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("FirewallAccessErrorAvatar.LearnMoreLabel"));
        learnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                learnMoreJLabelMousePressed(e);
            }
        });

        continueJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("FirewallAccessErrorAvatar.ContinueButton"));
        continueJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                continueJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("FirewallAccessErrorAvatar.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(learnMoreJLabel)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(messageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                            .addComponent(welcomeJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(50, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(245, Short.MAX_VALUE)
                .addComponent(continueJButton)
                .addGap(17, 17, 17)
                .addComponent(cancelJButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(welcomeJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(learnMoreJLabel)
                .addGap(115, 115, 115)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(continueJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void learnMoreJLabelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_learnMoreJLabelMousePressed
        try {
            DesktopUtil.browse("http://thinkparity.com/help/topic/windows_xp_firewall");
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open Learn More web page", dx);
        }
    }//GEN-LAST:event_learnMoreJLabelMousePressed

    private void continueJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_continueJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
        }
    }//GEN-LAST:event_continueJButtonActionPerformed

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    protected void validateInput() {
        super.validateInput();
        try {
            firewallHelper.addFirewallRules();
        } catch (final FirewallAccessException fax) {
            addInputError("");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
