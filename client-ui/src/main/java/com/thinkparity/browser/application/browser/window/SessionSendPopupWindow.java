/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.CheckBoxFactory;
import com.thinkparity.browser.application.browser.component.ComboBoxFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.session.UserListCellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.session.VersionListCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.model.document.WorkingVersion;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionSendPopupWindow extends JDialog {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The avatar's content provider.
	 * 
	 */
	protected ContentProvider contentProvider;

	/**
	 * The avatar input.
	 * 
	 */
	protected Object input;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// Variables declaration - do not modify
	private javax.swing.JButton cancelJButton;

	private javax.swing.JLabel contactJLabel;

	private javax.swing.JList contactsJList;

	private javax.swing.JScrollPane contactsJScrollPane;

	private DefaultListModel contactsModel;

	private DefaultListSelectionModel contactsSelectionModel;

	/**
	 * The main controller.
	 * 
	 */
	private Browser controller;

	private javax.swing.JCheckBox includeKeyJCheckBox;

	private javax.swing.JButton sendJButton;

	/**
	 * Used by the contact provider.
	 * 
	 * @see #reloadTeamMembers()
	 */
	private Contact[] team;

	private javax.swing.JLabel teamJLabel;

	private javax.swing.JList teamJList;

	private javax.swing.JScrollPane teamJScrollPane;

	private DefaultListModel teamModel;

	private DefaultListSelectionModel teamSelectionModel;

	private javax.swing.JComboBox versionJComboBox;

	private javax.swing.JLabel versionJLabel;

	private DefaultComboBoxModel versionModel;

	/**
	 * Create a SessionSendFormAvatar.
	 * 
	 */
	public SessionSendPopupWindow(final AbstractJFrame owner) {
		super(owner, "", true);
		initComponents();
	}

	/**
	 * Obtain the main controller.
	 *
	 */
	public Browser getController() {
		if(null == controller) { controller = Browser.getInstance(); }
		return controller;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() {
		return AvatarId.SESSION_SEND_FORM;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() {
		return null;
	}	

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

		if (null != documentId && 0 < contacts.size()) { return Boolean.TRUE; }
		else { return Boolean.FALSE; }
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		cancelJButton.setEnabled(true);
		sendJButton.setEnabled(true);
		reloadIncludeKey();
		reloadTeamMembers();
		reloadContacts();
		reloadVersions();
	}

	/**
	 * Set the content provider.
	 * 
	 * @param contentProvider
	 *            The content provider.
	 */
	public void setContentProvider(final ContentProvider contentProvider) {
		Assert.assertNotNull(
				"Cannot set a null content provider:  " + getId(), contentProvider);
		if(this.contentProvider == contentProvider
				|| contentProvider.equals(this.contentProvider)) { return; }
		
		this.contentProvider = contentProvider;
		reload();
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

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	// End of variables declaration

	/**
	 * Obtain a handle to the session api.
	 * 
	 * @return The session api.
	 */
	protected SessionModel getSessionModel() {
		return ModelFactory.getInstance().getSessionModel(getClass());
	}

	private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {
		getController().displayMainBrowserAvatar();
	}

	private void displayMainBrowserAvatar(final Boolean doReload) {
		if(doReload) { getController().reloadMainBrowserAvatar(); }
		getController().displayMainBrowserAvatar();
	}

	private Collection<Contact> extractContacts() {
		return SwingUtil.extract(contactsJList);
	}

	private Long extractDocumentId() {
		return (Long) input;
	}

	private DocumentVersion extractDocumentVersion() {
		return (DocumentVersion) versionJComboBox.getSelectedItem();
	}

	private Boolean extractDoIncludeKey() { return includeKeyJCheckBox.isSelected(); }

	private List<Contact> extractTeam() {
		return SwingUtil.extract(teamJList);
	}

	private Contact[] getContacts(final Contact[] team) {
		return (Contact[]) ((CompositeFlatContentProvider) contentProvider).getElements(0, team);
	}

	private Contact[] getTeam() {
		return (Contact[]) ((CompositeFlatContentProvider) contentProvider).getElements(1, (Long) input);
	}

	private DocumentVersion[] getVersions() {
		return (DocumentVersion[]) ((CompositeFlatContentProvider) contentProvider).getElements(2, (Long) input);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		versionModel = new DefaultComboBoxModel(new Vector(0));
		versionJLabel = LabelFactory.create();
		versionJComboBox = ComboBoxFactory.create();
		versionJComboBox.setModel(versionModel);
		versionJComboBox.setRenderer(new VersionListCellRenderer());
		teamJLabel = LabelFactory.create();
		contactJLabel = LabelFactory.create();

		teamModel = new DefaultListModel();
		teamSelectionModel = new DefaultListSelectionModel();
		teamSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					if(ListSelectionModel.SINGLE_SELECTION ==
						teamSelectionModel.getSelectionMode()) {
						contactsSelectionModel.clearSelection();
					}
				}
			}
		});
		teamJScrollPane = ScrollPaneFactory.create();

		teamJList = ListFactory.create();
		teamJList.setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		teamJList.setModel(teamModel);
		teamJList.setSelectionModel(teamSelectionModel);
		teamJList.setCellRenderer(new UserListCellRenderer());
		teamJList.setBackground(getBackground());

		contactsModel = new DefaultListModel();
		contactsSelectionModel = new DefaultListSelectionModel();
		contactsSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					if(ListSelectionModel.SINGLE_SELECTION ==
						contactsSelectionModel.getSelectionMode()) {
						teamSelectionModel.clearSelection();
					}
				}
			}

		});
		contactsJScrollPane = ScrollPaneFactory.create();
		
		contactsJList = ListFactory.create();
		contactsJList.setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		contactsJList.setModel(contactsModel);
		contactsJList.setSelectionModel(contactsSelectionModel);
		contactsJList.setCellRenderer(new UserListCellRenderer());
		contactsJList.setBackground(getBackground());
		
		includeKeyJCheckBox = CheckBoxFactory.create();
		includeKeyJCheckBox.addChangeListener(new ChangeListener() {
			Boolean previousIsSelected = includeKeyJCheckBox.isSelected();
			public void stateChanged(final ChangeEvent e) {
				if(previousIsSelected != includeKeyJCheckBox.isSelected()) {
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
				previousIsSelected = includeKeyJCheckBox.isSelected();
			}
			
		});
		cancelJButton = ButtonFactory.create();
		sendJButton = ButtonFactory.create();

		versionJLabel.setText("Version:");

		teamJLabel.setText("Team Members:");

		contactJLabel.setText("Contacts:");

		teamJScrollPane.setViewportView(teamJList);

		contactsJScrollPane.setViewportView(contactsJList);

		includeKeyJCheckBox.setText("Include Ownership");
		includeKeyJCheckBox.setBorder(javax.swing.BorderFactory
				.createEmptyBorder(0, 0, 0, 0));
		includeKeyJCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
		includeKeyJCheckBox.setOpaque(false);

		cancelJButton.setText("Cancel");
		cancelJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelJButtonActionPerformed(evt);
			}
		});

		sendJButton.setText("Send");
		sendJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendJButtonActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								versionJLabel)
																						.add(
																								teamJLabel)
																						.add(
																								contactJLabel))
																		.add(
																				21,
																				21,
																				21)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.TRAILING)
																						.add(
																								contactsJScrollPane,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								282,
																								Short.MAX_VALUE)
																						.add(
																								teamJScrollPane,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								282,
																								Short.MAX_VALUE)
																						.add(
																								versionJComboBox,
																								0,
																								282,
																								Short.MAX_VALUE)
																						.add(
																								org.jdesktop.layout.GroupLayout.LEADING,
																								includeKeyJCheckBox)))
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				sendJButton)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				cancelJButton)))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(versionJLabel)
														.add(
																versionJComboBox,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(teamJLabel)
														.add(
																teamJScrollPane,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																82,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				contactsJScrollPane,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				83,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				includeKeyJCheckBox)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.BASELINE)
																						.add(
																								cancelJButton)
																						.add(
																								sendJButton)))
														.add(contactJLabel))
										.addContainerGap(
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
	}// </editor-fold>

	private void loadUserList(final DefaultListModel listModel,
			final User[] users) {
		for(final User user : users) {
			logger.debug("Adding user:  " + user.getSimpleUsername());
			listModel.addElement(user);
		}
	}

	// TODO Fix this.
	private List<User> proxy(final Iterable<Contact> i) {
		final List<User> l = new LinkedList<User>();
		for(final Contact c : i ) { l.add(c); }
		return l;
	}

	private void reloadContacts() {
		contactsModel.clear();
		if(null != input)
			loadUserList(contactsModel, getContacts(team));
	}

	/**
	 * Reload the include key checkbox.
	 *
	 */
	private void reloadIncludeKey() {
		includeKeyJCheckBox.setVisible(false);
		if(null != input) {
			final Boolean isKeyHolder = ArtifactUtil.isKeyHolder(extractDocumentId());
			includeKeyJCheckBox.setVisible(isKeyHolder);
		}
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
				for(final DocumentVersion version : versions) {
					versionModel.addElement(version);
					versionJComboBox.setEnabled(true);
				}				
			}
		}
	}

	private void sendJButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if(isInputValid()) {
			final Long documentId = extractDocumentId();
			final List<User> contacts = proxy(extractTeam());
			contacts.addAll(proxy(extractContacts()));
			final Boolean doIncludeKey = extractDoIncludeKey();
			try {
				if(doIncludeKey) {
					// create a version and send it
					// update the server key holder
					final User user = contacts.get(0);
					// TODO Refactor the user object.
					final JabberId jabberId = JabberIdBuilder.parseUsername(user.getSimpleUsername());
					getSessionModel().sendKeyResponse(documentId, jabberId, KeyResponse.ACCEPT);
					displayMainBrowserAvatar(Boolean.TRUE);
				}
				else {
					final DocumentVersion version = extractDocumentVersion();
					if(version == WorkingVersion.getWorkingVersion()) {
						// create a version and send it
						getSessionModel().send(contacts, documentId);
						displayMainBrowserAvatar(Boolean.TRUE);
					}
					else {
						// send a specific version
						getSessionModel().send(
								contacts, documentId, version.getVersionId());
						displayMainBrowserAvatar(Boolean.TRUE);
					}
				}
			}
			catch(final ParityException px) { throw new RuntimeException(px); }
			finally {  }
		}
	}
}
