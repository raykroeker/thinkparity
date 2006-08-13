/*
 * Created On: August 2, 2006, 1:51 PM
 */

package com.thinkparity.browser.application.browser.display.avatar.dialog.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.user.UserRenderer;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.model.UserUtil;

import com.thinkparity.model.parity.model.sort.user.UserComparatorFactory;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateTeamAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The backing list for the contacts. */
    private final List<User> backingContacts;
    
    /** The backing list for the team. */
    private final List<User> backingTeam;

    /** The comparator for the contacts backing list. */
    private final Comparator<User> contactsComparator;

    /** The contacts list model. */
    private final DefaultListModel contactsModel;

    /** The team list comparator. */
    private final Comparator<User> teamComparator;

    /** The team list model. */
    private final DefaultListModel teamModel;

    /** Creates new form ManageTeam */
    public UpdateTeamAvatar() {
        super("ManageTeam", BrowserConstants.DIALOGUE_BACKGROUND);
        this.backingContacts = new ArrayList<User>();
        this.backingTeam = new ArrayList<User>();
        this.contactsComparator = UserComparatorFactory.createName(Boolean.TRUE);
        this.contactsModel = new DefaultListModel();
        this.teamComparator = UserComparatorFactory.createName(Boolean.TRUE);
        this.teamModel = new DefaultListModel();
        initComponents();
    }
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_UPDATE_TEAM;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     * 
     */
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid()
     */
    @Override
    public Boolean isInputValid() { return Boolean.TRUE; }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {
        reloadContacts();
        reloadTeam();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {}

    private void addAllJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addAllJButtonActionPerformed
        addAllTeamMembers();
    }//GEN-LAST:event_addAllJButtonActionPerformed

    private void addAllTeamMembers() {
        final int[] indices = new int[contactsModel.size()];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;
        contactsJList.setSelectedIndices(indices);
        addTeamMembers();
    }

    private void addJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addJButtonActionPerformed
        addTeamMembers();
    }//GEN-LAST:event_addJButtonActionPerformed

    /**
     * Add a contact to the team.
     *
     */
    private void addTeamMembers() {
        final Object[] selectedValues = contactsJList.getSelectedValues();
        for(final Object selectedValue : selectedValues) {
            backingTeam.add((User) selectedValue);
        }
        syncModel();
    }

    /**
     * Add a team member from the contacts list.
     * 
     * @param evt
     *            An action event.
     */    /**
     * Cancel the update team action.
     * 
     * @param evt
     *            An action event.
     */
    private void closeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_closeJButtonActionPerformed

    private void contactsJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_contactsJListMouseClicked
        if (2 == e.getClickCount()) {
            addTeamMembers();
        }
    }//GEN-LAST:event_contactsJListMouseClicked

    /**
     * Extract the user input for the team.
     * 
     * @return A list of users.
     */
    private List<User> extractInputTeam() {
        return backingTeam;
    }

    /**
     * Obtain the container id from the input.
     * 
     * @return A container id.
     */
    private Long getInputContainerId() {
        if(null == input) {
            return null;
        } else {
            return (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton addAllJButton;
        javax.swing.JButton addJButton;
        javax.swing.JButton closeJButton;
        javax.swing.JLabel contactsJLabel;
        javax.swing.JScrollPane contactsJScrollPane;
        javax.swing.JLabel eaJLabel;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JPanel gridJPanel;
        javax.swing.JButton removeAllJButton;
        javax.swing.JButton removeJButton;
        javax.swing.JLabel separatorJLabel;
        javax.swing.JLabel teamJLabel;
        javax.swing.JScrollPane teamJScrollPane;
        javax.swing.JPanel updateTeamJPanel;

        updateTeamJPanel = new javax.swing.JPanel();
        eaJLabel = new javax.swing.JLabel();
        gridJPanel = new javax.swing.JPanel();
        contactsJLabel = new javax.swing.JLabel();
        contactsJScrollPane = new javax.swing.JScrollPane();
        contactsJList = new javax.swing.JList();
        addJButton = new javax.swing.JButton();
        addAllJButton = new javax.swing.JButton();
        separatorJLabel = new javax.swing.JLabel();
        teamJLabel = new javax.swing.JLabel();
        teamJScrollPane = new javax.swing.JScrollPane();
        teamJList = new javax.swing.JList();
        removeJButton = new javax.swing.JButton();
        removeAllJButton = new javax.swing.JButton();
        closeJButton = new javax.swing.JButton();

        updateTeamJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.UpdateTeamPanel")));
        updateTeamJPanel.setOpaque(false);
        eaJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.Explanation"));
        eaJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        gridJPanel.setLayout(new java.awt.GridBagLayout());

        gridJPanel.setOpaque(false);
        contactsJLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        contactsJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.ContactsJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        gridJPanel.add(contactsJLabel, gridBagConstraints);

        contactsJList.setModel(contactsModel);
        contactsJList.setCellRenderer(new UserRenderer());
        contactsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                contactsJListMouseClicked(e);
            }
        });

        contactsJScrollPane.setViewportView(contactsJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridJPanel.add(contactsJScrollPane, gridBagConstraints);

        addJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.AddJButton"));
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addJButtonActionPerformed(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gridJPanel.add(addJButton, gridBagConstraints);

        addAllJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.AddAllJButton"));
        addAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addAllJButtonActionPerformed(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 4);
        gridJPanel.add(addAllJButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        gridJPanel.add(separatorJLabel, gridBagConstraints);

        teamJLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        teamJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.TeamJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        gridJPanel.add(teamJLabel, gridBagConstraints);

        teamJList.setModel(teamModel);
        teamJList.setCellRenderer(new UserRenderer());
        teamJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                teamJListMouseClicked(e);
            }
        });

        teamJScrollPane.setViewportView(teamJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridJPanel.add(teamJScrollPane, gridBagConstraints);

        removeJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.RemoveJButton"));
        removeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removeJButtonActionPerformed(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gridJPanel.add(removeJButton, gridBagConstraints);

        removeAllJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.RemoveAllJButton"));
        removeAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removeAllJButtonActionPerformed(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 4);
        gridJPanel.add(removeAllJButton, gridBagConstraints);

        closeJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.CloseJButton"));
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                closeJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout updateTeamJPanelLayout = new org.jdesktop.layout.GroupLayout(updateTeamJPanel);
        updateTeamJPanel.setLayout(updateTeamJPanelLayout);
        updateTeamJPanelLayout.setHorizontalGroup(
            updateTeamJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(updateTeamJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(updateTeamJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gridJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, closeJButton))
                .addContainerGap())
        );
        updateTeamJPanelLayout.setVerticalGroup(
            updateTeamJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(updateTeamJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gridJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(closeJButton)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(updateTeamJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(updateTeamJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Load the list model with the contacts.
     * 
     * @param backingList
     *            A list of users.
     * @param contacts
     *            A list of contacts.
     * @param team
     *            A team.
     */
    private void loadContacts(final List<Contact> contacts,
            final List<TeamMember> team) {
        for (final Contact contact : contacts) {
            if (!UserUtil.contains(team, contact)) {
                backingContacts.add(contact);
            }
        }
        Collections.sort(backingContacts, contactsComparator);
        for (final User contact : backingContacts) {
            contactsModel.addElement(contact);
        }
    }

    /**
     * Load the list model with the team.
     * 
     * @param backingList
     *            A list of users.
     * @param team
     *            A team.
     */
    private void loadTeam(final List<TeamMember> team) {
        for (final TeamMember teamMember : team) {
            backingTeam.add(teamMember);
        }
        Collections.sort(backingTeam, teamComparator);
        for (final User teamMember : backingTeam) {
            teamModel.addElement(teamMember);
        }
    }

    /**
     * Obtain the list of contacts.
     *
     * @return The list of contacts.
     */
    private List<Contact> readContacts() {
        final Contact[] contactsArray =
            (Contact[]) ((CompositeFlatContentProvider) contentProvider).getElements(0, null);
        final List<Contact> contacts = new ArrayList<Contact>();
        for(final Contact contact : contactsArray) { contacts.add(contact); }
        return contacts;
    }

    /**
     * Obtain the list of team members.
     * 
     * @return The list of team members.
     */
    private List<TeamMember> readTeam() {
        final TeamMember[] teamArray =
            (TeamMember[]) ((CompositeFlatContentProvider) contentProvider).getElements(1, getInputContainerId());
        final List<TeamMember> team = new ArrayList<TeamMember>();
        for(final TeamMember teamMember : teamArray) { team.add(teamMember); }
        return team;
    }

    /** Reload the contacts backing list. */
    private void reloadContacts() {
        backingContacts.clear();
        contactsModel.clear();
        if (null != input) {
            loadContacts(readContacts(), readTeam());
        }
    }

    /** Reload the team list. */
    private void reloadTeam() {
        backingTeam.clear();
        teamModel.clear();
        if (null != input) {
            loadTeam(readTeam());
        }
    }

    private void removeAllJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_removeAllJButtonActionPerformed
        removeAllTeamMembers();
    }//GEN-LAST:event_removeAllJButtonActionPerformed

    /**
     * Remove all team members from the team.
     *
     */
    private void removeAllTeamMembers() {
        final int[] indices = new int[teamModel.size()];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;
        teamJList.setSelectedIndices(indices);
        removeTeamMembers();
    }

    private void removeJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_removeJButtonActionPerformed
        removeTeamMembers();
    }//GEN-LAST:event_removeJButtonActionPerformed

    /**
     * Remove a team member from the team.
     * 
     */
    private void removeTeamMembers() {
        final Object[] selectedValues = teamJList.getSelectedValues();
        for(final Object selectedValue : selectedValues) {
            backingTeam.remove((User) selectedValue);
        }
        syncModel();
    }

    /**
     * Synchronize the model with the backing lists.
     *
     */
    private void syncModel() {
        // update the team
        getController().runUpdateContainerTeam(getInputContainerId(), extractInputTeam());

        // reload contacts
        reloadContacts();

        // reload team
        reloadTeam();
    }

    private void teamJListMouseClicked(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_teamJListMouseClicked
        if (2 == e.getClickCount()) {
            removeTeamMembers();
        }
    }//GEN-LAST:event_teamJListMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList contactsJList;
    private javax.swing.JList teamJList;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { CONTAINER_ID }
}
