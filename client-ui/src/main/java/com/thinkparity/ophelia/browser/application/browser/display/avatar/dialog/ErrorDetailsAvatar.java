/*
 * Created on January 23, 2007, 12:20 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog;

import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.swing.ClipboardUtils;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Error Dialog<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ErrorDetailsAvatar extends Avatar {
    
    /**
     * Create ErrorDetailsAvatar.
     * 
     */
    public ErrorDetailsAvatar() {
        super("ErrorDetailsDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                disposeWindow();
            }
        });
        initComponents();
    }

    public AvatarId getId() { return AvatarId.DIALOG_ERROR_DETAILS; }

    public State getState() { return null; }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadErrorMessage();
        reloadError();
    }

    public void setState(final State state) {}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        toggleDetailsJButton = new javax.swing.JButton();
        final javax.swing.JButton closeJButton = ButtonFactory.create();
        detailsJPanel = new javax.swing.JPanel();
        detailsJScrollPane = new javax.swing.JScrollPane();
        detailsJTextArea = new javax.swing.JTextArea();

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setText("An error has occured.");
        errorMessageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        toggleDetailsJButton.setText("Show Details");
        toggleDetailsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                toggleDetailsJButtonActionPerformed(e);
            }
        });

        copyDetailsJButton.setFont(Fonts.DialogButtonFont);
        copyDetailsJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("ErrorDetailsDialog.CopyDetailsButton"));
        copyDetailsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                copyDetailsJButtonActionPerformed(e);
            }
        });

        closeJButton.setFont(Fonts.DialogButtonFont);
        closeJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("ErrorDetailsDialog.Ok"));
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                closeJButtonActionPerformed(e);
            }
        });

        detailsJTextArea.setColumns(20);
        detailsJTextArea.setRows(5);
        detailsJScrollPane.setViewportView(detailsJTextArea);

        javax.swing.GroupLayout detailsJPanelLayout = new javax.swing.GroupLayout(detailsJPanel);
        detailsJPanel.setLayout(detailsJPanelLayout);
        detailsJPanelLayout.setHorizontalGroup(
            detailsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
        );
        detailsJPanelLayout.setVerticalGroup(
            detailsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(toggleDetailsJButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(copyDetailsJButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(235, 235, 235)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeJButton)
                    .addComponent(toggleDetailsJButton)
                    .addComponent(copyDetailsJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void toggleDetailsJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_toggleDetailsJButtonActionPerformed
        detailsJPanel.setVisible(detailsJPanel.isVisible());
        validate();
    }//GEN-LAST:event_toggleDetailsJButtonActionPerformed

    private void closeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_closeJButtonActionPerformed

    private void copyDetailsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyDetailsJButtonActionPerformed
        ClipboardUtils.copy(detailsJTextArea);
    }//GEN-LAST:event_copyDetailsJButtonActionPerformed
    
    /**
     * Get the input error.
     * 
     * @return An error string.
     */
    private String getInputError() {
        if(null == input) { return null; }
        else {
            final Throwable t = (Throwable) ((Data) input).get(DataKey.ERROR);
            if (null == t) {
                return null;
            } else {
                final StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                return sw.toString();
            }
        }
    }
    
    /**
     * Get the input error localized message.
     * 
     * @return A string.
     */
    private String getInputErrorLocalizedMessage() {
        if(null == input) { return null; }
        else {
            final Throwable t = (Throwable) ((Data) input).get(DataKey.ERROR);
            if (null == t) {
                return null;
            } else {
                return t.getLocalizedMessage();
            }
        }
    }

    /**
     * Get the error message key from the input.
     * 
     * @return An error message key.
     */
    private Object[] getInputErrorMessageArguments() {
        if (null == input) {
            return null;
        } else {
            return (Object[]) ((Data) input).get(DataKey.ERROR_MESSAGE_ARGUMENTS);
        }
    }   

    /**
     * Get the error message key from the input.
     * 
     * @return An error message key.
     */
    private String getInputErrorMessageKey() {
        if(null == input) { return null; }
        else { return (String) ((Data) input).get(DataKey.ERROR_MESSAGE_KEY); }
    }

    /**
     * Reload the error text area.
     *
     */
    private void reloadError() {
        detailsJTextArea.setText("");
        final String error = getInputError();
        if (null != error) {
            detailsJTextArea.setText(error);
            detailsJTextArea.setCaretPosition(0);
        }
    }

    /**
     * Reload the error message label.
     *
     */
    private void reloadErrorMessage() {
        errorMessageJLabel.setText("");
        final String errorMessageKey = getInputErrorMessageKey();
        final Object[] errorMessageArguments = getInputErrorMessageArguments();
        if (null != errorMessageKey) {
            final String text;
            if (null != errorMessageArguments) {
                text = getString(errorMessageKey, errorMessageArguments);
            } else {
                text = getString(errorMessageKey);
            }
            errorMessageJLabel.setText(MessageFormat.format("<html>{0}</html>", text));
        } else {
            final String errorLocalizedMessage = getInputErrorLocalizedMessage();
            if (null != errorLocalizedMessage) {
                errorMessageJLabel.setText(errorLocalizedMessage);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton copyDetailsJButton = ButtonFactory.create();
    private javax.swing.JPanel detailsJPanel;
    private javax.swing.JScrollPane detailsJScrollPane;
    private javax.swing.JTextArea detailsJTextArea;
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private javax.swing.JButton toggleDetailsJButton;
    // End of variables declaration//GEN-END:variables

    /** Data keys. */
    public enum DataKey { ERROR, ERROR_MESSAGE_ARGUMENTS, ERROR_MESSAGE_KEY }
}
