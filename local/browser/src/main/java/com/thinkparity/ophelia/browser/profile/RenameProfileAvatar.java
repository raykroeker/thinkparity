/*
 * Created On: Fri Jun 02 2006 16:02 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;


import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class RenameProfileAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The profile name. */
    private String profileName;

    /**
     * Creates new form CreateProfileAvatar
     */
    RenameProfileAvatar(final ProfileManager manager) {
        super("RenameProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Rename", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                renameJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getId");
    }

    /**
     * Obtain the new profile name.
     *
     * @return The new profile name.
     */
    public String getProfileName() { return profileName; }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()*/
    public State getState() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getState");
    }

    /** @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid() */
    public Boolean isInputValid() {
        final String profileName = extractProfileName();
        if(FileUtil.isDirectoryNameValid(profileName)) {
            final List<Profile> profiles = getInputADataAllProfiles();
            for(final Profile profile : profiles) {
                if(profile.getName().equals(profileName)) { return Boolean.FALSE; }
            }
            return Boolean.TRUE;
        }
        else { return Boolean.FALSE; }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadEmbeddedAssistance();
        reloadName();

        profileNameJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     */
    public void setInput(final Object input) {
        Assert.assertNotNull("[NEW PROFILE AVATAR] [NULL INPUT]", input);
        this.input = input;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#setState");
    }

    /**
     * Obtain the dialog title.
     * 
     * @return The title.
     */
    String getTitle() { return getString("Title"); }

    /**
     * The cancel event handler.
     *
     * @param e
     *      The action event.
     */
    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        profileName = null;
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Extract the profile name from the text control.
     *
     * @return The profile name.
     */
    private String extractProfileName() {
        return SwingUtil.extract(profileNameJTextField);
    }

    /**
     * Obtain the all profiles input data.
     * 
     * @return A list of profiles.
     */
    private List<Profile> getInputADataAllProfiles() {
        final List<?> list = (List<?>) ((Data) input).get(DataKey.ALL_PROFILES);
        final List<Profile> profiles = new ArrayList<Profile>();
        for(final Object o : list) { profiles.add((Profile) o); }
        return profiles;
    }

    /**
     * Obtain the selected profile input data.
     * 
     * @return A profile.
     */
    private Profile getInputADataSelectedProfile() {
        return (Profile) ((Data) input).get(DataKey.SELECTED_PROFILE);
    }

    private Profile getInputSelectedProfile() {
        if (null == input) {
            return null;
        } else {
            return (Profile) ((Data) input).get(DataKey.SELECTED_PROFILE);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel profileNameJLabel;
        javax.swing.JButton renameJButton;

        profileNameJLabel = LabelFactory.create(getString("ProfileNameLabel"));
        profileNameJTextField = TextFactory.create();
        renameJButton = ButtonFactory.create(getString("RenameButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));
        eaJLabel = new javax.swing.JLabel();

        profileNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("RenameProfileAvatar.ProfileNameLabel"));

        profileNameJTextField.setText("!Profile name value!");

        renameJButton.setMnemonic('R');
        renameJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("RenameProfileAvatar.RenameButton"));
        renameJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                renameJButtonActionPerformed(e);
            }
        });

        cancelJButton.setMnemonic('a');
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("RenameProfileAvatar.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("RenameProfileAvatar.EmbeddedAssistance"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renameJButton))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(profileNameJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(profileNameJLabel)
                    .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(renameJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /** Reload the embedded assistance control. */
    private void reloadEmbeddedAssistance() {
        eaJLabel.setText("");
        if(null != input) {
            eaJLabel.setText(getString("EmbeddedAssistance",
                    new Object[] {getInputADataSelectedProfile().getName()}));
        }
    }

    /** Reload the name control. */
    private void reloadName() {
        profileNameJTextField.setText("");
        final Profile selectedProfile = getInputSelectedProfile();
        if (null != selectedProfile) {
            profileNameJTextField.setText(selectedProfile.getName());
            profileNameJTextField.selectAll();
        }
    }
    /**
     * Create event handler.
     *
     * @param e
     *      The action event.
     */
    private void renameJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_renameJButtonActionPerformed
        if(isInputValid()) {
            profileName = extractProfileName();
            disposeWindow();
        }
    }//GEN-LAST:event_renameJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel eaJLabel;
    private javax.swing.JTextField profileNameJTextField;
    // End of variables declaration//GEN-END:variables
    
    enum DataKey { ALL_PROFILES, SELECTED_PROFILE }
}
