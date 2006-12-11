/*
 * Created On: September 22, 2006, 11:15 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.swing.SwingUtil;

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
    private javax.swing.JPanel buttonBarJPanel;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel commentJLabel;
    private javax.swing.JScrollPane commentJScrollPane;
    private javax.swing.JTextArea commentJTextArea;
    private javax.swing.JLabel documentJLabel;
    private javax.swing.JLabel documentNameJLabel;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JLabel filler1JLabel;
    private javax.swing.JList namesJList;
    private javax.swing.JScrollPane namesJScrollPane;
    private javax.swing.JPanel progressBarJPanel;
    private javax.swing.JButton publishJButton;
    private javax.swing.JProgressBar publishJProgressBar;
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
     * Get the avatar title.
     * 
     * @return the avatar title
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getAvatarTitle()
     */
    @Override
    public String getAvatarTitle() {
        final PublishType publishType;
        if (input==null) {
            publishType = PublishType.PUBLISH;
        } else {
            publishType = getInputPublishType();
        }
        
        if (publishType == PublishType.PUBLISH_VERSION) {
            return getString("PublishVersionTitle");
        } else {
            return getString("Title");
        }
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
            reloadExplanation();
            reloadComment();
            reloadJList();           
            publishJButton.setEnabled(isInputValid());
        }
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
        if (null != status && 0 < status.trim().length()) {
            documentJLabel.setText(getDocumentJLabelText());
            documentNameJLabel.setText(status);
        } else {
            /* NOTE the space is deliberate (as opposed to an empty string) in
             * order to maintain vertical spacing. */
            documentJLabel.setText(" ");
            documentNameJLabel.setText(" ");
        }
        /* NOTE The avatar assumes that at some point the worker will become
         * determinate. */
        if (publishJProgressBar.getValue() == publishJProgressBar.getMaximum()) {
            disposeWindow();
        }
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
        javax.swing.JLabel filler2JLabel;
        javax.swing.JLabel titleJLabel;

        explanationJLabel = new javax.swing.JLabel();
        commentJLabel = new javax.swing.JLabel();
        commentJScrollPane = new javax.swing.JScrollPane();
        commentJTextArea = new javax.swing.JTextArea();
        buttonBarJPanel = new javax.swing.JPanel();
        filler1JLabel = new javax.swing.JLabel();
        filler2JLabel = new javax.swing.JLabel();
        publishJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        progressBarJPanel = new javax.swing.JPanel();
        titleJLabel = new javax.swing.JLabel();
        documentJLabel = new javax.swing.JLabel();
        documentNameJLabel = new javax.swing.JLabel();
        publishJProgressBar = new javax.swing.JProgressBar();
        namesJScrollPane = new javax.swing.JScrollPane();
        namesJList = new javax.swing.JList();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("PublishContainerDialog.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        explanationJLabel.setFocusable(false);

        commentJLabel.setText(bundle.getString("PublishContainerDialog.Comment")); // NOI18N
        commentJLabel.setFocusable(false);

        commentJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentJTextArea.setFont(Fonts.DialogFont);
        commentJTextArea.setLineWrap(true);
        commentJTextArea.setWrapStyleWord(true);
        commentJScrollPane.setViewportView(commentJTextArea);

        buttonBarJPanel.setOpaque(false);
        filler1JLabel.setPreferredSize(new java.awt.Dimension(3, 14));

        filler2JLabel.setText(" ");

        publishJButton.setText(bundle.getString("PublishContainerDialog.publishJButton")); // NOI18N
        publishJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("PublishContainerDialog.Cancel")); // NOI18N
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
                    .add(filler1JLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, filler2JLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                .addContainerGap())
        );
        buttonBarJPanelLayout.setVerticalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(filler1JLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(7, 7, 7)
                .add(filler2JLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(publishJButton))
                .addContainerGap())
        );

        progressBarJPanel.setOpaque(false);
        titleJLabel.setText(bundle.getString("PublishContainerDialog.progressBarJPanel.titleJLabel")); // NOI18N

        documentJLabel.setText(getDocumentJLabelText());

        documentNameJLabel.setText("!My Document.doc!");

        publishJProgressBar.setBorder(new javax.swing.border.LineBorder(Colors.Browser.ProgressBar.BORDER, 1, true));

        org.jdesktop.layout.GroupLayout progressBarJPanelLayout = new org.jdesktop.layout.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .add(24, 24, 24)
                .add(documentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(documentNameJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
            .add(titleJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, publishJProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(titleJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(documentJLabel)
                    .add(documentNameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(publishJProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namesJScrollPane.setBorder(null);
        namesJScrollPane.setOpaque(false);
        namesJList.setModel(namesListModel);
        namesJList.setCellRenderer(new PublishContainerAvatarUserCellRenderer());
        namesJList.setOpaque(false);
        namesJList.setVisibleRowCount(6);
        namesJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                namesJListMouseClicked(evt);
            }
        });

        namesJScrollPane.setViewportView(namesJList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, progressBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(commentJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commentJScrollPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commentJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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

    private void namesJListMouseClicked(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_namesJListMouseClicked
        if (e.getButton() == MouseEvent.BUTTON1) {
            PublishContainerAvatarUser user = (PublishContainerAvatarUser)((JList) e.getSource()).getSelectedValue();
            user.toggleSelected();
            final int listModelIndex = namesListModel.indexOf(user);
            if (listModelIndex >= 0) {
                namesListModel.set(listModelIndex, user);
            }
            publishJButton.setEnabled(isInputValid());
        }
    }//GEN-LAST:event_namesJListMouseClicked

    /**
     * Publish the container.
     */
    private void publishContainer() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId();  
        final PublishContainerAvatarUserListModel model = (PublishContainerAvatarUserListModel) namesJList.getModel();
        final List<TeamMember> teamMembers = model.getSelectedTeamMembers();
        final List<Contact> contacts = model.getSelectedContacts();
        if (publishType == PublishType.PUBLISH_VERSION) {
            final Long versionId = getInputVersionId();
            getController().runPublishContainerVersion(containerId, versionId, teamMembers, contacts);   
        } else {
            getController().runPublishContainer(createMonitor(), containerId,
                    teamMembers, contacts, extractComment());  
        }  
    }

    private void publishJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishJButtonActionPerformed
        if (isInputValid()) {
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
     * Get most recent version id, or null if there is no version.
     */
    private Long readLatestVersionId(final Long containerId) {
        return ((PublishContainerProvider)contentProvider).readLatestVersionId(containerId);
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
     * Get the publish date.
     */
    private Calendar readPublishDate(final Long containerId, final Long versionId) {
        if ((null != containerId) && (null != versionId)) {
            return ((PublishContainerProvider)contentProvider).readPublishDate(containerId, versionId);
        } else {
            return null;
        }
    }
    
    /**
     * Get the publisher.
     */
    private User readPublisher() {
        final Long containerId = getInputContainerId();
        final Long versionId = getInputVersionId();
        if ((null != containerId) && (null != versionId)) {
            return ((PublishContainerProvider)contentProvider).readPublisher(containerId, versionId);
        } else {
            return null;
        }
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
     * Reload the explanation control.
     */
    private void reloadExplanation() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId();
        final Long versionId = getInputVersionId();
        final String name = ((PublishContainerProvider) contentProvider).readContainerName(containerId);
        if (publishType == PublishType.PUBLISH_VERSION) {
            final Calendar publishDate = readPublishDate(containerId, versionId);
            explanationJLabel.setText(getString("PublishVersionExplanation", new Object[] {publishDate.getTime(), name}));
        } else {
            explanationJLabel.setText(getString("Explanation", new Object[] {name}));    
        }
    }
    
    /**
     * Reload the jList.
     */
    private void reloadJList() {
        final List<TeamMember> teamMembers;
        final List<Contact> contacts;
        final Map<User, ArtifactReceipt> versionUsers;
        final User publisher;
        Boolean firstContact;
        
        if (getPublishTypeSpecific() == PublishTypeSpecific.PUBLISH_NOT_FIRST_TIME) {
            teamMembers = readTeamMembers();
            versionUsers = readLatestVersionUsers();
            publisher = readPublisher();
            firstContact = !teamMembers.isEmpty();
        } else {
            teamMembers = Collections.emptyList();
            versionUsers = null;
            publisher = null;
            firstContact = Boolean.FALSE;
        }     
        contacts = readContacts(teamMembers);
        
        namesListModel.clear();
        for (final TeamMember teamMember : teamMembers) {
            namesListModel.addElement(new PublishContainerAvatarUser(teamMember,
                    isVersionUser(teamMember, publisher, versionUsers)));
        }
        for (final Contact contact : contacts) {
            namesListModel.addElement(new PublishContainerAvatarUser(contact, Boolean.FALSE, firstContact));
            firstContact = Boolean.FALSE;
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
    
    public class PublishContainerAvatarUser {
        
        /** The user. */
        private User user;
        
        /** The selection status. */
        private Boolean selected;
        
        /** The first contact in the list. */
        private Boolean firstContact;
        
        public PublishContainerAvatarUser(final User user, final Boolean selected) {
            this(user, selected, Boolean.FALSE);
        }
        
        public PublishContainerAvatarUser(final User user, final Boolean selected, final Boolean firstContact) {
            this.user = user;
            this.selected = selected;
            this.firstContact = firstContact;
        }
        
        public User getUser() {
            return user;
        }
        
        public String getExtendedName() {
            return localization.getString("UserName",
                    new Object[] {user.getName(), user.getTitle(), user.getOrganization()} );
        }
        
        public Boolean isSelected() {
            return selected;
        }
        
        public void toggleSelected() {
            selected = !selected;
        }
        
        public Boolean isFirstContact() {
            return firstContact;
        }
    }
    
    private class PublishContainerAvatarUserListModel extends DefaultListModel {
        
        public PublishContainerAvatarUserListModel() {
            super();
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
    }
        
    public enum DataKey { CONTAINER_ID, PUBLISH_TYPE, VERSION_ID }    
    public enum PublishType { PUBLISH, PUBLISH_VERSION }
    private enum PublishTypeSpecific { PUBLISH_FIRST_TIME, PUBLISH_NOT_FIRST_TIME, PUBLISH_VERSION }
}
