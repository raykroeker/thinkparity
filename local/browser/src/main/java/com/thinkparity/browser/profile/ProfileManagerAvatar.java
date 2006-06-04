/*
 * Created On: June 2, 2006, 3:03 PM
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

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

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getId");
    }
    
    /**
     * Obtain the selected profile.
     * 
     * @return A profile.
     */
    public Profile getSelectedProfile() { return selectedProfile; }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()*/
    public State getState() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getState");
    }

    /**
     * Obtain the avatar title.
     * 
     * @return The title.
     */
    String getTitle() { return getString("Title"); }

    /** @see com.thinkparity.browser.javax.swing.AbstractJPanel#isInputValid() */
    public Boolean isInputValid() {
        final Profile selectedProfile = extractSelectedProfile();
        if(null == selectedProfile) { return Boolean.FALSE; }
        else { return Boolean.TRUE; }
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadProfiles();

        profileJList.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     */
    public void setInput(final Object input) {
        Assert.assertNotNull("[PROFILE MANAGER] [NULL MANAGER INPUT]", input);
        this.input = input;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
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

    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

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
        javax.swing.JTextArea eaJTextArea;
        javax.swing.JButton exitJButton;
        javax.swing.JButton newJButton;
        javax.swing.JScrollPane profileJScrollPane;
        javax.swing.JButton renameJButton;
        javax.swing.JButton startJButton;

        eaJTextArea = TextFactory.createArea(getString("EmbeddedAssistance"));
        profileJScrollPane = ScrollPaneFactory.create();
        profileJList = ListFactory.create();
        startJButton = ButtonFactory.create(getString("StartButton"));
        exitJButton = ButtonFactory.create(getString("ExitButton"));
        newJButton = ButtonFactory.create(getString("NewButton"));
        renameJButton = ButtonFactory.create(getString("RenameButton"));
        deleteJButton = ButtonFactory.create(getString("DeleteButton"));

        eaJTextArea.setColumns(20);
        eaJTextArea.setEditable(false);
        eaJTextArea.setLineWrap(true);
        eaJTextArea.setRows(5);
        eaJTextArea.setWrapStyleWord(true);
        eaJTextArea.setBorder(null);
        eaJTextArea.setFocusable(false);
        eaJTextArea.setOpaque(false);

        profileJList.setCellRenderer(new ProfileListCellRenderer());
        profileJList.setModel(profileModel);
        profileJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                profileJListMouseClicked(e);
            }
        });

        profileJScrollPane.setViewportView(profileJList);

        startJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startJButtonActionPerformed(e);
            }
        });

        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                exitJButtonActionPerformed(e);
            }
        });

        newJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                newJButtonActionPerformed(e);
            }
        });

        renameJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                renameJButtonActionPerformed(e);
            }
        });

        deleteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(newJButton)
                    .add(renameJButton)
                    .add(deleteJButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(profileJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 295, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(175, Short.MAX_VALUE)
                .add(exitJButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(startJButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(profileJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(startJButton)
                            .add(exitJButton)))
                    .add(layout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(newJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renameJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteJButton)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        if(null != input) {
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
