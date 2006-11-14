/*
 * PublishContainerAvatar.java
 *
 * Created on September 22, 2006, 11:15 AM
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
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.swing.TableSorter;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.model.user.TeamMember;

/**
 *
 * @author  Administrator
 */
public class PublishContainerAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** Creates new form PublishContainerAvatar */
    public PublishContainerAvatar() {
        super("PublishContainerDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        namesJScrollPane.getViewport().setBackground(BrowserConstants.DIALOGUE_BACKGROUND);
        namesJTable.setBackground(BrowserConstants.DIALOGUE_BACKGROUND);
        namesJTable.getTableHeader().setDefaultRenderer(new PublishTableHeaderRenderer(namesJTable.getTableHeader()));
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(State state) {        
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

    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) { 
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
            okJButton.setEnabled(isInputValid());
        }
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
       
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        explanationJLabel = new javax.swing.JLabel();
        commentJLabel = new javax.swing.JLabel();
        commentJTextField = new javax.swing.JTextField();
        namesJScrollPane = new javax.swing.JScrollPane();
        namesJTable = new PublishJTable();
        okJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

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

        okJButton.setText(bundle.getString("PublishContainerDialog.Ok")); // NOI18N
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("PublishContainerDialog.Cancel")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(commentJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(commentJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

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
                .add(namesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if(isInputValid()) {
            publishContainer();
            disposeWindow();
        }
    }//GEN-LAST:event_okJButtonActionPerformed
    
    /**
     * Publish the container. (Expect to call this if the user presses "OK" and input is valid.)
     */
    private void publishContainer() {
        final PublishType publishType = getInputPublishType();
        final Long containerId = getInputContainerId(); 
        final TableSorter sorter = (TableSorter)namesJTable.getModel();
        final CustomTableModel model = (CustomTableModel)(sorter.getTableModel());
        final List<TeamMember> teamMembers = model.getSelectedTeamMembers();
        final List<Contact> contacts = model.getSelectedContacts();
        if (publishType==PublishType.PUBLISH_VERSION) {
            final Long versionId = getInputVersionId();
            getController().runPublishContainerVersion(containerId, versionId, teamMembers, contacts);    
        } else {
            final String comment = commentJTextField.getText();
            getController().runPublishContainer(containerId, teamMembers, contacts, comment);  
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
     * The table model.
     */
    private class CustomTableModel extends AbstractTableModel {
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        private final Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
        private final boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };
        
        private final PublishType publishType;
        private final Boolean versionExists;
        private final User publisher;
        private final Map<User, ArtifactReceipt> versionUsers;
        private final List<TeamMember> teamMembers;
        private final List<Contact> contacts;
        private List<Boolean> publishTo;
        
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
        
        public List<TeamMember> getSelectedTeamMembers() {
            List<TeamMember> selectedTeamMembers = new ArrayList<TeamMember>();
            for (int i = 0; i < teamMembers.size(); i++ ) {               
                if (publishTo.get(i) == Boolean.TRUE) {
                    selectedTeamMembers.add(teamMembers.get(i));
                }
            }
            return selectedTeamMembers;
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
        
        public int getColumnCount() {
            return 5;
        }
        
        public Class<?> getColumnClass(final int columnIndex) {
            return types[columnIndex];
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
        
        public boolean isCellEditable(final int rowIndex, final int columnIndex) {
            return canEdit [columnIndex];
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
        
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex==0) {
                publishTo.set(rowIndex, (Boolean) aValue);
                okJButton.setEnabled(isInputValid());
            }
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
                if (user.getId().equals(publisher.getId())) {
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
        
        public Boolean isVersionUser(final User user) {
            for (final User versionUser : versionUsers.keySet()) {
                if (versionUser.getId().equals(user.getId())) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
            
        }
        
        public ArtifactReceipt getArtifactReceipt(final User user) {
            for (final User versionUser : versionUsers.keySet()) {
                if (versionUser.getId().equals(user.getId())) {
                    return versionUsers.get(versionUser);
                }
            }
            return null;
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
        
        // Table header
        private final JTableHeader tableHeader;
        
        // Mouse rollover column
        private int rolloverColumn = -1;
        
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
        
        private void updateRolloverColumn(MouseEvent e) {
            int col = tableHeader.columnAtPoint(e.getPoint());
            if (col != rolloverColumn) {
                rolloverColumn = col;
                tableHeader.repaint();
            }
        }
        
        private void endRolloverColumn() {
            rolloverColumn = -1;
            tableHeader.repaint();
        }

        public void updateUI() {
            super.updateUI();
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
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
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel commentJLabel;
    private javax.swing.JTextField commentJTextField;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JScrollPane namesJScrollPane;
    private javax.swing.JTable namesJTable;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables
    
    public enum PublishType { PUBLISH, PUBLISH_VERSION }
    public enum DataKey { PUBLISH_TYPE, CONTAINER_ID, VERSION_ID }
}
