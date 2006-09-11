/*
 * Created On: Aug 25, 2006 8:11:14 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.ProfileEMailCell;



/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class UpdateProfileAvatarModel {

    private final DefaultListModel emailsListModel;

    UpdateProfileAvatarModel() {
        super();
        this.emailsListModel = new DefaultListModel();
    }

    /**
     * Add an <code>EMail</code> to the model.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the model is modified as a result of the add; false
     *         otherwise.
     */
    Boolean add(final ProfileEMailCell emailCell) {
        if (!emailsListModel.contains(emailCell)) {
            final int originalSize = emailsListModel.size();
            emailsListModel.addElement(emailCell);
            return originalSize != emailsListModel.size();
        }
        return Boolean.FALSE;
    }

    /**
     * Clear all <code>EMails</code>.
     *
     */
    void clear() {
        emailsListModel.clear();
    }

    /**
     * Retreive the emails from the model.
     * 
     * @return A list of emails.
     */
    List<ProfileEMail> getEmails() {
        final List<ProfileEMail> emails = new ArrayList<ProfileEMail>(emailsListModel.size());
        for (int i = 0; i < emailsListModel.size(); i++) {
            emails.add(((ProfileEMailCell) emailsListModel.get(i)));
        }
        return emails;
    }

    ListModel getEmailsListModel() {
        return emailsListModel;
    }

    List<ProfileEMail> getSelectedEmails(final JList emailsJList) {
        final int[] selectedIndices = emailsJList.getSelectedIndices();
        final List<ProfileEMail> emails = new ArrayList<ProfileEMail>(selectedIndices.length);
        for (final int selectedIndex : selectedIndices) {
            emails.add((ProfileEMail) emailsListModel.getElementAt(selectedIndex));
        }
        return emails;
    }

    /**
     * Remove an <code>EMail</code> from the model.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the model is modified as a result of the add; false
     *         otherwise.
     */
    Boolean remove(final ProfileEMailCell emailCell) {
        if (emailsListModel.contains(emailCell)) {
            final int originalSize = emailsListModel.size();
            emailsListModel.removeElement(emailCell);
            return originalSize == emailsListModel.size();
        }
        return Boolean.FALSE;
    }
}
