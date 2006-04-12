/*
 * SessionSendVersion.java
 *
 * Created on March 29, 2006, 11:06 AM
 */

package com.thinkparity.browser.application.browser.display.avatar.session;

import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.codebase.Pair;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 *
 * @author raykroeker@gmail.com
 */
public class SessionSendVersion extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    private final DefaultListModel teamModel;

    /**
     * Creates new form SessionSendVersion
     *
     */
    public SessionSendVersion() {
        super("SendVersion", BrowserConstants.DIALOGUE_BACKGROUND);
        this.teamModel = new DefaultListModel();
        initComponents();
    }

    /**
     * Reload the avatar.
     *
     */
    public void reload() {
        reloadDocument();
        reloadDocumentVersion();
        reloadTeamMembers();

        reloadSendJButtonState();
    }

    /**
     * Set the avatar state.
     * 
     * 
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) {}

    /**
     * Obtain the avatar's state information.
     * 
     * 
     * @return The avatar's state information.
     */
    public State getState() { return null; }

    /**
     * Obtain the avatar id.
     * 
     * 
     * @return The avatar id.
     */
    public AvatarId getId() { return AvatarId.SESSION_SEND_VERSION; }

    /**
	 * @see com.thinkparity.browser.javax.swing.AbstractJPanel#isInputValid()
	 * 
	 */
	public Boolean isInputValid() {
		Long documentId;
		try { documentId = getInputArtifactId(); }
		catch(final Throwable t) { return Boolean.FALSE; }

		List<Contact> team = null;
		try { team = extractTeam(); }
		catch(final Throwable t) { return Boolean.FALSE; }

		final Long versionId;
		try { versionId = getInputVersionId(); }
		catch(final Throwable t) { return Boolean.FALSE; }

		if(null != documentId && null != versionId && 0 < team.size()) {
			return Boolean.TRUE;
		}
		else { return Boolean.FALSE; }
	}

	/**
     * Extract the selected team members from the list.
     * 
     * @return The selected team members.
     */
	private List<Contact> extractTeam() { return SwingUtil.extract(teamJList); }

	/**
     * Obtain the document from the provider.
     *
     */
    private Document getDocument() {
        return (Document) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, getInputArtifactId());
    }

    private DocumentVersion getDocumentVersion() {
        return (DocumentVersion) ((CompositeFlatSingleContentProvider) contentProvider).getElement(1, new Pair(getInputArtifactId(), getInputVersionId()));
    }

    private Long getInputArtifactId() {
        return (Long) ((Data) input).get(DataKey.ARTIFACT_ID);
    }

    private Long getInputVersionId() {
        return (Long) ((Data) input).get(DataKey.VERSION_ID);
    }

    private User[] getTeam() {
        return (User[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, getInputArtifactId());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JSeparator jSeparator1;
        javax.swing.JPanel sendToJPanel;
        javax.swing.JLabel teamJLabel;
        javax.swing.JScrollPane teamJScrollPane;

        documentJLabel = LabelFactory.create();
        versionJLabel = LabelFactory.create();
        jSeparator1 = new javax.swing.JSeparator();
        sendJButton = ButtonFactory.create(getString("SendButton"));
        cancelJButton = ButtonFactory.create(getString("CancelButton"));
        sendToJPanel = new javax.swing.JPanel();
        teamJLabel = LabelFactory.create(getString("TeamLabel"));
        teamJScrollPane = new javax.swing.JScrollPane();
        teamJList = ListFactory.create();

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

        sendToJPanel.setOpaque(false);

        teamJList.setCellRenderer(new UserListCellRenderer());
        teamJList.setModel(teamModel);
        teamJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                teamJListValueChanged(e);
            }
        });

        teamJScrollPane.setViewportView(teamJList);

        org.jdesktop.layout.GroupLayout sendToJPanelLayout = new org.jdesktop.layout.GroupLayout(sendToJPanel);
        sendToJPanel.setLayout(sendToJPanelLayout);
        sendToJPanelLayout.setHorizontalGroup(
            sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(teamJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, teamJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );
        sendToJPanelLayout.setVerticalGroup(
            sendToJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sendToJPanelLayout.createSequentialGroup()
                .add(teamJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(teamJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, sendToJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, documentJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sendJButton))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, versionJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(documentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(versionJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendToJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sendJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
    	disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void disposeWindow() {
    	SwingUtilities.getWindowAncestor(this).dispose();
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

	private void sendJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_sendJButtonActionPerformed
    	if(isInputValid()) {
    		toggleVisualFeedback(Boolean.TRUE);
    		try {
    			getSessionModel().send(proxy(extractTeam()), getInputArtifactId(), getInputVersionId());
    		}
    		catch(final ParityException px) { throw new RuntimeException(px); }
    		finally { 
    			toggleVisualFeedback(Boolean.FALSE);
    			getController().fireDocumentUpdated(getInputArtifactId());
    			disposeWindow();
    		}
    	}
    }//GEN-LAST:event_sendJButtonActionPerformed

    private void loadUserList(final DefaultListModel listModel, final User[] users) {
        for(final User user : users) { listModel.addElement(user); }
    }

    private void reloadDocument() {
        documentJLabel.setText(getString("DocumentLabel.Empty"));
        if(null != input) {
            final Document document = getDocument();
            final Object[] arguments = new Object[] {document.getName()};
            documentJLabel.setText(getString("DocumentLabel", arguments));
        }
    }

    private void reloadDocumentVersion() {
        versionJLabel.setText(getString("VersionLabel.Empty"));
        if(null != input) {
            final DocumentVersion version = getDocumentVersion();
            final Object[] arguments = new Object[] {version.getVersionId()};
            versionJLabel.setText(getString("VersionLabel", arguments));
        }
    }

    private void reloadSendJButtonState() {
    	sendJButton.setEnabled(isInputValid());
    }

    private void reloadTeamMembers() {
        teamModel.clear();
        if(null != input) {
            loadUserList(teamModel, getTeam());
        }
    }

    private void teamJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_teamJListValueChanged
    	reloadSendJButtonState();
    }//GEN-LAST:event_teamJListValueChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel documentJLabel;
    private javax.swing.JButton sendJButton;
    private javax.swing.JList teamJList;
    private javax.swing.JLabel versionJLabel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { ARTIFACT_ID, VERSION_ID }
}
