/*
 * Created On: Fri Jun 02 2006 16:02 PDT
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

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

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getId");
    }

    /**
     * Obtain the profile name.
     *
     * @return The profile name.
     */
    public String getProfileName() { return profileName; }

    /** @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()*/
    public State getState() {
        throw Assert.createNotYetImplemented("ProfileManagerAvatar#getState");
    }
    
    /** @see com.thinkparity.browser.javax.swing.AbstractJPanel#isInputValid() */
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
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadName();

        profileNameJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     */
    public void setInput(final Object input) {
        Assert.assertNotNull("[NEW PROFILE AVATAR] [NULL INPUT]", input);
        this.input = input;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
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

    /** Dispose of the window. */
    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

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
        javax.swing.JTextArea eaJTextArea;
        javax.swing.JLabel profileNameJLabel;

        eaJTextArea = TextFactory.createArea(
            getString("EmbeddedAssistance",
                new Object[] {System.getProperty("line.separator")}));
        profileNameJLabel = LabelFactory.create(getString("ProfileNameLabel"));
        profileNameJTextField = TextFactory.create();
        createJButton = ButtonFactory.create(getString("CreateButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));

        eaJTextArea.setColumns(20);
        eaJTextArea.setEditable(false);
        eaJTextArea.setLineWrap(true);
        eaJTextArea.setRows(5);
        eaJTextArea.setWrapStyleWord(true);
        eaJTextArea.setOpaque(false);

        createJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                createJButtonActionPerformed(e);
            }
        });

        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, eaJTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 338, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                            .add(cancelJButton)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(createJButton)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(profileNameJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(profileNameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(createJButton)
                    .add(cancelJButton))
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
