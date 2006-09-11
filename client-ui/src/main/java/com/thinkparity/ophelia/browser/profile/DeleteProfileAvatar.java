/*
 * Created On: Fri Jun 02 2006 16:02 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.profile;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.assertion.Assert;


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
class DeleteProfileAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** A flag indicating whether or not to delete. */
    private Boolean delete;

    /** Create DeleteProfileAvatar */
    DeleteProfileAvatar(final ProfileManager manager) {
        super("DeleteProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() {
        throw Assert.createNotYetImplemented("DeleteProfileAvatar#getId");
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()*/
    public State getState() {
        throw Assert.createNotYetImplemented("DeleteProfileAvatar#getState");
    }

    /**
     * Obtain the avatar title.
     * 
     * @return The title.
     */
    public String getTitle() { return getString("Title"); }

    /** @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid() */
    public Boolean isInputValid() { return Boolean.TRUE; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    public void reload() {
        reloadName();

        cancelJButton.requestFocusInWindow();
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
     * Whether or not to delete.
     * 
     * @return True if the user indicated deletion; false otherwise.
     */
    Boolean doDelete() { return delete; }

    /**
     * The cancel event handler.
     *
     * @param e
     *      The action event.
     */
    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        delete = Boolean.FALSE;
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Create event handler.
     *
     * @param e
     *      The action event.
     */
    private void deleteJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_deleteJButtonActionPerformed
        if(isInputValid()) {
            delete = Boolean.TRUE;
            disposeWindow();
        }
    }//GEN-LAST:event_deleteJButtonActionPerformed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton deleteJButton;
        javax.swing.JLabel eaJLabel;

        eaJLabel = new javax.swing.JLabel();
        profileNameJLabel = LabelFactory.create(getString("ProfileNameLabel"));
        profileNameJTextField = TextFactory.create();
        deleteJButton = ButtonFactory.create(getString("DeleteButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));

        eaJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("DeleteProfileAvatar.EmbeddedAssistance"));

        profileNameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("DeleteProfileAvatar.ProfileNameLabel"));

        profileNameJTextField.setText("!Profile name value!");
        profileNameJTextField.setBorder(null);
        profileNameJTextField.setOpaque(false);

        deleteJButton.setMnemonic('D');
        deleteJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("DeleteProfileAvatar.DeleteButton"));
        deleteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteJButtonActionPerformed(e);
            }
        });

        cancelJButton.setMnemonic('a');
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("DeleteProfileAvatar.CancelButton"));
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
                    .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(profileNameJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(profileNameJLabel)
                    .add(profileNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(deleteJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /** Reload the name control. */
    private void reloadName() {
        profileNameJTextField.setText("");
        if(null != input) {
            final Profile profile = (Profile) input;
            profileNameJTextField.setText(profile.getName());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel profileNameJLabel;
    private javax.swing.JTextField profileNameJTextField;
    // End of variables declaration//GEN-END:variables
}
