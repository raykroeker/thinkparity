/*
 * ConfirmDialog.java
 *
 * Created on April 7, 2006, 6:25 PM
 */
package com.thinkparity.browser.platform.application.dialog;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * A generic confirmation dialog that uses a popup window to display a
 * confirmation (yes/no) message to obtain user input.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmDialog extends AbstractJPanel {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Display a confirmation dialog with custom confirm/deny text.
     * 
     * @param confirmMessage
     *            The confirm message.
     * @param confirmText
     *            The confirm text.
     * @param denyText
     *            The deny text.
     * @return True if the user confirmed; false otherwise.
     */
    public static Boolean confirm(final BrowserWindow browserWindow,
            final String confirmMessageKey, final String confirmTextKey,
            final String denyTextKey) {
        final ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmMessage(confirmMessageKey);
        confirmDialog.setConfirmText(confirmTextKey);
        confirmDialog.setDenyText(denyTextKey);
        return doConfirm(browserWindow, confirmDialog);
    }

    /**
     * Display a confirmation dialog.
     * 
     * @param confirmMessageKey
     *            The confirm message.
     * @return True if the user confirmed; false otherwise.
     */
    public static Boolean confirm(final BrowserWindow browserWindow,
            final String confirmMessageKey) {
        final ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmMessage(confirmMessageKey);
        return doConfirm(browserWindow, confirmDialog);
    }

    /**
     * Display a confirmation dialog.
     * 
     * @param confirmMessageKey
     *            The confirm message.
     * @return True if the user confirmed; false otherwise.
     */
    public static Boolean confirm(final BrowserWindow browserWindow,
            final String confirmMessageKey, final Object[] arguments) {
        final ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmMessage(confirmMessageKey, arguments);
        return doConfirm(browserWindow, confirmDialog);
    }

    /**
     * Display the confirmation dialog.
     * 
     * @param confirmDialog
     *            The confirmation dialog.
     * @return True if the user confirmed; false otherwise.
     */
    private static Boolean doConfirm(final BrowserWindow browserWindow,
            final ConfirmDialog confirmDialog) {
        final Window w = WindowFactory.create(WindowId.CONFIRM, browserWindow);
        w.open(confirmDialog, new Dimension(300, 125));
        return confirmDialog.didConfirm();
    }

    /** Flag indicating whether or not the user confirmed. */
    private Boolean confirm;

    /** Creates new form ConfirmDialog */
    private ConfirmDialog() {
        super("ConfirmDialog");
        initComponents();
    }

    /**
     * Determine whether or not the user confirmed.
     * 
     * @return True if the user confirmed; false otherwise.
     */
    private Boolean didConfirm() { return confirm; }

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

        setBackground(new java.awt.Color(255, 255, 255));
        confirmJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                confirmJButtonActionPerformed(e);
            }
        });

        denyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                denyJButtonActionPerformed(e);
            }
        });

        confirmMessageJTextArea.setColumns(20);
        confirmMessageJTextArea.setEditable(false);
        confirmMessageJTextArea.setLineWrap(true);
        confirmMessageJTextArea.setRows(5);
        confirmMessageJTextArea.setWrapStyleWord(true);
        confirmMessageJTextArea.setBorder(null);
        confirmMessageJTextArea.setFocusable(false);
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
     * Dispose of the window.
     *
     */
    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    /**
     * Set the confirmation message text.
     * 
     * @param confirmMessage
     *            The confirmation message.
     */
    private void setConfirmMessage(final String confirmMessageKey) {
        confirmMessageJTextArea.setText(getString(confirmMessageKey));
    }

    /**
     * Set the confirmation message text.
     * 
     * @param confirmMessage
     *            The confirmation message localization key.
     * @param arguments
     *            The localization message arguments.
     */
    private void setConfirmMessage(final String confirmMessageKey,
            final Object[] arguments) {
        confirmMessageJTextArea.setText(getString(confirmMessageKey, arguments));
    }

    /**
     * Set the confirmation action text.
     * 
     * @param confirmText
     *            The confirmation action text.
     */
    private void setConfirmText(final String confirmTextKey) {
        confirmJButton.setText(getString(confirmTextKey));
    }

    /**
     * Set the deny action text.
     * 
     * @param denyText
     *            The deny action text.
     */
    private void setDenyText(final String denyTextKey) {
        denyJButton.setText(getString(denyTextKey));
    }

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmJButton;
    private javax.swing.JTextArea confirmMessageJTextArea;
    private javax.swing.JButton denyJButton;
    // End of variables declaration//GEN-END:variables
}
