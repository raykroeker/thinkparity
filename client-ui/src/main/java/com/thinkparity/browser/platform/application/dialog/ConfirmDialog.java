/*
 * Created On: April 7, 2006, 6:25 PM
 * $Id$
 */
package com.thinkparity.browser.platform.application.dialog;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

/**
 * A generic confirmation dialog that uses a popup window to display a
 * confirmation (yes/no) message to obtain user input.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ConfirmDialog extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The deny action default key. */
    private static final String DENY_ACTION_DEFAULT_KEY = "DenyAction.Default";

    /** The confirm action default key. */
    private static final String CONFIRM_ACTION_DEFAULT_KEY = "ConfirmAction.Default";

    /** Flag indicating whether or not the user confirmed. */
    private Boolean confirm;

    /** Creates new form ConfirmDialog */
    public ConfirmDialog() {
        super("ConfirmDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() { return AvatarId.DIALOG_CONFIRM; }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     * 
     */
    public State getState() { return null; }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadConfirmMessage();
        reloadConfirmAction();
        reloadDenyAction();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {}

    /**
     * Determine whether or not the user confirmed.
     * 
     * @return True if the user confirmed; false otherwise.
     */
    public Boolean didConfirm() { return confirm; }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        confirmJButton = ButtonFactory.create(getString("ConfirmButton"));
        denyJButton = ButtonFactory.create(getString("DenyButton"));
        confirmMessageJTextArea = TextFactory.createArea();

        confirmJButton.setText("!Yes!");
        confirmJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmJButtonActionPerformed(evt);
            }
        });

        denyJButton.setText("!No!");
        denyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denyJButtonActionPerformed(evt);
            }
        });

        confirmMessageJTextArea.setColumns(20);
        confirmMessageJTextArea.setEditable(false);
        confirmMessageJTextArea.setLineWrap(true);
        confirmMessageJTextArea.setRows(5);
        confirmMessageJTextArea.setWrapStyleWord(true);
        confirmMessageJTextArea.setBorder(null);
        confirmMessageJTextArea.setFocusable(false);
        confirmMessageJTextArea.setOpaque(false);
        confirmMessageJTextArea.setBorder(null);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, confirmMessageJTextArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
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
                .add(confirmMessageJTextArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(confirmJButton)
                    .add(denyJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

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
     * Obtain the confirmation arguments from the input data.
     * 
     * @return The confirmation message arguments.
     */
    private Object[] getConfirmMessageArguments() {
        return (Object[]) ((Data) input).get(DataKey.MESSAGE_ARGUMENTS);
    }
    
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
     * Obtain the deny action key from the input data.
     * 
     * @return The confirmation action key.
     */
    private String getDenyActionKey() {
        final String denyActionKey =
            (String) ((Data) input).get(DataKey.DENY_ACTION_KEY);
        if(null == denyActionKey) { return DENY_ACTION_DEFAULT_KEY; }
        else { return denyActionKey; }
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
     * Reload the deny action (jbutton).
     *
     */
    private void reloadDenyAction() {
        denyJButton.setText(getString(DENY_ACTION_DEFAULT_KEY));
        if(null != input) {
            denyJButton.setText(getString(getDenyActionKey()));
        }
    }

    /**
     * Reload the confirmation message.
     *
     */
    private void reloadConfirmMessage() {
        confirmMessageJTextArea.setText(getString("ConfirmMessage.Empty"));
        if(null != input) {
            confirmMessageJTextArea.setText(getString(
                    getConfirmMessageKey(),
                    getConfirmMessageArguments()));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmJButton;
    private javax.swing.JTextArea confirmMessageJTextArea;
    private javax.swing.JButton denyJButton;
    // End of variables declaration//GEN-END:variables

    /** Data keys. */
    public enum DataKey { CONFIRM_ACTION_KEY, DENY_ACTION_KEY, MESSAGE_KEY, MESSAGE_ARGUMENTS }
}
