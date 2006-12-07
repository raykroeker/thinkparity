/*
 * EditProfileAvatar.java
 *
 * Created on December 6, 2006, 11:13 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.EditProfileProvider;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * @author robert@thinkparity.com
 */
public class EditProfileAvatar extends Avatar {
    
    /** EditProfileAvatar tabs. */
    private final List<EditProfileAvatarAbstractTabPanel> tabs;

    /** Creates new form EditProfileAvatar */
    public EditProfileAvatar() {
        super("EditProfileDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        tabs = new ArrayList<EditProfileAvatarAbstractTabPanel>();
        tabs.add(new EditProfileAvatarDataTabPanel(getController(), this, localization.getString("DataTabPanelTitle")));
        tabs.add(new EditProfileAvatarPasswordTabPanel(getController(), this, localization.getString("PasswordTabPanelTitle")));
        tabs.add(new EditProfileAvatarEmailTabPanel(getController(), this, localization.getString("EmailTabPanelTitle")));
        for (final EditProfileAvatarAbstractTabPanel tab : tabs) {
            profileJTabbedPane.add(tab.getTabName(), tab);
        }
        bindEscapeKey();
    }
    
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_EDIT;
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }
    
    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            /** @see java.io.Serializable */
            private static final long serialVersionUID = 1;

            /** @see javax.swing.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {        
        final Profile profile = readProfile();
        for (final EditProfileAvatarAbstractTabPanel tab : tabs) {
            tab.reload(profile);
        }
    }

    /**
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        Boolean valid = Boolean.TRUE;
        for (final EditProfileAvatarAbstractTabPanel tab : tabs) {
            if (!tab.isInputValid()) {
                valid = Boolean.FALSE;
                break;
            }
        }
        
        return valid;
    }
    
    /**
     * Read the profile from the content provider.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile readProfile() {
        return ((EditProfileProvider) contentProvider).readProfile();
    }
    
    /**
     * Read the profile email addresses from the content provider.
     * 
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    protected List<ProfileEMail> readEmails() {
        return ((EditProfileProvider) contentProvider).readEmails();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        profileJTabbedPane = new javax.swing.JTabbedPane();
        okJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        okJButton.setText(bundle.getString("EditProfileDialog.OK")); // NOI18N
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("EditProfileDialog.Cancel")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(profileJTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(profileJTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 254, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 20, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }// GEN-LAST:event_cancelJButtonActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            for (final EditProfileAvatarAbstractTabPanel tab : tabs) {
                tab.save();
            }
            disposeWindow();
        }
     }//GEN-LAST:event_okJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JButton okJButton;
    private javax.swing.JTabbedPane profileJTabbedPane;
    // End of variables declaration//GEN-END:variables

}
