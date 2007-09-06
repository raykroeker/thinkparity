/*
 * Created on July 21, 2006, 12:08 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractAction;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class ErrorAvatar extends Avatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** Creates new form ErrorDialog */
    public ErrorAvatar() {
        super("ErrorAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEscapeKey();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getAvatarTitle()
     */
    @Override
    public String getAvatarTitle() {
        if (null == input) {
            return null;
        } else if (null == getInputErrorMessageKey()) {
            return getString("Title");
        } else {
            final StringBuffer errorKey = new StringBuffer(getInputErrorMessageKey()).append(".Title");
            return getString(errorKey.toString());
        }
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_ERROR;
    }

    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        reloadErrorMessage();
    }

    public void setState(final State state) {}

    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                disposeWindow();
            }
        });
    }

    private void closeJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_closeJButtonActionPerformed

    /**
     * Get the current date.
     * 
     * @return The current date <code>String</code>.
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm Z").format(Calendar.getInstance().getTime());
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
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(DataKey.ERROR_MESSAGE_KEY);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JButton closeJButton = ButtonFactory.create();

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setText("Error encountered.");
        errorMessageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        errorMessageJLabel.setPreferredSize(new java.awt.Dimension(485, 50));

        closeJButton.setFont(Fonts.DialogButtonFont);
        closeJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ErrorAvatar.Ok"));
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(closeJButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(27, 27, 27)
                .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeJButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

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
            errorMessageJLabel.setText(MessageFormat.format("<html>{0}</html>",
                    getString("ErrorUnexpected", new Object[] {getCurrentDate()})));
        }
    }

    /** Data keys. */
    public enum DataKey { ERROR, ERROR_MESSAGE_ARGUMENTS, ERROR_MESSAGE_KEY }
}
