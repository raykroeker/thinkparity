/*
 * ManageTeam.java
 *
 * Created on August 2, 2006, 1:51 PM
 */

package com.thinkparity.browser.application.browser.display.avatar.container;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.session.ContactCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 *
 * @author  Administrator
 */
public class ManageTeam extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /**
     * The contacts list.
     */
    private DefaultListModel contactsModel;
    
    /**
     * The team members list.
     */
    private DefaultListModel teamModel;
    
    /** Creates new form ManageTeam */
    public ManageTeam() {
        super("ManageTeam", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.MANAGE_TEAM;
    }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {
        // Check for null input because reload() is called once while setting up the
        // provider, but before the input is ready. Browser.setInput() creates the
        // Avatar which adds the provider which calls reload().
        if (null!=input) {
            contactsModel.clear();
            loadContacts(contactsModel, getContacts());
            teamModel.clear();
            loadTeamMembers(teamModel, getTeamMembers());
            removeTeamMembersFromContacts();
        }
    }
    
    /**
     * Obtain the list of contacts.
     *
     * @return The list of contacts.
     */
    private Contact[] getContacts() {
        return (Contact[]) ((CompositeFlatContentProvider) contentProvider)
                .getElements(0,null);
    }
    
    /**
     * Obtain the list of team members.
     * 
     * @return The list of team members.
     */
    private TeamMember[] getTeamMembers() {
        final Long containerId = (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        return (TeamMember[]) ((CompositeFlatContentProvider) contentProvider)
                 .getElements(1,containerId);
    }
    
    /**
     * Load the contacts into the model, as users.
     * 
     * @param listModel
     *            The contacts list.
     * @param contacts
     *            The list of contacts.
     */
    private void loadContacts(final DefaultListModel listModel,
            final Contact[] contacts) {
        for(final Contact contact : contacts) {
            final User user = (User) contact;
            listModel.addElement(user);
        }
    }
    
    /**
     * Load the team members into the model.
     * 
     * @param listModel
     *            The contacts list.
     * @param teamMembers
     *            The list of team members.
     */
    private void loadTeamMembers(final DefaultListModel listModel,
            final TeamMember[] teamMembers) {
        for(final TeamMember teamMember : teamMembers) {
            final User user = (User) teamMember;           
            listModel.addElement(user);
        }
    }
    
    /**
     * Remove team members from the contacts list.
     */
    private void removeTeamMembersFromContacts() {
        for (int t=0; t<teamModel.getSize(); t++) {
            final User teamMember = (User) teamModel.getElementAt(t);
            for (int c=0; c<contactsModel.getSize(); c++) {
                final User contact = (User) contactsModel.getElementAt(c);
                if (teamMember.getId()==contact.getId()) {
                    contactsModel.removeElement(contact);
                    break;
                }
            }
        }
    }
    
    /**
     * Get the team members from the JList.
     */
    private List<User> getTeamMembersFromJList() {
        final List<User> teamMembers = new ArrayList<User>();
        for (int i=0; i<teamModel.getSize(); i++) {
            teamMembers.add((User) teamModel.getElementAt(i));
        }
        return teamMembers;
    }
    
    /**
     * Set up the team.
     */
    private void setUpTeam() {
        final Long containerId = (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        final List<User> teamMembers = getTeamMembersFromJList();
        getController().runManageTeam(containerId, teamMembers);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        explanationJTextArea = new javax.swing.JTextArea();
        contactsJLabel = new javax.swing.JLabel();
        teamJLabel = new javax.swing.JLabel();
        contactsJScrollPane = new javax.swing.JScrollPane();
        contactsJList = new javax.swing.JList();
        teamJScrollPane = new javax.swing.JScrollPane();
        teamJList = new javax.swing.JList();
        addToTeamJButton = new javax.swing.JButton();
        removeFromTeamJButton = new javax.swing.JButton();
        okJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        explanationJTextArea.setColumns(20);
        explanationJTextArea.setEditable(false);
        explanationJTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
        explanationJTextArea.setLineWrap(true);
        explanationJTextArea.setRows(5);
        explanationJTextArea.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.Explanation"));
        explanationJTextArea.setWrapStyleWord(true);
        explanationJTextArea.setBorder(null);
        explanationJTextArea.setFocusable(false);
        explanationJTextArea.setOpaque(false);

        contactsJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        contactsJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.ContactsLabel"));

        teamJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        teamJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.TeamLabel"));

        contactsModel = new DefaultListModel();
        contactsJList.setModel(contactsModel);
        contactsJList.setCellRenderer(new ContactCellRenderer());
        contactsJScrollPane.setViewportView(contactsJList);

        teamModel = new DefaultListModel();
        teamJList.setModel(teamModel);
        teamJList.setCellRenderer(new ContactCellRenderer());
        teamJScrollPane.setViewportView(teamJList);

        addToTeamJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.AddToTeam"));
        addToTeamJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToTeamJButtonActionPerformed(evt);
            }
        });

        removeFromTeamJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.RemoveFromTeam"));
        removeFromTeamJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromTeamJButtonActionPerformed(evt);
            }
        });

        okJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.Ok"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("ManageTeam.Cancel"));
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
                    .add(explanationJTextArea)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(contactsJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                .add(8, 8, 8))
                            .add(layout.createSequentialGroup()
                                .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(removeFromTeamJButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(addToTeamJButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(teamJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(teamJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {contactsJScrollPane, teamJScrollPane}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {contactsJLabel, teamJLabel}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, 0)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(contactsJLabel)
                            .add(teamJLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(teamJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                            .add(contactsJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cancelJButton)
                            .add(okJButton)))
                    .add(layout.createSequentialGroup()
                        .add(108, 108, 108)
                        .add(addToTeamJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeFromTeamJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {contactsJLabel, teamJLabel}, org.jdesktop.layout.GroupLayout.VERTICAL);

        layout.linkSize(new java.awt.Component[] {contactsJScrollPane, teamJScrollPane}, org.jdesktop.layout.GroupLayout.VERTICAL);

    }// </editor-fold>//GEN-END:initComponents

    private void removeFromTeamJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromTeamJButtonActionPerformed
        int[] selected = teamJList.getSelectedIndices();
        for (final int index : selected) {
            contactsModel.addElement(teamModel.getElementAt(index));
        }
        for (int index = selected.length-1; index>=0; index--) {
            teamModel.removeElementAt(selected[index]);
        }
    }//GEN-LAST:event_removeFromTeamJButtonActionPerformed

    private void addToTeamJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToTeamJButtonActionPerformed
        int[] selected = contactsJList.getSelectedIndices();
        for (final int index : selected) {
            teamModel.addElement(contactsModel.getElementAt(index));
        }
        for (int index = selected.length-1; index>=0; index--) {
            contactsModel.removeElementAt(selected[index]);
        }
    }//GEN-LAST:event_addToTeamJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        setUpTeam();
        disposeWindow();
    }//GEN-LAST:event_okJButtonActionPerformed
    
    /**
     * Dispose of the window.
     */
    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToTeamJButton;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel contactsJLabel;
    private javax.swing.JList contactsJList;
    private javax.swing.JScrollPane contactsJScrollPane;
    private javax.swing.JTextArea explanationJTextArea;
    private javax.swing.JButton okJButton;
    private javax.swing.JButton removeFromTeamJButton;
    private javax.swing.JLabel teamJLabel;
    private javax.swing.JList teamJList;
    private javax.swing.JScrollPane teamJScrollPane;
    // End of variables declaration//GEN-END:variables
    
    public enum DataKey { CONTAINER_ID }
    
}
