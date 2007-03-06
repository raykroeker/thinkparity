/*
 * Created On: September 22, 2006, 11:15 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.container.PublishContainerAvatarUserCellRenderer;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com, raymond@thinkparity.com
 * @verison 1.1.2.18
 */
public final class PublishContainerAvatar extends Avatar implements
        PublishContainerSwingDisplay {

    /** The names list model <code>PublishContainerAvatarUserListModel</code>. */
    private final PublishContainerAvatarUserListModel namesListModel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JPanel buttonBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JTextArea commentJTextArea = new javax.swing.JTextArea();
    private final javax.swing.JLabel documentJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel documentNameJLabel = new javax.swing.JLabel();
    private final javax.swing.JList namesJList = new javax.swing.JList();
    private final javax.swing.JScrollPane namesJScrollPane = new javax.swing.JScrollPane();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JButton publishJButton = new javax.swing.JButton();
    private final javax.swing.JProgressBar publishJProgressBar = new javax.swing.JProgressBar();
    // End of variables declaration//GEN-END:variables

    /**
     * Creates PublishContainerAvatar.
     *
     */
    public PublishContainerAvatar() {
        super("PublishContainerDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        this.namesListModel = new PublishContainerAvatarUserListModel();
        initComponents();
        bindEscapeKey();
        namesJScrollPane.getViewport().setOpaque(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_PUBLISH;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() {
        return null;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#installProgressBar(java.lang.Long)
     *
     */
    public void installProgressBar(final Long containerId) {
        publishJProgressBar.setIndeterminate(true);
        progressBarJPanel.setVisible(true);
        buttonBarJPanel.setVisible(false);
        validate();
    }
    
    /**
     * Determine whether the user input for the dialog is valid.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {        
        final PublishContainerAvatarUserListModel model = (PublishContainerAvatarUserListModel) namesJList.getModel();
        return (model.isItemSelected());
    }
    
    public void reload() {
        reloadProgressBar();
        if (input != null) { 
            reloadComment();
            reloadJList();           
            publishJButton.setEnabled(isInputValid());
            namesJScrollPane.getViewport().setViewPosition(new Point(0,0));
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#resetProgressBar(java.lang.Long)
     * 
     */
    public void resetProgressBar(final Long containerId) {
        reloadProgressBar();
        publishJButton.setEnabled(isInputValid());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#setDetermination(java.lang.Long, java.lang.Integer)
     *
     */
    public void setDetermination(final Long containerId, final Integer steps) {
        publishJProgressBar.setMinimum(1);
        publishJProgressBar.setMaximum(steps);
        publishJProgressBar.setIndeterminate(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(State state) {        
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#updateProgress(java.lang.Long, java.lang.Integer, java.lang.String)
     *
     */
    public void updateProgress(final Long containerId, final Integer step,
            final String status) {
        publishJProgressBar.setValue(step);
        if (null != status) {
            documentJLabel.setText(getDocumentJLabelText());
            documentNameJLabel.setText(status);
        } else {
            /* NOTE the space is deliberate (as opposed to an empty string) in
             * order to maintain vertical spacing. */
            documentJLabel.setText(" ");
            documentNameJLabel.setText(" ");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerSwingDisplay#dispose()
     *
     */
    public void dispose() {
        disposeWindow();
    }

    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            /** @see java.io.Serializable */
            private static final long serialVersionUID = 1;

            /** @see javax.swing.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    private ThinkParitySwingMonitor createMonitor() {
        return new PublishContainerSwingMonitor(this, getInputContainerId());
    }
    
    /**
     * Extract the user comment.
     * 
     * @return The user comment <code>String</code>.
     */
    private String extractComment() {
        return SwingUtil.extract(commentJTextArea);
    }
    
    /**
     * Obtain the text for the document label.
     * 
     * @return A text <code>String<code>.
     */
    private String getDocumentJLabelText() {
        final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages");
        return bundle.getString("PublishContainerDialog.progressBarJPanel.documentJLabel");        
    }
    
    /**
     * Obtain the input container id.
     *
     * @return A container id.
     */
    private Long getInputContainerId() {
        if (input!=null) {
            return (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        } else {
            return null;
        }
    }
    
    /**
     * Obtain the input publish type.
     *
     * @return A PublishType.
     */
    private PublishType getInputPublishType() {
        if (input!=null) {
            return (PublishType) ((Data) input).get(DataKey.PUBLISH_TYPE);
        } else {
            return null;
        }
    }
    
    /**
     * Obtain the input version id.
     *
     * @return A version id.
     */
    private Long getInputVersionId() {
        if (input!=null) {
            return (Long) ((Data) input).get(DataKey.VERSION_ID);
        } else {
            return null;
        }
    }
    
    /**
     * Obtain the specific publish type.
     * 
     * @return A PublishTypeSpecific.
     */
    private PublishTypeSpecific getPublishTypeSpecific() {
        final PublishType publishType = getInputPublishType();
        if (publishType==PublishType.PUBLISH_VERSION) {
            return PublishTypeSpecific.PUBLISH_VERSION;
        } else {
            final Long containerId = getInputContainerId();
            final Long versionId = readLatestVersionId(containerId);
            if (null == versionId) {
                return PublishTypeSpecific.PUBLISH_FIRST_TIME;
            } else {
                return PublishTypeSpecific.PUBLISH_NOT_FIRST_TIME;
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
        final javax.swing.JLabel emailsJLabel = new javax.swing.JLabel();
        final javax.swing.JScrollPane emailsJScrollPane = new javax.swing.JScrollPane();
        final javax.swing.JTextArea emailsJTextArea = new javax.swing.JTextArea();
        final javax.swing.JLabel commentJLabel = new javax.swing.JLabel();
        final javax.swing.JScrollPane commentJScrollPane = new javax.swing.JScrollPane();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();
        final javax.swing.JButton cancelJButton = new javax.swing.JButton();

        namesJScrollPane.setBorder(null);
        namesJScrollPane.setOpaque(false);
        namesJList.setFont(Fonts.DialogFont);
        namesJList.setModel(namesListModel);
        namesJList.setCellRenderer(new PublishContainerAvatarUserCellRenderer());
        namesJList.setOpaque(false);
        namesJList.setVisibleRowCount(7);
        namesJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                namesJListMousePressed(evt);
            }
        });

        namesJScrollPane.setViewportView(namesJList);

        emailsJLabel.setFont(Fonts.DialogFont);
        emailsJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("PublishContainerDialog.Emails"));

        emailsJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        emailsJTextArea.setColumns(20);
        emailsJTextArea.setFont(Fonts.DialogTextEntryFont);
        emailsJTextArea.setLineWrap(true);
        emailsJScrollPane.setViewportView(emailsJTextArea);

        commentJLabel.setFont(Fonts.DialogFont);
        commentJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("PublishContainerDialog.Comment"));
        commentJLabel.setFocusable(false);

        commentJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentJTextArea.setFont(Fonts.DialogTextEntryFont);
        commentJTextArea.setLineWrap(true);
        commentJTextArea.setWrapStyleWord(true);
        commentJScrollPane.setViewportView(commentJTextArea);

        buttonBarJPanel.setOpaque(false);
        fillerJLabel.setFont(Fonts.DialogFont);
        fillerJLabel.setPreferredSize(new java.awt.Dimension(3, 17));

        publishJButton.setFont(Fonts.DialogButtonFont);
        publishJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("PublishContainerDialog.publishJButton"));
        publishJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("PublishContainerDialog.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout buttonBarJPanelLayout = new org.jdesktop.layout.GroupLayout(buttonBarJPanel);
        buttonBarJPanel.setLayout(buttonBarJPanelLayout);
        buttonBarJPanelLayout.setHorizontalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                        .add(publishJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton))
                    .add(buttonBarJPanelLayout.createSequentialGroup()
                        .add(fillerJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                        .add(71, 71, 71))))
        );
        buttonBarJPanelLayout.setVerticalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(fillerJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(publishJButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        progressBarJPanel.setOpaque(false);
        documentJLabel.setFont(Fonts.DialogFont);
        documentJLabel.setText(getDocumentJLabelText());

        documentNameJLabel.setFont(Fonts.DialogFont);
        documentNameJLabel.setText("!My Document.doc!");

        publishJProgressBar.setBorder(javax.swing.BorderFactory.createLineBorder(Colors.Browser.ProgressBar.BORDER));

        org.jdesktop.layout.GroupLayout progressBarJPanelLayout = new org.jdesktop.layout.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .add(documentJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(documentNameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(publishJProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(documentJLabel)
                    .add(documentNameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(publishJProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(emailsJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(emailsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(commentJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(commentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(emailsJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(emailsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commentJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private Boolean isVersionUser(final User user, final User publisher, final Map<User, ArtifactReceipt> versionUsers) {
        if (null != publisher) {
            if (publisher.getId().equals(user.getId())) {
                return Boolean.TRUE;
            }
        }
        if (null != versionUsers) {
            for (final User versionUser : versionUsers.keySet()) {
                if (versionUser.getId().equals(user.getId())) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;       
    }

    private void namesJListMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_namesJListMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            PublishContainerAvatarUser user = (PublishContainerAvatarUser)((JList) e.getSource()).getSelectedValue();
            user.toggleSelected();
            final int listModelIndex = namesListModel.indexOf(user);
            if (listModelIndex >= 0) {
                namesListModel.set(listModelIndex, user);
            }
            publishJButton.setEnabled(isInputValid());
        }
    }//GEN-LAST:event_namesJListMousePressed

    /**
     * Publish the container.
     */
    private void publishContainer() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId();  
        final PublishContainerAvatarUserListModel model = (PublishContainerAvatarUserListModel) namesJList.getModel();
        final List<TeamMember> teamMembers = model.getSelectedTeamMembers();
        final List<Contact> contacts = model.getSelectedContacts();
        
        // Publish
        if (publishType == PublishType.PUBLISH_VERSION) {
            final Long versionId = getInputVersionId();
            getController().runPublishContainerVersion(createMonitor(),
                    containerId, versionId, teamMembers, contacts);   
        } else {
            getController().runPublishContainer(createMonitor(), containerId,
                    teamMembers, contacts, extractComment());  
        }  
    }

    private void publishJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishJButtonActionPerformed
        if (isInputValid()) {
            publishJButton.setEnabled(false);
            publishContainer();
        }
    }//GEN-LAST:event_publishJButtonActionPerformed
    
    /**
     * Read contacts. The list does not include any contacts
     * that are in the list of team members.
     * 
     * @param teamMembers
     *          The list of team members.
     * 
     * @return The list of contacts.
     */
    private List<Contact> readContacts(List<TeamMember> teamMembers) {
        final List<Contact> allContacts = ((PublishContainerProvider)contentProvider).readContacts();
        final List<Contact> contacts = new LinkedList <Contact>();
        for (final Contact contact : allContacts) {
            Boolean found = Boolean.FALSE;
            if (null != teamMembers) {
                for (final TeamMember teamMember : teamMembers) {
                    if (teamMember.getId().equals(contact.getId())) {
                        found = Boolean.TRUE;
                        break;
                    }
                }
            }
            if (!found) {
                contacts.add(contact);
            }
        }
        return contacts;
    }
    
    /**
     * Get the publisher of the most recent version.
     */
    private User readLatestPublisher() {
        final Long containerId = getInputContainerId();
        final Long versionId = readLatestVersionId(containerId);
        if ((null != containerId) && (null != versionId)) {
            return ((PublishContainerProvider)contentProvider).readPublisher(containerId, versionId);
        } else {
            return null;
        }
    } 
    
    /**
     * Get most recent version id, or null if there is no version.
     */
    private Long readLatestVersionId(final Long containerId) {
        return ((PublishContainerProvider)contentProvider).readLatestVersionId(containerId);
    }    
    
    /**
     * Read users that got this version.
     */
    private Map<User, ArtifactReceipt> readLatestVersionUsers() {
        final Long containerId = getInputContainerId();
        final Long versionId = readLatestVersionId(containerId);
        if ((null != containerId) && (null != versionId)) {
            return ((PublishContainerProvider)contentProvider).readVersionUsers(containerId, versionId);
        } else {
            return null;
        }
    }
    
    /**
     * Read the profile.
     * 
     * @return The profile.
     */
    private Profile readProfile() {
        final Profile profile = (Profile) ((PublishContainerProvider)contentProvider).readProfile();
        return profile;
    }
    
    /**
     * Read team members. The current user is removed.
     * When forwarding, an empty list is returned.
     * When this is the first publish, an empty list is returned.
     *              
     * @return The list of team members.
     */
    private List<TeamMember> readTeamMembers() {
        final List<TeamMember> teamMembers = new LinkedList <TeamMember>();
        final Long containerId = getInputContainerId();
        final Profile profile = readProfile();  
        final List<TeamMember> allTeamMembers = ((PublishContainerProvider)contentProvider).readTeamMembers(containerId);
        for (final TeamMember teamMember : allTeamMembers) {
            if (!teamMember.getId().equals(profile.getId())) {
                teamMembers.add(teamMember);
            }
        }
        
        return teamMembers;
    }
    
    /**
     * Reload the comment control.
     * Normally the control is blank and editable. If PUBLISH_VERSION then
     * load the existing comment and don't allow edit.
     */
    private void reloadComment() {
        final PublishType publishType = getInputPublishType();
        if (publishType == PublishType.PUBLISH_VERSION) {
            final Long containerId = getInputContainerId();
            final Long versionId = getInputVersionId();
            final String comment = ((PublishContainerProvider) contentProvider).readContainerVersionComment(containerId, versionId);
            commentJTextArea.setText(comment);
            commentJTextArea.setEditable(false);
            commentJTextArea.setFocusable(false);
        } else {
            commentJTextArea.setText(null);
            commentJTextArea.setEditable(true);
            commentJTextArea.setFocusable(true);
        }
    }
    
    /**
     * Reload the jList.
     */
    private void reloadJList() {
        final List<TeamMember> teamMembers;
        final List<Contact> contacts;
        final Boolean selectLatestVersionUsers;
        
        switch (getPublishTypeSpecific()) {
        case PUBLISH_FIRST_TIME:
            selectLatestVersionUsers = Boolean.FALSE;            
            teamMembers = Collections.emptyList();
            break;
        case PUBLISH_NOT_FIRST_TIME:
            selectLatestVersionUsers = Boolean.TRUE;            
            teamMembers = readTeamMembers();
            break;
        case PUBLISH_VERSION:
            selectLatestVersionUsers = Boolean.FALSE;            
            teamMembers = readTeamMembers();
            break;
        default:
            throw Assert.createUnreachable("Unknown publish type");
        }
         
        contacts = readContacts(teamMembers);        
        namesListModel.clear();
        
        // Populate team members
        if (selectLatestVersionUsers) {
            final User publisher = readLatestPublisher();
            final Map<User, ArtifactReceipt> versionUsers = readLatestVersionUsers();
            for (final TeamMember teamMember : teamMembers) {
                namesListModel.addElement(new PublishContainerAvatarUser(
                        teamMember, isVersionUser(teamMember, publisher, versionUsers)));
            }
        } else {
            for (final TeamMember teamMember : teamMembers) {
                namesListModel.addElement(new PublishContainerAvatarUser(
                        teamMember, Boolean.FALSE));
            }
        }
        // Add a spacer if necessary to separate team members from contacts
        if (!teamMembers.isEmpty()) {
            namesListModel.addElement(new PublishContainerAvatarUser(null, Boolean.FALSE));
        }
        // Populate contacts
        for (final Contact contact : contacts) {
            namesListModel.addElement(new PublishContainerAvatarUser(contact, Boolean.FALSE));
        }
    }
    
    private void reloadProgressBar() {
        buttonBarJPanel.setVisible(true);
        progressBarJPanel.setVisible(false);
        /* NOTE the space is deliberate (as opposed to an empty string) in
         * order to maintain vertical spacing. */
        documentJLabel.setText(" ");
        documentNameJLabel.setText(" ");
        validate();
    }
    
    public enum DataKey { CONTAINER_ID, PUBLISH_TYPE, VERSION_ID }
    
    public class PublishContainerAvatarUser {
        
        /** The selection status. */
        private Boolean selected;
        
        /** The user. */
        private User user;
        
        public PublishContainerAvatarUser(final User user, final Boolean selected) {
            this.user = user;
            this.selected = selected;
        }
        
        public String getExtendedName() {
            return localization.getString("UserName",
                    new Object[] {user.getName(), user.getTitle(), user.getOrganization()} );
        }
        
        public User getUser() {
            return user;
        }
        
        public Boolean isSelected() {
            if (null == user) {
                return Boolean.FALSE;
            } else {
                return selected;
            }
        }
        
        public void toggleSelected() {
            selected = !selected;
        }
    }
        
    public enum PublishType { PUBLISH, PUBLISH_VERSION }    
    private class PublishContainerAvatarUserListModel extends DefaultListModel {
        
        public PublishContainerAvatarUserListModel() {
            super();
        }
        
        public List<Contact> getSelectedContacts() {
            List<Contact> selectedContacts = new ArrayList<Contact>();
            for (int index = 0; index < getSize(); index++) {
                PublishContainerAvatarUser user = (PublishContainerAvatarUser)getElementAt(index);
                if (user.isSelected() && (user.getUser() instanceof Contact)) {
                    selectedContacts.add((Contact)user.getUser());
                }                
            }

            return selectedContacts;
        }
        
        public List<TeamMember> getSelectedTeamMembers() {
            List<TeamMember> selectedTeamMembers = new ArrayList<TeamMember>();
            for (int index = 0; index < getSize(); index++) {
                PublishContainerAvatarUser user = (PublishContainerAvatarUser)getElementAt(index);
                if (user.isSelected() && (user.getUser() instanceof TeamMember)) {
                    selectedTeamMembers.add((TeamMember)user.getUser());
                }                
            }
            
            return selectedTeamMembers;
        }
        
        public Boolean isItemSelected() {
            for (int index = 0; index < getSize(); index++) {
                PublishContainerAvatarUser user = (PublishContainerAvatarUser)getElementAt(index);
                if (user.isSelected()) {
                    return Boolean.TRUE;
                }
            }
            
            return Boolean.FALSE;
        }
    }
    private enum PublishTypeSpecific { PUBLISH_FIRST_TIME, PUBLISH_NOT_FIRST_TIME, PUBLISH_VERSION }
}
