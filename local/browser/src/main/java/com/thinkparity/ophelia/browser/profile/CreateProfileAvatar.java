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
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class CreateProfileAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The profile name. */
    private String profileName;

    /**
     * Creates new form CreateProfileAvatar
     */
    CreateProfileAvatar(final ProfileManager manager) {
        super("CreateProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Create", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                createJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getId");
    }

    /**
     * Obtain the profile name.
     *
     * @return The profile name.
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
            final List<Profile> profiles = getInputProfiles();
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
     * Create event handler.
     *
     * @param e
     *      The action event.
     */
    private void createJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_createJButtonActionPerformed
        if(isInputValid()) {
            profileName = extractProfileName();
            disposeWindow();
        }
    }//GEN-LAST:event_createJButtonActionPerformed

    /**
     * Extract the profile name from the text control.
     *
     * @return The profile name.
     */
    private String extractProfileName() {
        return SwingUtil.extract(profileNameJTextField);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JButton createJButton;
        javax.swing.JLabel eaJLabel;
        javax.swing.JLabel profileNameJLabel;

        eaJLabel = new javax.swing.JLabel();
        profileNameJLabel = LabelFactory.create(getString("ProfileNameLabel"));
        profileNameJTextField = TextFactory.create();
        createJButton = ButtonFactory.create(getString("CreateButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateProfileAvatar.EmbeddedAssistance"));

        profileNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateProfileAvatar.ProfileNameLabel"));

        createJButton.setMnemonic('C');
        createJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateProfileAvatar.CreateButton"));
        createJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setMnemonic('a');
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateProfileAvatar.CancelButton"));
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
                    .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(profileNameJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(createJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, createJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(profileNameJLabel)
                    .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 18, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(createJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Convert input to a profile list.
     * 
     * @return A list of profiles.
     */
    private List<Profile> getInputProfiles() {
        final List<?> list = (List<?>) input;
        final List<Profile> profiles = new ArrayList<Profile>();
        for(final Object o : list) { profiles.add((Profile) o); }
        return profiles;
    }

    /** Reload the name control. */
    private void reloadName() { profileNameJTextField.setText(""); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField profileNameJTextField;
    // End of variables declaration//GEN-END:variables
}
