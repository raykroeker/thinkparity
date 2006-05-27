/*
 * SessionSendFormAvatar.java
 *
 * Created on March 14, 2006, 9:54 AM
 */

package com.thinkparity.browser.application.browser.display.avatar.document;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.component.SeparatorFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.session.UserListCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 *
 * @author  raymond
 */
public class AddNewTeamMember extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The contacts list model. */
    private final DefaultListModel contactsModel;

    /** The contacts list selection model. */
    private final DefaultListSelectionModel contactsSelectionModel;

    /** The team list model. */
    private final DefaultListModel teamModel;

    /** The team list selection model. */
    private final DefaultListSelectionModel teamSelectionModel;

    /**
     * Used by the contact provider to exclude team members from the contact
     * list.
     *
     */
    private User[] team;

    /** Create AddNewTeamMember. */
    public AddNewTeamMember() {
        super("AddNewTeamMember", BrowserConstants.DIALOGUE_BACKGROUND);
        this.contactsModel = new DefaultListModel();
        this.contactsSelectionModel = new DefaultListSelectionModel();
        this.teamModel = new DefaultListModel();
        this.teamSelectionModel = new DefaultListSelectionModel();
        initComponents();
    }

    public void setState(final State state) {}

    public State getState() { return null; }

    public AvatarId getId() { return AvatarId.ADD_TEAM_MEMBER; }

    /**
     * Determine whether or not the user's input is valid.
     *
     */
    public Boolean isInputValid() {
        Long documentId;
        try { documentId = extractDocumentId(); } catch(final Throwable t) { return Boolean.FALSE; }
        
        Collection<Contact> contacts = null;
        try { contacts = extractContacts(); }
        catch(final Throwable t) { return Boolean.FALSE; }
        
        if(null != documentId && 0 < contacts.size()) {
            return Boolean.TRUE;
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Reload the avatar based upon the input and content providers.
     *
     */
    public void reload() {
    	reloadDocument();
        reloadTeamMembers();
        reloadContacts();
        reloadAddJButtonState();
        
        contactsJList.requestFocusInWindow();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel contactsJLabel;
        javax.swing.JScrollPane contactsJScrollPane;
        javax.swing.JLabel documentJLabel;
        javax.swing.JSeparator jSeparator_1;

        documentJLabel = LabelFactory.create(getString("DocumentLabel"));
        documentNameJLabel = new javax.swing.JLabel();
        jSeparator_1 = SeparatorFactory.create();
        contactsJLabel = LabelFactory.create(getString("ContactsLabel"));
        contactsJScrollPane = ScrollPaneFactory.create();
        contactsJList = ListFactory.create();
        addJButton = ButtonFactory.create(getString("AddButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));

        contactsJList.setCellRenderer(new UserListCellRenderer());
        contactsJList.setModel(contactsModel);
        contactsJList.setSelectionModel(contactsSelectionModel);
        contactsJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                contactsJListValueChanged(e);
            }
        });

        contactsJScrollPane.setViewportView(contactsJList);

        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addJButtonActionPerformed(e);
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
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(documentJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(documentNameJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator_1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(contactsJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                        .add(14, 14, 14))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(cancelJButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(addJButton))
                            .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
                        .add(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(documentJLabel)
                    .add(documentNameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator_1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactsJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addJButton)
                    .add(cancelJButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addJButtonActionPerformed
        if(isInputValid()) {
            final List<Contact> contacts = extractContacts();
            final List<JabberId> jabberIds = new LinkedList<JabberId>();
            for(final Contact contact : contacts) { jabberIds.add(contact.getId()); }
            getController().runAddNewDocumentTeamMember(extractDocumentId(), jabberIds);
            disposeWindow();
        }
    }//GEN-LAST:event_addJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void disposeWindow() {
    	SwingUtilities.getWindowAncestor(this).dispose();
    }
    
    private void contactsJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_contactsJListValueChanged
        if(!evt.getValueIsAdjusting()) {
            if(ListSelectionModel.SINGLE_SELECTION ==
                contactsSelectionModel.getSelectionMode()) {
                teamSelectionModel.clearSelection();
            }
        }
    	reloadAddJButtonState();
    }//GEN-LAST:event_contactsJListValueChanged
    
    /**
     * Extract the selected contacts.
     *
     */
    private List<Contact> extractContacts() {
        return SwingUtil.extract(contactsJList);
    }

    /**
     * Extract the document id we are sending.
     *
     */
    private Long extractDocumentId() { return (Long) input; }

    /**
     * Obtain a list of contacts from the content provider.
     *
     */
    private Contact[] getContacts(final User[] team) {
        return (Contact[]) ((CompositeFlatSingleContentProvider) contentProvider)
				.getElements(0, team);
    }

    /**
     * Obtain the document from the content provider.
     *
     * @return The document.
     */
    private Document getDocument() {
        return (Document) ((CompositeFlatSingleContentProvider) contentProvider)
        .getElement(0, (Long) input);
    }

    /**
     * Obtain a list of team members from the content provider.
     *
     */
    private User[] getTeam() {
        return (Contact[]) ((CompositeFlatSingleContentProvider) contentProvider)
                        .getElements(1, (Long) input);
    }

    /**
     * Load a list model with the user list.
     *
     */
    private void loadUserList(final DefaultListModel listModel,
        final User[] users) {
        for (final User user : users) {
            logger.debug("Adding user:  " + user.getSimpleUsername());
            listModel.addElement(user);
        }
    }

    /**
     * Reload the contacts model.
     *
     */
    private void reloadContacts() {
        contactsModel.clear();
        if(null != input) loadUserList(contactsModel, getContacts(team));
    }

    /**
     * Reload the document.
     *
     */
    private void reloadDocument() {
    	documentNameJLabel.setText(getString("DocumentNameLabel.Empty"));
    	if(null != input) {
            final Document document = getDocument();
            final Object[] arguments = new Object[] {document.getName()};
            documentNameJLabel.setText(getString("DocumentNameLabel", arguments));
    	}
    }

    /**
     * Set the enabled property of the send button based upon the validity
     * of the form's input.
     *
     */
    private void reloadAddJButtonState() {
    	addJButton.setEnabled(isInputValid());
    }

    private void reloadTeamMembers() {
        teamModel.clear();
        if(null != input) {
            team = getTeam();
            loadUserList(teamModel, team);
        }
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     *
     */
    public void setInput(Object input) {
        Assert.assertNotNull("Cannot set null input:  " + getId(), input);
        this.input = input;
        reload();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JList contactsJList;
    private javax.swing.JLabel documentNameJLabel;
    // End of variables declaration//GEN-END:variables
    
}
