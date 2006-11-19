/*
 * Created On: September 22, 2006, 11:15 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.TableSorter;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.user.TeamMember;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityProgressBarUI;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com, raymond@thinkparity.com
 * @verison 1.1.2.18
 */
public final class PublishContainerAvatar extends Avatar implements
        PublishContainerSwingDisplay {

    /**
     * Creates PublishContainerAvatar.
     *
     */
    public PublishContainerAvatar() {
        super("PublishContainerDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        namesJScrollPane.getViewport().setBackground(BrowserConstants.DIALOGUE_BACKGROUND);
        namesJTable.setBackground(BrowserConstants.DIALOGUE_BACKGROUND);
        namesJTable.getTableHeader().setDefaultRenderer(new PublishTableHeaderRenderer(namesJTable.getTableHeader()));
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
        publishJProgressBar.setUI(new ThinkParityProgressBarUI());
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
        Boolean valid = Boolean.FALSE;
        TableSorter sorter = (TableSorter)namesJTable.getModel();
        CustomTableModel model = (CustomTableModel)(sorter.getTableModel());
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0) == Boolean.TRUE) {
                valid = Boolean.TRUE;
                break;
            }
        }
        return valid;
    }

    public void reload() {
        reloadProgressBar();
        if (input != null) { 
            reloadExplanation();
            reloadComment();
            final PublishType publishType = getInputPublishType();
            final Long containerId = getInputContainerId();
            final Long versionId;
            if (publishType==PublishType.PUBLISH_VERSION) {
                versionId = getInputVersionId();
            } else {
                versionId = getLatestVersionId(containerId);
            }            
            TableSorter sorter = new TableSorter(new CustomTableModel(publishType, containerId, versionId));
            namesJTable.setModel(sorter);
            sorter.setTableHeader(namesJTable.getTableHeader());
            initColumnSizes(namesJTable);
            publishJButton.setEnabled(isInputValid());
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getComponentsThatSupportMouseMove()
     */
    @Override
    protected List<Component> getComponentsThatSupportMouseMove() {
        List<Component> componentsThatSupportMouseMove = new ArrayList<Component>();
        componentsThatSupportMouseMove.add(explanationJLabel);
        return componentsThatSupportMouseMove;
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
        return SwingUtil.extract(commentJTextField);
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
     * Obtain the input container id.
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
     * Get most recent version id, or null if there is no version.
     */
    private Long getLatestVersionId(final Long containerId) {
        return ((PublishContainerProvider)contentProvider).getLatestVersionId(containerId);
    }
    
    /**
     * Get the publish date.
     */
    private Calendar getPublishDate(final Long containerId, final Long versionId) {
        if (null==versionId) {
            // True if there is no published version yet.
            return null;
        } else {
            return ((PublishContainerProvider)contentProvider).getPublishDate(containerId, versionId);
        }
    }
    
    /**
     * Get the publisher.
     */
    private User getPublisher(final Long containerId, final Long versionId) {
        if (null==versionId) {
            // True if there is no published version yet.
            return null;
        } else {
            return ((PublishContainerProvider)contentProvider).getPublisher(containerId, versionId);
        }
    }
    
    /**
     * This method picks good column sizes.
     * 
     * @param table
     *          The table.
     */
    private void initColumnSizes(javax.swing.JTable table) {
        TableSorter sorter = (TableSorter)table.getModel();
        CustomTableModel model = (CustomTableModel)(sorter.getTableModel());
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < model.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                                 table, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, model.getLongValue(i),
                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel jLabel1;
        javax.swing.JLabel jLabel2;
        javax.swing.JLabel titleJLabel;

        explanationJLabel = new javax.swing.JLabel();
        commentJLabel = new javax.swing.JLabel();
        commentJTextField = new javax.swing.JTextField();
        namesJScrollPane = new javax.swing.JScrollPane();
        namesJTable = new PublishJTable();
        buttonBarJPanel = new javax.swing.JPanel();
        cancelJButton = new javax.swing.JButton();
        publishJButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        progressBarJPanel = new javax.swing.JPanel();
        titleJLabel = new javax.swing.JLabel();
        documentJLabel = new javax.swing.JLabel();
        documentNameJLabel = new javax.swing.JLabel();
        publishJProgressBar = new javax.swing.JProgressBar();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("PublishContainerDialog.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        explanationJLabel.setFocusable(false);

        commentJLabel.setText(bundle.getString("PublishContainerDialog.Comment")); // NOI18N
        commentJLabel.setFocusable(false);

        namesJScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 239, 250)));
        namesJTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        namesJTable.setRowSelectionAllowed(false);
        namesJTable.setShowHorizontalLines(false);
        namesJTable.setShowVerticalLines(false);
        namesJScrollPane.setViewportView(namesJTable);

        buttonBarJPanel.setOpaque(false);
        cancelJButton.setText(bundle.getString("PublishContainerDialog.Cancel")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        publishJButton.setText(bundle.getString("PublishContainerDialog.publishJButton")); // NOI18N
        publishJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishJButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(" ");

        jLabel2.setText(" ");

        org.jdesktop.layout.GroupLayout buttonBarJPanelLayout = new org.jdesktop.layout.GroupLayout(buttonBarJPanel);
        buttonBarJPanel.setLayout(buttonBarJPanelLayout);
        buttonBarJPanelLayout.setHorizontalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .add(cancelJButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(publishJButton))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        buttonBarJPanelLayout.linkSize(new java.awt.Component[] {cancelJButton, publishJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        buttonBarJPanelLayout.setVerticalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(publishJButton)
                    .add(cancelJButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        progressBarJPanel.setOpaque(false);
        titleJLabel.setText(bundle.getString("PublishContainerDialog.progressBarJPanel.titleJLabel")); // NOI18N

        documentJLabel.setText(getDocumentJLabelText());

        documentNameJLabel.setText("!My Document.doc!");

        publishJProgressBar.setBorder(new javax.swing.border.LineBorder(Colors.Browser.ProgressBar.BORDER, 1, true));
        publishJProgressBar.setOpaque(false);

        org.jdesktop.layout.GroupLayout progressBarJPanelLayout = new org.jdesktop.layout.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(titleJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(documentJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(documentNameJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, publishJProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                    .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(commentJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(commentJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                    .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(commentJLabel)
                    .add(commentJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Publish the container.
     *
     */
    private void publishContainer() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId(); 
        final TableSorter sorter = (TableSorter) namesJTable.getModel();
        final CustomTableModel model = (CustomTableModel)(sorter.getTableModel());
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
        if(isInputValid()) {
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
            for (final TeamMember teamMember : teamMembers) {
                if (teamMember.getId().equals(contact.getId())) {
                    found = Boolean.TRUE;
                    break;
                }
            }
            if (!found) {
                contacts.add(contact);
            }
        }
        return contacts;
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
     * 
     * @param containerId
     *          The container id.
     * @return The list of team members.
     */
    private List<TeamMember> readTeamMembers(final Long containerId) {
        final Profile profile = readProfile();  
        final List<TeamMember> allTeamMembers = ((PublishContainerProvider)contentProvider).readTeamMembers(containerId);
        final List<TeamMember> teamMembers = new LinkedList <TeamMember>();
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
    private Map<User, ArtifactReceipt> readVersionUsers(final Long containerId, final Long versionId) {
        if (null==versionId) {
            // True if there is no published version yet.
            return null;
        } else {
            return ((PublishContainerProvider)contentProvider).getVersionUsers(containerId, versionId);
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
            final String comment = ((PublishContainerProvider) contentProvider).getContainerVersionComment(containerId, versionId);
            commentJTextField.setText(comment);
            commentJTextField.setEditable(false);
            commentJTextField.setFocusable(false);
        } else {
            commentJTextField.setText(null);
            commentJTextField.setEditable(true);
            commentJTextField.setFocusable(true);
        }
    }
    /**
     * Reload the explanation control.
     */
    private void reloadExplanation() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId();
        final Long versionId = getInputVersionId();
        final String name = ((PublishContainerProvider) contentProvider).getContainerName(containerId);
        if (publishType == PublishType.PUBLISH_VERSION) {
            final Calendar publishDate = getPublishDate(containerId, versionId);
            explanationJLabel.setText(getString("PublishVersionExplanation", new Object[] {name, publishDate.getTime()}));
        } else {
            explanationJLabel.setText(getString("Explanation", new Object[] {name}));    
        }
    }
    public enum DataKey { CONTAINER_ID, PUBLISH_TYPE, VERSION_ID }
    public enum PublishType { PUBLISH, PUBLISH_VERSION }
    /**
     * The table model.
     */
    private class CustomTableModel extends AbstractTableModel {
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        private final boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };
        private final List<Contact> contacts;
        
        private final User publisher;
        private List<Boolean> publishTo;
        private final PublishType publishType;
        private final List<TeamMember> teamMembers;
        private final Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
        private final Boolean versionExists;
        private final Map<User, ArtifactReceipt> versionUsers;
        
        public CustomTableModel(final PublishType publishType, final Long containerId, final Long versionId) {
            super();
            if (null==containerId) {
                Assert.assertNotNull("containerId cannot be null.", containerId);
                this.publishType = PublishType.PUBLISH;
                versionExists = Boolean.FALSE;
                publisher = null;
                versionUsers = null;
                teamMembers = null;
                contacts = null;
                publishTo = null;
            } else {
                this.publishType = publishType;
                versionExists = (null!=versionId);
                publisher = getPublisher(containerId, versionId);
                versionUsers = readVersionUsers(containerId, versionId);
                teamMembers = readTeamMembers(containerId);
                contacts = readContacts(teamMembers);
                publishTo = new ArrayList<Boolean>(getRowCount());
                
                // When publishing, by default select those that were
                // sent the last version (not the same as all team members)
                if (publishType == PublishType.PUBLISH) {                    
                    for (int i = 0; i < getRowCount(); i++) {
                        if (i < teamMembers.size()) {
                            TeamMember teamMember = teamMembers.get(i);
                            publishTo.add(isVersionUser(teamMember));
                        } else {
                            publishTo.add(Boolean.FALSE);
                        }
                    }
                } else {
                    for (int i = 0; i < getRowCount(); i++) {
                        publishTo.add(Boolean.FALSE);
                    }
                }
            }
        }
        
        public ArtifactReceipt getArtifactReceipt(final User user) {
            if (null != versionUsers) {
                for (final User versionUser : versionUsers.keySet()) {
                    if (versionUser.getId().equals(user.getId())) {
                        return versionUsers.get(versionUser);
                    }
                }
            }
            return null;
        }
        
        public Class<?> getColumnClass(final int columnIndex) {
            return types[columnIndex];
        }
        
        public int getColumnCount() {
            return 5;
        }
        
        public String getColumnName(final int columnIndex) {
            if (columnIndex == 4) {
                if (versionExists && (publishType==PublishType.PUBLISH)) {
                    return localization.getString("TableColumnTitle4_NotFirstPublish");
                } else if (versionExists && (publishType==PublishType.PUBLISH_VERSION)) {
                    return localization.getString("TableColumnTitle4_Forward"); 
                } else {
                    return localization.getString("TableColumnTitle4_FirstPublish");
                }
            } else {
                return localization.getString("TableColumnTitle" + columnIndex);
            }
        }
        
        // The "long value" is used to set up default column widths
        public Object getLongValue(final int columnIndex) {
            if (columnIndex==0) {
                return Boolean.TRUE;
            } else {
                return localization.getString("TableColumnLongValue" + columnIndex);
            }
        }
        
        public int getRowCount() {
            if ((null==teamMembers) || (null==contacts)) {
                return 0;
            } else {
                return teamMembers.size() + contacts.size();
            }
        }
        
        public List<Contact> getSelectedContacts() {
            List<Contact> selectedContacts = new ArrayList<Contact>();
            for (int i = 0; i < contacts.size(); i++ ) {
                if (publishTo.get(i+teamMembers.size()) == Boolean.TRUE) {
                    selectedContacts.add(contacts.get(i));
                }
            }
            return selectedContacts;
        }
        
        public List<TeamMember> getSelectedTeamMembers() {
            List<TeamMember> selectedTeamMembers = new ArrayList<TeamMember>();
            for (int i = 0; i < teamMembers.size(); i++ ) {               
                if (publishTo.get(i) == Boolean.TRUE) {
                    selectedTeamMembers.add(teamMembers.get(i));
                }
            }
            return selectedTeamMembers;
        }
        
        public Object getUserValueAt(final User user, final int columnIndex) {
            Object value;
            switch(columnIndex) {
            case 1:
                value = user.getName();
                break;
            case 2:
                value = user.getTitle();
                break;
            case 3:
                value = user.getOrganization();
                break;
            case 4:                     
                if ((null!=publisher) && (user.getId().equals(publisher.getId()))) {
                    value = localization.getString("TeamMemberPublisher");
                } else if (isVersionUser(user)) {
                    ArtifactReceipt receipt = getArtifactReceipt(user);    
                    if ((null == receipt) || (!receipt.isSetReceivedOn())) {
                        value = localization.getString("TeamMemberDidNotReceive");
                    } else {
                        value = localization.getString("TeamMemberReceived", new Object[] {receipt.getReceivedOn().getTime()});     
                    }
                } else if (teamMembers.contains(user)) {
                    // Team members that didn't get sent this version
                    if (publishType==PublishType.PUBLISH_VERSION) {
                        value = localization.getString("TeamMemberNotSentToForward"); 
                    } else {
                        value = localization.getString("TeamMemberNotSentToPublish");
                    }
                } else {
                    // Will be true for non-team members, eg. contacts
                    value = null;
                }
                break;
            default:
                value = null;
                break;
            }
            return value;
        }
        
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            if (columnIndex==0) {
                return publishTo.get(rowIndex);
            } else if ((rowIndex>=0) && (rowIndex<teamMembers.size())) {
                final TeamMember teamMember = teamMembers.get(rowIndex);
                return getUserValueAt((User)teamMember, columnIndex);
            } else if ((rowIndex>=teamMembers.size()) && (rowIndex<teamMembers.size()+contacts.size())) {
                final Contact contact = contacts.get(rowIndex-teamMembers.size());
                return getUserValueAt((User)contact, columnIndex);
            } else {
                return null;
            }
        }
        
        public boolean isCellEditable(final int rowIndex, final int columnIndex) {
            return canEdit [columnIndex];
        }
        
        public Boolean isVersionUser(final User user) {
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
        
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex==0) {
                publishTo.set(rowIndex, (Boolean) aValue);
                publishJButton.setEnabled(isInputValid());
            }
        }
    }
    
    /**
     * This class extends JTable with support for odd and even row background colours
     */
    private class PublishJTable extends javax.swing.JTable {
        
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;
        
        /**
         * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
         */
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isCellSelected(row, column)) {
                if (row % 2 == 0) {
                    c.setBackground(Colors.Browser.Table.ROW_EVEN_BG);
                } else {
                    c.setBackground(Colors.Browser.Table.ROW_ODD_BG);
                }
            } else {
                // If not shaded, match the table's background
                c.setBackground(getBackground());
            }
            
            return c;
        }
    }

    /**
     * This class changes the rendering of the table header.
     * Starting point taken from:
     *     http://www.chka.de/swing/table/faq.html
     * Mechanism to adjust the color during mouse rollover from:
     *     http://forum.java.sun.com/thread.jspa?forumID=57&threadID=435791
     */
    private class PublishTableHeaderRenderer extends DefaultTableCellRenderer {
        
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;
        
        // Mouse rollover column
        private int rolloverColumn = -1;
        
        // Table header
        private final JTableHeader tableHeader;
        
        public PublishTableHeaderRenderer(JTableHeader tableHeader) {
            this.tableHeader = tableHeader;
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);

            // This call is needed because DefaultTableCellRenderer calls
            // setBorder() in its constructor, which is executed after updateUI()
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            
            // Add listeners so we know when the mouse is over a column header
            tableHeader.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    updateRolloverColumn(e);
                }
                public void mouseExited(MouseEvent e) {
                    endRolloverColumn();
                }
            });
            tableHeader.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) { 
                    updateRolloverColumn(e);
                }
            });
            
        }
        
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean selected, boolean focused, int row,
                int column) {
            JTableHeader h = table != null ? table.getTableHeader() : null;

            if (h != null) {
                setEnabled(h.isEnabled());
                setComponentOrientation(h.getComponentOrientation());

                if (column == rolloverColumn) {
                    setBackground(Colors.Browser.Table.HEADER_ROLLOVER_BG);  
                    setForeground(Colors.Browser.Table.HEADER_ROLLOVER_FG); 
                } else if (selected || focused) {
                    setBackground(h.getBackground());
                    setForeground(h.getForeground()); 
                } else {
                    setBackground(Colors.Browser.Table.HEADER_BG);
                    setForeground(Colors.Browser.Table.HEADER_FG);
                }
                setFont(h.getFont());
            } else {
                /*
                 * Use sensible values instead of random leftover values from
                 * the last call
                 */
                setEnabled(true);
                setComponentOrientation(ComponentOrientation.UNKNOWN);

                setForeground(UIManager.getColor("TableHeader.foreground"));
                setBackground(UIManager.getColor("TableHeader.background"));
                setFont(UIManager.getFont("TableHeader.font"));
            }

            setValue(value);

            return this;
        }
        
        public void updateUI() {
            super.updateUI();
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        }

        private void endRolloverColumn() {
            rolloverColumn = -1;
            tableHeader.repaint();
        }

        private void updateRolloverColumn(MouseEvent e) {
            int col = tableHeader.columnAtPoint(e.getPoint());
            if (col != rolloverColumn) {
                rolloverColumn = col;
                tableHeader.repaint();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonBarJPanel;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel commentJLabel;
    private javax.swing.JTextField commentJTextField;
    private javax.swing.JLabel documentJLabel;
    private javax.swing.JLabel documentNameJLabel;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JScrollPane namesJScrollPane;
    private javax.swing.JTable namesJTable;
    private javax.swing.JPanel progressBarJPanel;
    private javax.swing.JButton publishJButton;
    private javax.swing.JProgressBar publishJProgressBar;
    // End of variables declaration//GEN-END:variables
}
