/*
 * Created on June 10, 2006, 12:00 PM
 * $Id$
 */

package com.thinkparity.browser.platform.firstrun;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.model.parity.model.user.UserEmail;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserProfileAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Obtain a logging api id.
     * 
     * @param api
     *            The api.
     * @return A logging api id.
     */
    private static StringBuffer getApiId(final String api) {
        return getAvatarId(AvatarId.USER_PROFILE).append(" ").append(api);
    }

    private String email;

    private String fullName;

    private String organization;

    /** Creates new form UserProfileAvatar */
    UserProfileAvatar(final FirstRunHelper firstRun) {
        super("UserProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEnterKey("Finish", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                finishJButtonActionPerformed(e);
            }
        });
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() { return AvatarId.USER_PROFILE; }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("UserProfileAvatar#getState");
    }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload() */
    public void reload() {
        reloadName();
        reloadEMail();
        reloadOrganization();
    }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object) */
    public void setInput(final Object input) {
        Assert.assertNotNull(getApiId("[SET INPUT]"), input);
        Assert.assertOfType(getApiId("[SET INPUT]"), Contact.class, input);
        this.input = input;
    }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State) */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("UserProfileAvatar#setState");
    }

    String getEmail() { return email; }

    String getFullName() { return fullName; }

    String getOrganization() { return organization; }

    String getTitle() { return getString("Title"); }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        fullName = email = organization = null;
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    private String extractEMail() { return SwingUtil.extract(emailJTextField); }

    private String extractFullName() { return SwingUtil.extract(nameJTextField); }

    private String extractOrganization() { return SwingUtil.extract(organizationJTextField); }

    private void finishJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_finishJButtonActionPerformed
        if(isInputValid()) {
            fullName = extractFullName();
            email = extractEMail();
            organization = extractOrganization();
            disposeWindow();
        }
    }//GEN-LAST:event_finishJButtonActionPerformed

    private String getInputUserEmail() {
        final UserEmail email = ((Contact) input).getEmails().get(0);
        if(null == email) { return null; }
        else { return email.getEmail(); }
    }

    private String getInputUserName() { return ((User) input).getName(); }

    private String getInputUserOrganization() {
        return ((User) input).getOrganization();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel eaJLabel;
        javax.swing.JLabel emailJLabel;
        javax.swing.JButton finishJButton;
        javax.swing.JLabel nameJLabel;
        javax.swing.JLabel organizationJLabel;
        javax.swing.JPanel userInfoJPanel;

        userInfoJPanel = new javax.swing.JPanel();
        nameJLabel = LabelFactory.create();
        emailJLabel = LabelFactory.create();
        organizationJLabel = LabelFactory.create();
        eaJLabel = new javax.swing.JLabel();
        nameJTextField = TextFactory.create();
        emailJTextField = TextFactory.create();
        organizationJTextField = TextFactory.create();
        finishJButton = ButtonFactory.create(getString("SaveButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));

        userInfoJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.UserInfoTitle")));
        userInfoJPanel.setOpaque(false);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.NameLabel"));

        emailJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.EMailLabel"));

        organizationJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.OrganizationLabel"));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.EmbeddedAssistance"));

        finishJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.SaveButton"));
        finishJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                finishJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("UserProfileAvatar.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout userInfoJPanelLayout = new org.jdesktop.layout.GroupLayout(userInfoJPanel);
        userInfoJPanel.setLayout(userInfoJPanelLayout);
        userInfoJPanelLayout.setHorizontalGroup(
            userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(userInfoJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(nameJLabel)
                    .add(emailJLabel)
                    .add(organizationJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .add(organizationJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                .addContainerGap())
            .add(userInfoJPanelLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap(10, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, userInfoJPanelLayout.createSequentialGroup()
                .addContainerGap(191, Short.MAX_VALUE)
                .add(cancelJButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(finishJButton)
                .addContainerGap())
        );
        userInfoJPanelLayout.setVerticalGroup(
            userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(userInfoJPanelLayout.createSequentialGroup()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJLabel)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailJLabel)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(organizationJLabel)
                    .add(organizationJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(userInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(finishJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(userInfoJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(userInfoJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reloadEMail() {
        emailJTextField.setText("");
        if(null != input) { emailJTextField.setText(getInputUserEmail()); }
    }
    private void reloadName() {
        nameJTextField.setText("");
        if(null != input) { nameJTextField.setText(getInputUserName()); }
    }
    private void reloadOrganization() {
        organizationJTextField.setText("");
        if(null != input) {
            organizationJTextField.setText(getInputUserOrganization());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailJTextField;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JTextField organizationJTextField;
    // End of variables declaration//GEN-END:variables
}
