/*
 * Created on January 23, 2007, 2:19 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Confirm Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ConfirmAvatar extends Avatar {

    /** Flag indicating whether or not the user confirmed. */
    private Boolean confirm;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton confirmJButton = ButtonFactory.create();
    private final javax.swing.JLabel confirmMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JButton denyJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /**
     * Create ConfirmAvatar.
     *
     */
    public ConfirmAvatar() {
        super("ConfirmAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindKeys();
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getAvatarTitle()
     */
    @Override
    public String getAvatarTitle() {
        if (null == input) {
            return null;
        } else if (null == getInputString(DataKey.MESSAGE_KEY, null)) {
            return getString("Title");
        } else {
            final StringBuffer confirmKey = new StringBuffer(getInputConfirmMessageKey()).append(".Title");
            return getString(confirmKey.toString());
        }
    }

    public AvatarId getId() { return AvatarId.DIALOG_CONFIRM; }

    public State getState() { return null; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     * 
     */
    public void reload() {
        confirm = Boolean.FALSE;
        reloadConfirmJButton();
        reloadDenyJButton();
        reloadConfirmMessage();
    }

    public void setState(final State state) {}

    /**
     * Bind keys.
     */
    private void bindKeys() {
        final AbstractAction confirmAction = new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                confirm();
            }
        };
        final AbstractAction denyAction = new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                deny();
            }
        };
        bindEscapeKey("Cancel", denyAction);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), confirmAction);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.SHIFT_DOWN_MASK), confirmAction);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), denyAction);
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.SHIFT_DOWN_MASK), denyAction);
    }

    /**
     * User confirms.
     */
    private void confirm() {
        confirm = Boolean.TRUE;
        disposeWindow();
    }

    private void confirmJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmJButtonActionPerformed
        confirm();
    }//GEN-LAST:event_confirmJButtonActionPerformed

    /**
     * User denies.
     */
    private void deny() {
        confirm = Boolean.FALSE;
        disposeWindow();
    }

    private void denyJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_denyJButtonActionPerformed
        deny();
    }//GEN-LAST:event_denyJButtonActionPerformed

    /**
     * Obtain the confirmation arguments from the input data.
     * 
     * @return The confirmation message arguments.
     */
    private Object[] getInputConfirmMessageArguments() {
        if (null == input) {
            return null;
        } else {
            return (Object[]) ((Data) input).get(DataKey.MESSAGE_ARGUMENTS);
        }
    }

    /**
     * Obtain the confirmation message key from the input data.
     * 
     * @return The confirmation message key.
     */
    private String getInputConfirmMessageKey() {
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(DataKey.MESSAGE_KEY);
        }
    }

    /**
     * Obtain an input string.
     * 
     * @param key
     *            The input key <code>DataKey</code>.
     * @param defaultValue
     *            The default value to provide if the input value is null.
     * @return The value of the input; or the default value if the input is
     *         null.
     */
    private String getInputString(final DataKey key, final String defaultValue) {
        if (null == input) {
            return defaultValue;
        } else {
            final String inputString = (String) ((Data) input).get(key);
            if (null == inputString) {
                return defaultValue;
            } else {
                return inputString;
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        confirmMessageJLabel.setFont(Fonts.DialogFont);
        confirmMessageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        confirmJButton.setFont(Fonts.DialogButtonFont);
        confirmJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ConfirmAvatar.Confirm"));
        confirmJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmJButtonActionPerformed(evt);
            }
        });

        denyJButton.setFont(Fonts.DialogButtonFont);
        denyJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ConfirmAvatar.Deny"));
        denyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denyJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(confirmMessageJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(confirmJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(denyJButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {confirmJButton, denyJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(confirmMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(denyJButton)
                    .addComponent(confirmJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the confirm button text.
     *
     */
    private void reloadConfirmJButton() {
        final String text = getString(getInputString(DataKey.CONFIRM_BUTTON_KEY,
                "Confirm"));
        confirmJButton.setText(text);
    }

    /**
     * Reload the confirmation message.
     *
     */
    private void reloadConfirmMessage() {
        confirmMessageJLabel.setText("");
        final String localizedConfirmMessage = getInputString(DataKey.LOCALIZED_MESSAGE, null);
        final String confirmMessageKey = getInputString(DataKey.MESSAGE_KEY, null);
        final Object[] confirmMessageArguments = getInputConfirmMessageArguments();
        if (null != localizedConfirmMessage) {
            confirmMessageJLabel.setText(MessageFormat.format("<html>{0}</html>", localizedConfirmMessage));
        } else if (null != confirmMessageKey) {
            final String text;
            if (null != confirmMessageArguments) {
                text = getString(confirmMessageKey, confirmMessageArguments);
            } else {
                text = getString(confirmMessageKey);
            }
            confirmMessageJLabel.setText(MessageFormat.format("<html>{0}</html>", text));
        }
    }

    /**
     * Reload the deny button text.
     *
     */
    private void reloadDenyJButton() {
        final String text = getString(getInputString(DataKey.DENY_BUTTON_KEY,
                "Deny"));
        denyJButton.setText(text);
    }

    /** Data keys. */
    public enum DataKey {
        CONFIRM_BUTTON_KEY, DENY_BUTTON_KEY, LOCALIZED_MESSAGE,
        MESSAGE_ARGUMENTS, MESSAGE_KEY
    }
}
