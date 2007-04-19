/*
 * Created On: June 2, 2006, 3:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.ListFactory;
import com.thinkparity.ophelia.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ProfileManagerAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The profile manager. */
    private final ProfileManager manager;

    /** The profile list model. */
    private final DefaultListModel profileModel;

    /** The selected profile. */
    private Profile selectedProfile;

    /**
     * Create ProfileManagerAvatar
     */
    ProfileManagerAvatar(final ProfileManager manager) {
        super("ProfileManagerAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.manager = manager;
        this.profileModel = new DefaultListModel();
        initComponents();
        bindEnterKey("Start", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                startJButtonActionPerformed(e);
            }
        });
        bindEscapeKey("Exit", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                exitJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getId");
    }
    
    /**
     * Obtain the selected profile.
     * 
     * @return A profile.
     */
    public Profile getSelectedProfile() { return selectedProfile; }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()*/
    public State getState() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getState");
    }

    /**
     * Obtain the avatar title.
     * 
     * @return The title.
     */
    String getTitle() { return getString("Title"); }

    /** @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid() */
    public Boolean isInputValid() {
        final Profile selectedProfile = extractSelectedProfile();
        if(null == selectedProfile) { return Boolean.FALSE; }
        else { return Boolean.TRUE; }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadProfiles();

        profileJList.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     */
    public void setInput(final Object input) {
        Assert.assertNotNull("[PROFILE MANAGER] [NULL MANAGER INPUT]", input);
        this.input = input;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#setState");
    }

    /**
     * Reload and maintain the profile selection.
     * 
     * @param profile
     *            A profile.
     */
    void reload(final Profile profile) {
        reload();
        profileJList.setSelectedValue(profile, true);
    }

    /**
     * The delete button event handler.
     * 
     * @param e
     *            The action event.
     */
    private void deleteJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_deleteJButtonActionPerformed
        if(isInputValid()) {
            selectedProfile = extractSelectedProfile();
            manager.delete();
        }
    }//GEN-LAST:event_deleteJButtonActionPerformed

    private void exitJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_exitJButtonActionPerformed
        selectedProfile = null;
        disposeWindow();
    }//GEN-LAST:event_exitJButtonActionPerformed

    private Profile extractSelectedProfile() {
        if(profileJList.isSelectionEmpty()) { return null; }
        else { return (Profile) profileJList.getSelectedValue(); }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton deleteJButton;
        javax.swing.JLabel eaJLabel;
        javax.swing.JButton exitJButton;
        javax.swing.JButton newJButton;
        javax.swing.JScrollPane profileJScrollPane;
        javax.swing.JButton renameJButton;
        javax.swing.JButton startJButton;

        eaJLabel = new javax.swing.JLabel();
        profileJScrollPane = ScrollPaneFactory.create();
        profileJList = ListFactory.create();
        startJButton = ButtonFactory.create(getString("StartButton"));
        exitJButton = ButtonFactory.create(getString("ExitButton"));
        renameJButton = ButtonFactory.create(getString("RenameButton"));
        newJButton = ButtonFactory.create(getString("NewButton"));
        deleteJButton = ButtonFactory.create(getString("DeleteButton"));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.EmbeddedAssistance"));

        profileJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        profileJList.setCellRenderer(new ProfileListCellRenderer());
        profileJList.setModel(profileModel);
        profileJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profileJListMouseClicked(evt);
            }
        });

        profileJScrollPane.setViewportView(profileJList);

        startJButton.setMnemonic('S');
        startJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.StartButton"));
        startJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startJButtonActionPerformed(evt);
            }
        });

        exitJButton.setMnemonic('x');
        exitJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.ExitButton"));
        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJButtonActionPerformed(evt);
            }
        });

        renameJButton.setMnemonic('R');
        renameJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.RenameButton"));
        renameJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameJButtonActionPerformed(evt);
            }
        });

        newJButton.setMnemonic('N');
        newJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.NewButton"));
        newJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newJButtonActionPerformed(evt);
            }
        });

        deleteJButton.setMnemonic('D');
        deleteJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ProfileManagerAvatar.DeleteButton"));
        deleteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(deleteJButton)
                            .add(newJButton)
                            .add(renameJButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(startJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(exitJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {exitJButton, startJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {deleteJButton, newJButton, renameJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(exitJButton)
                            .add(startJButton)))
                    .add(layout.createSequentialGroup()
                        .add(25, 25, 25)
                        .add(newJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renameJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteJButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * The rename action handler.
     * 
     * @param e
     *            The action event.
     */
    private void renameJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_renameJButtonActionPerformed
        if(isInputValid()) {
            selectedProfile = extractSelectedProfile();
            manager.rename();
        }
    }//GEN-LAST:event_renameJButtonActionPerformed

    /**
     * The profile list mouse handler.
     * 
     * @param e
     *            The mouse event.
     */
    private void profileJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_profileJListMouseClicked
        if(2 == e.getClickCount()) {
            final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(),
                    "Start", e.getWhen(), e.getModifiers());
            startJButtonActionPerformed(ae);
        }
    }//GEN-LAST:event_profileJListMouseClicked

    /**
     * Convert input to a profile list.
     * 
     * @return A list of profiles.
     */
    private List<Profile> getInputProfiles() {
        final List<?> list = (List<?>) input;
        final List<Profile> profiles = new ArrayList<Profile>();
        for(final Object o : list) { profiles.add((Profile) o); }
        Collections.sort(profiles);
        return profiles;
    }

    /**
     * Load the model with a list of profiles.
     * 
     * @param model
     *            A list model.
     * @param profiles
     *            A list of profiles.
     */
    private void loadProfileList(final DefaultListModel model,
            final List<Profile> profiles, final Profile selectedProfile) {
        for(final Profile profile : profiles) { model.addElement(profile); }
        if(null != selectedProfile) {
            if(profiles.contains(selectedProfile)) {
                profileJList.setSelectedValue(selectedProfile, true);
            }
            else {
                if(!model.isEmpty()) { profileJList.setSelectedIndex(0); }
            }
        }
        else {
            if(!model.isEmpty()) { profileJList.setSelectedIndex(0); }
        }
    }

    /**
     * The new button event handler.
     * 
     * @param e
     *            The action event.
     */
    private void newJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_newJButtonActionPerformed
        manager.create();
    }//GEN-LAST:event_newJButtonActionPerformed

    /** Reload the profile list. */
    private void reloadProfiles() {
        final Profile selectedProfile = (Profile) profileJList.getSelectedValue();
        profileModel.clear();
        if (null != input) {
            loadProfileList(profileModel, getInputProfiles(), selectedProfile);
        }
    }

    /**
     * The start button event handler.
     * 
     * @param e
     *            The action event.
     */
    private void startJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_startJButtonActionPerformed
        if(isInputValid()) {
            selectedProfile = extractSelectedProfile();
            disposeWindow();
        }
    }//GEN-LAST:event_startJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList profileJList;
    // End of variables declaration//GEN-END:variables
}
