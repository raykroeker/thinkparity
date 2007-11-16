/*
 * CreateOutgoingEMailInvitationAvatar.java
 *
 * Created on November 12, 2007, 2:22 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.session.OfflineException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact.CreateOutgoingEMailInvitationProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author robert@thinkparity.com
 */
public class CreateOutgoingEMailInvitationAvatar extends Avatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JButton cancelJButton = ButtonFactory.create();
    private final javax.swing.JTextField emailJTextField = TextFactory.create();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** A <code>List</code> of <code>String</code> delimiters. */
    private List<String> delimiters;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Creates new form CreateOutgoingEMailInvitationAvatar */
    public CreateOutgoingEMailInvitationAvatar() {
        super("CreateOutgoingEMailInvitationAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        initComponents();
        initEMailDelimiters();
        addValidationListener(emailJTextField);
        initFocusListeners();
        initFocusListenerOKButton();
        bindEscapeKey();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTACT_CREATE_OUTGOING_EMAIL_INVITATION;
    }

    public State getState() {
        return null;
    }

    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input != null) {
            emailJTextField.setText("");
            emailJTextField.requestFocusInWindow();
            validateInput();
        }
    }

    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isInputValid()
     */
    @Override
    protected Boolean isInputValid() {
        validateInput(Boolean.TRUE);
        return !containsInputErrors();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
        validateInput(Boolean.FALSE);
    }

    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Create the outgoing email invitation.
     */
    private void createOutgoingEMailInvitation() {
        getController().runCreateOutgoingEMailInvitation(extractInputEMail());
    }

    private void emailJTextFieldActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailJTextFieldActionPerformed
        okJButtonActionPerformed(evt);
    }//GEN-LAST:event_emailJTextFieldActionPerformed

    /**
     * Extract the EMail from the control.
     * 
     * @return The <code>EMail</code>.
     */
    private EMail extractInputEMail() {
        final String inputEMail = extractInputEMailString();
        if (null != inputEMail) {
            final List<String> emails = StringUtil.tokenize(extractInputEMailString(), delimiters, new ArrayList<String>());
            if (emails.size() > 0) {
                final EMail email = EMailBuilder.parse(emails.get(0));
                return email;
            }
        }
        return null;
    }

    /**
     * Extract the EMail string from the control.
     *
     * @return The EMail <code>String</code>.
     */
    private String extractInputEMailString() {
        return SwingUtil.extract(emailJTextField, Boolean.TRUE);
    }

    /**
     * Read the list of contacts from the input.
     * 
     * @return A List of <code>Contact</code>.
     */
    private List<Contact> getInputContacts() {
        final List<Contact> contacts = ((Data) input).getList(DataKey.CONTACTS);
        return contacts;
    }

    /**
     * Read the list of outgoing email invitations from the input.
     * 
     * @return A List of <code>OutgoingEMailInvitation</code>.
     */
    private List<OutgoingEMailInvitation> getInputOutgoingEMailInvitations() {
        final List<OutgoingEMailInvitation> invitations = ((Data) input).getList(DataKey.OUTGOING_EMAIL_INVITATIONS);
        return invitations;
    }

    /**
     * Read the profile email from the input.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    private ProfileEMail getInputProfileEMail() {
        return (ProfileEMail) ((Data) input).get(DataKey.PROFILE_EMAIL);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel emailJLabel = new javax.swing.JLabel();

        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateOutgoingEMailInvitationAvatar.Explanation"));

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateOutgoingEMailInvitationAvatar.EMail"));

        emailJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) emailJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(profileConstraints.getEmail()));
        emailJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailJTextFieldActionPerformed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateOutgoingEMailInvitationAvatar.Invite"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateOutgoingEMailInvitationAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(emailJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(explanationJLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailJLabel)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(errorMessageJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the email delimiters.
     */
    private void initEMailDelimiters() {
        this.delimiters = new ArrayList<String>(3);
        delimiters.add(",");
        delimiters.add(";");
        delimiters.add("\n");
        delimiters.add(" ");
    }

    /**
     * Initialize the focus listeners.
     */
    private void initFocusListeners() {
        final FocusListener focusListener = new FocusListener() {
            public void focusGained(final FocusEvent e) {
                validateInput();
            }
            public void focusLost(final FocusEvent e) {
                validateInput();
            }
        };
        emailJTextField.addFocusListener(focusListener);
    }

    /**
     * Initialize the focus listener for the OK button.
     * 
     * This ensures the OK button does not have focus if the
     * user tabs to it and then it becomes disabled.
     */
    private void initFocusListenerOKButton() {
        okJButton.addFocusListener(new FocusListener() {
            public void focusGained(final FocusEvent e) {
                if (!okJButton.isEnabled()) {
                    cancelJButton.requestFocusInWindow();
                }
            }
            public void focusLost(final FocusEvent e) {
            }
        });
    }

    /**
     * Determine if the email is valid.
     * 
     * @param email
     *            An email <code>String</code>.
     * @return True if the email is valid; false otherwise.
     */
    private Boolean isEmailValid(final String email) {
        try {
            EMailBuilder.parse(email);
            return Boolean.TRUE;
        } catch(final EMailFormatException efx) {
            return Boolean.FALSE;
        }
    }

    /**
     * Determine if the invitation is valid.
     * 
     * @return True if the invitation is valid; false otherwise.
     */
    private Boolean isInviteRestricted() {
        final EMail email = extractInputEMail();
        Boolean restricted = Boolean.TRUE;
        if (null != email) {
            try {
                restricted = ((CreateOutgoingEMailInvitationProvider) contentProvider).readIsInviteRestricted(email);
            } catch (final OfflineException ox) {
                addInputError(getString("ErrorOffline"));
            }
        }
        if (restricted) {
            addInputError(getString("ErrorInvalidInvitation"));
        }
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
            okJButton.setEnabled(Boolean.FALSE);
        }
        
        return restricted;
    }

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
            errorMessageJLabel.setText(getString("CheckingInvitation"));
            errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel
                    .getWidth(), errorMessageJLabel.getHeight());
            final Boolean restricted = isInviteRestricted();
            SwingUtil.setCursor(this, null);
            if (!restricted) {
                disposeWindow();
                createOutgoingEMailInvitation();
            }
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    /**
     * Validate input.
     * 
     * @param ignoreFocus
     *            A <code>Boolean</code> to ignore focus or not.
     */
    private void validateInput(final Boolean ignoreFocus) {
        super.validateInput();
        final String inputEMail = extractInputEMailString();
        String email = null;

        // strip out delimiters, then make sure there is exactly one email
        if (null == inputEMail) {
            addInputError(Separator.Space.toString());
        } else {
            final List<String> emails = StringUtil.tokenize(inputEMail, delimiters, new ArrayList<String>());
            if (emails.size() > 1) {
                addInputError(getString("ErrorMoreThanOneEmail"));
            } else if (emails.size() == 1) {
                email = emails.get(0);
            } else {
                addInputError(Separator.Space.toString());
            }
        }

        // check the email is valid
        if (!containsInputErrors() && !isEmailValid(email)) {
            if (ignoreFocus || !emailJTextField.isFocusOwner()) {
                addInputError(getString("ErrorEmailInvalid"));
            }
        }

        // check the user isn't inviting himself
        if (!containsInputErrors()) {
            if (email.equalsIgnoreCase(getInputProfileEMail().getEmail().toString())) {
                addInputError(getString("ErrorProfileEMail"));
            }
        }

        // check there isn't already an outgoing invitation for this email
        if (!containsInputErrors()) {
            final List<OutgoingEMailInvitation> invitations = getInputOutgoingEMailInvitations();
            for (final OutgoingEMailInvitation invitation : invitations) {
                if (email.equalsIgnoreCase(invitation.getInvitationEMail().toString())) {
                    addInputError(getString("ErrorOutgoingEMailInvitationExists"));
                }
            }
        }

        // check the invitation is not already a contact
        if (!containsInputErrors()) {
            final List<Contact> contacts = getInputContacts();
            for (final Contact contact : contacts) {
                if (contact.getEmailsSize() > 0) {
                    if (email.equalsIgnoreCase(contact.getEmails().get(0).toString())) {
                        addInputError(getString("ErrorContactExists"));
                    }
                }
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        okJButton.setEnabled(!containsInputErrors());
    }

    /** <b>Title:</b>Create Outgoing EMail Invitation Avatar Data Key<br> */
    public enum DataKey { CONTACTS, OUTGOING_EMAIL_INVITATIONS, PROFILE_EMAIL }
}
