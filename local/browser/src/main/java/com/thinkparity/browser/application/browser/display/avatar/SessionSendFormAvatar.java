/*
 * SessionSendFormAvatar.java
 *
 * Created on March 14, 2006, 9:54 AM
 */

package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.component.*;
import com.thinkparity.browser.application.browser.display.avatar.session.UserListCellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.session.VersionListCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.model.document.WorkingVersion;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 *
 * @author  raymond
 */
public class SessionSendFormAvatar extends Avatar {

    /**
     *@see java.io.Serializable
     *
     */
    private static final long serialVersionUID = 1;
    
    /**
     * The contacts list model.
     *
     */
    private final DefaultListModel contactsModel;

    /**
     * The contacts list selection model.
     *
     */
    private final DefaultListSelectionModel contactsSelectionModel;

    /**
	 * Flag indicating whether or not the include key checkbox is selected.
	 * 
	 * @see #includeKeyJCheckBoxStateChanged(javax.swing.event.ChangeEvent)
	 */
	private Boolean includeKeyIsSelected = Boolean.FALSE;

    /**
     * The team list model.
     *
     */
    private final DefaultListModel teamModel;

    /**
     * The team list selection model.
     *
     */
    private final DefaultListSelectionModel teamSelectionModel;

    /**
     * Used by the contact provider to exclude team members from the contact
     * list.
     *
     */
    private Contact[] team;

    /**
     * The version model.
     *
     */
    private final DefaultComboBoxModel versionModel;

    /**
     * Create a SessionSendFormAvatar
     *
     */
    public SessionSendFormAvatar() {
    	// COLOR Send Form Background
		super("SessionSendForm", Color.WHITE);
		this.contactsModel = new DefaultListModel();
		this.contactsSelectionModel = new DefaultListSelectionModel();
		this.teamModel = new DefaultListModel();
		this.teamSelectionModel = new DefaultListSelectionModel();
		this.versionModel = new DefaultComboBoxModel();
		initComponents();
	}

    public void setState(final State state) {}

    public State getState() { return null; }

    public AvatarId getId() { return AvatarId.SESSION_SEND_FORM; }

    /**
     * Determine whether or not the user's input is valid.
     *
     */
    public Boolean isInputValid() {
		Long documentId;
		try { documentId = extractDocumentId(); }
		catch(final Throwable t) { return Boolean.FALSE; }

		Collection<Contact> contacts = null;
		try { contacts = extractTeam(); }
		catch(final Throwable t) { return Boolean.FALSE; }

		try { contacts.addAll(extractContacts()); }
		catch(final Throwable t) { return Boolean.FALSE; }

		final Boolean doIncludeKey = extractDoIncludeKey();
		if(doIncludeKey) {
			// only 1 user if the key is to be included
			if(1 != contacts.size()) { return Boolean.FALSE; }
		}
		else {
			// at least 1 user if the key is not included
			if(1 > contacts.size()) { return Boolean.FALSE; }
		}

		final DocumentVersion version = extractDocumentVersion();
		if(null == version) { return Boolean.FALSE; }
		if(doIncludeKey) {
			// if including the key; the working version must be selected
			if(version != WorkingVersion.getWorkingVersion()) { return Boolean.FALSE; }
		}

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
		reloadIncludeKey();
		reloadTeamMembers();
		reloadContacts();
		reloadVersions();

		versionJComboBox.requestFocusInWindow();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel contactsJLabel;
        javax.swing.JScrollPane contactsJScrollPane;
        javax.swing.JSeparator jSeparator_1;
        javax.swing.JLabel teamJLabel;
        javax.swing.JScrollPane teamJScrollPane;
        javax.swing.JLabel versionJLabel;

        documentJLabel = LabelFactory.create();
        versionJLabel = LabelFactory.create(getString("VersionLabel"));
        versionJComboBox = ComboBoxFactory.create();
        includeKeyJCheckBox = CheckBoxFactory.create(getString("IncludeKeyCheckBox"));
        jSeparator_1 = SeparatorFactory.create();
        sendToJPanel = new javax.swing.JPanel();
        teamJLabel = LabelFactory.create(getString("TeamLabel"));
        contactsJLabel = LabelFactory.create(getString("ContactsLabel"));
        teamJScrollPane = ScrollPaneFactory.create();
        teamJList = ListFactory.create();
        contactsJScrollPane = ScrollPaneFactory.create();
        contactsJList = ListFactory.create();
        sendJButton = ButtonFactory.create(getString("SendButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));
        vSpacerJLabel = LabelFactory.create();

        setOpaque(false);

        versionJComboBox.setModel(versionModel);
        versionJComboBox.setRenderer(new VersionListCellRenderer());
        versionJComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                versionJComboBoxItemStateChanged(e);
            }
        });

        includeKeyJCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        includeKeyJCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        includeKeyJCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                includeKeyJCheckBoxStateChanged(e);
            }
        });


        teamJList.setCellRenderer(new UserListCellRenderer());
        teamJList.setModel(teamModel);
        teamJList.setSelectionModel(teamSelectionModel);
        teamJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                teamJListValueChanged(e);
            }
        });

        teamJScrollPane.setViewportView(teamJList);

        contactsJList.setCellRenderer(new UserListCellRenderer());
        contactsJList.setModel(contactsModel);
        contactsJList.setSelectionModel(contactsSelectionModel);
        contactsJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                contactsJListValueChanged(e);
            }
        });

        contactsJScrollPane.setViewportView(contactsJList);

        org.jdesktop.layout.GroupLayout sendToJPanelLayout = new org.jdesktop.layout.GroupLayout(sendToJPanel);
        sendToJPanel.setLayout(sendToJPanelLayout);
        sendToJPanelLayout.setHorizontalGroup(
            sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sendToJPanelLayout.createSequentialGroup()
                .add(sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(teamJLabel)
                    .add(teamJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(contactsJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)))
        );
        sendToJPanelLayout.setVerticalGroup(
            sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sendToJPanelLayout.createSequentialGroup()
                .add(sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(teamJLabel)
                    .add(contactsJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(teamJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                .addContainerGap())
        );

        sendJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sendJButtonActionPerformed(e);
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
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, sendToJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(documentJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sendJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator_1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(versionJLabel)
                            .add(vSpacerJLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, includeKeyJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, versionJComboBox, 0, 309, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(documentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(versionJLabel)
                    .add(versionJComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(includeKeyJCheckBox)
                    .add(vSpacerJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator_1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendToJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(sendJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void versionJComboBoxItemStateChanged(java.awt.event.ItemEvent e) {//GEN-FIRST:event_versionJComboBoxItemStateChanged
    	reloadSendJButtonState();
    }//GEN-LAST:event_versionJComboBoxItemStateChanged

    private void sendJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendJButtonActionPerformed
    	if(isInputValid()) {
			final Long documentId = extractDocumentId();
			final List<User> contacts = proxy(extractTeam());
			contacts.addAll(proxy(extractContacts()));
			final Boolean doIncludeKey = extractDoIncludeKey();
			toggleVisualFeedback(Boolean.TRUE);
			try {
				if(doIncludeKey) {
					// create a version and send it
					// update the server key holder
					final User user = contacts.get(0);
					// TODO Refactor the user object.
					final JabberId jabberId = JabberIdBuilder.parseUsername(user.getSimpleUsername());
					getSessionModel().sendKeyResponse(documentId, jabberId, KeyResponse.ACCEPT);
					cancelJButtonActionPerformed(evt);
				}
				else {
					final DocumentVersion version = extractDocumentVersion();
					if(version == WorkingVersion.getWorkingVersion()) {
						// create a version and send it
						getSessionModel().send(contacts, documentId);
						cancelJButtonActionPerformed(evt);
					}
					else {
						// send a specific version
						getSessionModel().send(
								contacts, documentId, version.getVersionId());
						cancelJButtonActionPerformed(evt);
					}
				}
				// NOTE Interesting
				ArtifactModel.getModel().applyFlagSeen(documentId);
			}
			catch(final ParityException px) { throw new RuntimeException(px); }
			finally {
				toggleVisualFeedback(Boolean.FALSE);
				getController().fireDocumentUpdated(documentId);
			}
		}
    }//GEN-LAST:event_sendJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
		SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void includeKeyJCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_includeKeyJCheckBoxStateChanged
		if(includeKeyIsSelected != includeKeyJCheckBox.isSelected()) {
			if(includeKeyJCheckBox.isSelected()) {
				contactsSelectionModel.setSelectionMode(
						ListSelectionModel.SINGLE_SELECTION);
				teamSelectionModel.setSelectionMode(
						ListSelectionModel.SINGLE_SELECTION);

				// since include key is selected; only 1 user can be used
				// as the "destination"  remove *all* but the first selected
				// user
				int[] indices = teamJList.getSelectedIndices();
				if(1 < indices.length) {
					for(int i = 1; i < indices.length; i++) {
						teamSelectionModel.removeSelectionInterval(i, i);
					}
					contactsSelectionModel.clearSelection();
				}
				else {
					indices = contactsJList.getSelectedIndices();
					if(1 < indices.length) {
						for(int i = 1; i < indices.length; i++) {
							contactsSelectionModel.removeSelectionInterval(i, i);
						}
					}
				}
			}
			else {
				contactsSelectionModel.setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				teamSelectionModel.setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			}
			// if the state changes we reload the versions
			reloadVersions();
		}
		includeKeyIsSelected = includeKeyJCheckBox.isSelected();
    	reloadSendJButtonState();
    }//GEN-LAST:event_includeKeyJCheckBoxStateChanged
    
    private void contactsJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_contactsJListValueChanged
    	if(!evt.getValueIsAdjusting()) {
			if(ListSelectionModel.SINGLE_SELECTION ==
				contactsSelectionModel.getSelectionMode()) {
				teamSelectionModel.clearSelection();
			}
		}
    	reloadSendJButtonState();
    }//GEN-LAST:event_contactsJListValueChanged
    
    private void teamJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_teamJListValueChanged
    	if(!evt.getValueIsAdjusting()) {
			if(ListSelectionModel.SINGLE_SELECTION ==
				teamSelectionModel.getSelectionMode()) {
				contactsSelectionModel.clearSelection();
			}
		}
    	reloadSendJButtonState();
    }//GEN-LAST:event_teamJListValueChanged

    /**
     * Extract the selected contacts.
     *
     */
    private List<Contact> extractContacts() {
		return SwingUtil.extract(contactsJList);
	}

    /**
     * Extract the doIncludeKey setting.
     *
     */
    private Boolean extractDoIncludeKey() {
		return includeKeyJCheckBox.isSelected();
	}

    /**
     * Extract the document id we are sending.
     *
     */
    private Long extractDocumentId() { return (Long) input; }

    /**
     * Extract the selected version.
     *
     */
    private DocumentVersion extractDocumentVersion() {
		return (DocumentVersion) versionJComboBox.getSelectedItem();
	}

    /**
     * Extract the selected team.
     *
     */
    private List<Contact> extractTeam() { return SwingUtil.extract(teamJList); }

    /**
     * Obtain a list of contacts from the content provider.
     *
     */
    private Contact[] getContacts(final Contact[] team) {
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
    private Contact[] getTeam() {
		return (Contact[]) ((CompositeFlatSingleContentProvider) contentProvider)
				.getElements(1, (Long) input);
	}

    /**
     * Obtain a list of versions from the content provider.
     *
     */
    private DocumentVersion[] getVersions() {
		return (DocumentVersion[]) ((CompositeFlatSingleContentProvider) contentProvider)
				.getElements(2, (Long) input);
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
	 * Proxy a contact list to a user list.
	 * 
	 * @param i
	 *            The contact list.
	 * @return The user list.
	 * @deprecated
	 */
	private List<User> proxy(final Iterable<Contact> i) {
		final List<User> l = new LinkedList<User>();
		for(final Contact c : i ) { l.add(c); }
		return l;
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
    	documentJLabel.setText(getString("DocumentLabel.Empty"));
    	if(null != input) {
    		final Document document = getDocument();
    		final Object[] arguments = new Object[] {document.getName()};
    		documentJLabel.setText(getString("DocumentLabel", arguments));
    	}
    }

    /**
     * Reload the include key model.
     *
     */
    private void reloadIncludeKey() {
		includeKeyJCheckBox.setVisible(false);
		if(null != input) {
			final Boolean isKeyHolder = ArtifactUtil
					.isKeyHolder(extractDocumentId());
			includeKeyJCheckBox.setVisible(isKeyHolder);
		}
	}

    /**
     * Set the enabled property of the send button based upon the validity
     * of the form's input.
     *
     */
    private void reloadSendJButtonState() {
    	sendJButton.setEnabled(isInputValid());
    }

    private void reloadTeamMembers() {
		teamModel.clear();
		if(null != input) {
			team = getTeam();
			loadUserList(teamModel, team);
		}
	}
    
    private void reloadVersions() {
		versionModel.removeAllElements();
		if(null != input) {
			// only include the working version if we are to send the key
			if(extractDoIncludeKey()) {
				versionModel.addElement(WorkingVersion.getWorkingVersion());
				versionJComboBox.setEnabled(false);
			}
			else {
				final DocumentVersion[] versions = getVersions();
				for (final DocumentVersion version : versions) {
					versionModel.addElement(version);
					versionJComboBox.setEnabled(true);
				}
			}
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
    private javax.swing.JList contactsJList;
    private javax.swing.JLabel documentJLabel;
    private javax.swing.JCheckBox includeKeyJCheckBox;
    private javax.swing.JButton sendJButton;
    private javax.swing.JPanel sendToJPanel;
    private javax.swing.JList teamJList;
    private javax.swing.JLabel vSpacerJLabel;
    private javax.swing.JComboBox versionJComboBox;
    // End of variables declaration//GEN-END:variables
    
}
