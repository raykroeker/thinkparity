/*
 * Created On: April 7, 2006, 6:25 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.application.dialog;


import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * A generic confirmation dialog that uses a popup window to display a
 * confirmation (yes/no) message to obtain user input.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ConfirmDialog extends Avatar {

    /** The confirm action default key. */
    private static final String CONFIRM_ACTION_DEFAULT_KEY = "ConfirmAction.Default";

    /** The deny action default key. */
    private static final String DENY_ACTION_DEFAULT_KEY = "DenyAction.Default";

    /** Flag indicating whether or not the user confirmed. */
    private Boolean confirm;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmJButton;
    private final javax.swing.JLabel confirmJLabel = new javax.swing.JLabel();
    private javax.swing.JButton denyJButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Creates new form ConfirmDialog
     * 
     */
    public ConfirmDialog() {
        super("ConfirmDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * Determine whether or not the user confirmed.
     * 
     * @return True if the user confirmed; false otherwise.
     */
    public Boolean didConfirm() {
        return confirm;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_CONFIRM;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     * 
     */
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadConfirmMessage();
        reloadConfirmAction();
        reloadDenyAction();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {}
    
    /**
     * Run the confirm action.
     * 
     * @param e
     *            The confirm action event.
     */
    private void confirmJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_confirmJButtonActionPerformed
        confirm = Boolean.TRUE;
        disposeWindow();
    }//GEN-LAST:event_confirmJButtonActionPerformed
    
    /**
     * Run the deny action.
     * 
     * @param e
     *            The deny action event.
     */
    private void denyJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_denyJButtonActionPerformed
        confirm = Boolean.FALSE;
        disposeWindow();
    }//GEN-LAST:event_denyJButtonActionPerformed

    /**
     * Obtain the confirmation action key from the input data.
     * 
     * @return The confirmation action key.
     */
    private String getConfirmActionKey() {
        final String confirmKey =
            (String) ((Data) input).get(DataKey.CONFIRM_ACTION_KEY);
        if(null == confirmKey) { return CONFIRM_ACTION_DEFAULT_KEY; }
        else { return confirmKey; }
    }

    /**
     * Obtain a confirm message.
     * 
     * @return A confirm message <code>String</code>.
     */
    private String getLocalizedConfirmMessage() {
        return (String) ((Data) input).get(DataKey.LOCALIZED_MESSAGE);
    }

    /**
     * Obtain the confirmation arguments from the input data.
     * 
     * @return The confirmation message arguments.
     */
    private Object[] getConfirmMessageArguments() {
        return (Object[]) ((Data) input).get(DataKey.MESSAGE_ARGUMENTS);
    }

    /**
     * Obtain the confirmation message key from the input data.
     * 
     * @return The confirmation message key.
     */
    private String getConfirmMessageKey() {
        return (String) ((Data) input).get(DataKey.MESSAGE_KEY);
    }

    /**
     * Obtain the deny action key from the input data.
     * 
     * @return The confirmation action key.
     */
    private String getDenyActionKey() {
        final String denyActionKey =
            (String) ((Data) input).get(DataKey.DENY_ACTION_KEY);
        if (null == denyActionKey) {
            return DENY_ACTION_DEFAULT_KEY;
        } else {
            return denyActionKey;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        confirmJButton = ButtonFactory.create(getString("ConfirmButton"));
        denyJButton = ButtonFactory.create(getString("DenyButton"));

        confirmJLabel.setText("!Are you sure?!");

        confirmJButton.setText("!Yes!");
        confirmJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                confirmJButtonActionPerformed(e);
            }
        });

        denyJButton.setText("!No!");
        denyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                denyJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, confirmJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(denyJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(confirmJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {confirmJButton, denyJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(confirmJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(confirmJButton)
                    .add(denyJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the confirm action (jbutton).
     *
     */
    private void reloadConfirmAction() {
        confirmJButton.setText(getString("ConfirmAction.Default"));
        if(null != input) {
            confirmJButton.setText(getString(getConfirmActionKey()));
            
        }
    }

    /**
     * Reload the confirmation message.
     *
     */
    private void reloadConfirmMessage() {
        confirmJLabel.setText(getString("ConfirmMessage.Empty"));
        if(null != input) {
            final String localizedMessage = getLocalizedConfirmMessage();
            if (null == localizedMessage) {
                confirmJLabel.setText(getString(
                        getConfirmMessageKey(),
                        getConfirmMessageArguments()));
            } else {
                confirmJLabel.setText(localizedMessage);
            }
        }
    }

    /**
     * Reload the deny action (jbutton).
     *
     */
    private void reloadDenyAction() {
        denyJButton.setText(getString(DENY_ACTION_DEFAULT_KEY));
        if(null != input) {
            denyJButton.setText(getString(getDenyActionKey()));
        }
    }

    /** Input data keys. */
    public enum DataKey {
        CONFIRM_ACTION_KEY, DENY_ACTION_KEY, LOCALIZED_MESSAGE,
        MESSAGE_ARGUMENTS, MESSAGE_KEY
    }
}
