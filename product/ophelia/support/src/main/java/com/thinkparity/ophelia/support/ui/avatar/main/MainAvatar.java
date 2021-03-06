/*
 * Created On:  2007-11-19 10:25 -0700
 */
package com.thinkparity.ophelia.support.ui.avatar.main;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.thinkparity.ophelia.support.ui.avatar.AbstractAvatar;
import com.thinkparity.ophelia.support.ui.avatar.AvatarFactory;
import com.thinkparity.ophelia.support.ui.avatar.application.ApplicationAvatar;
import com.thinkparity.ophelia.support.ui.avatar.network.NetworkAvatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainAvatar extends AbstractAvatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.support.ui.avatar.application.ApplicationAvatar applicationAvatar;
    private com.thinkparity.ophelia.support.ui.avatar.network.NetworkAvatar networkAvatar;
    private javax.swing.JLabel tabSepJLabel_0;
    // End of variables declaration//GEN-END:variables

    /** The tab. */
    private Tab tab;

    /**
     * Create MainAvatar
     *
     */
    public MainAvatar() {
        super("/main/main");
        initComponents();
    }

    /**
     * Select the application tab.
     * 
     */
    public void selectApplication() {
        select(Tab.APPLICATION, applicationAvatar, networkAvatar);
    }

    /**
     * Select the network tab.
     * 
     */
    public void selectNetwork() {
        select(Tab.NETWORK, networkAvatar, applicationAvatar);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        networkAvatar = (NetworkAvatar) AvatarFactory.getInstance().newAvatar("/network/network");
        applicationAvatar = (ApplicationAvatar) AvatarFactory.getInstance().newAvatar("/application/application");
        javax.swing.JLabel tabApplicationJLabel = new javax.swing.JLabel();
        tabSepJLabel_0 = new javax.swing.JLabel();
        javax.swing.JLabel tabNetworkJLabel = new javax.swing.JLabel();
        javax.swing.JButton quitJButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/UIMessages"); // NOI18N
        tabApplicationJLabel.setText(bundle.getString("avatar.main.mainavatar.tabapplicationtext")); // NOI18N
        tabApplicationJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabApplicationJLabelMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        add(tabApplicationJLabel, gridBagConstraints);

        tabSepJLabel_0.setText(">");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 0, 3);
        add(tabSepJLabel_0, gridBagConstraints);

        tabNetworkJLabel.setText(bundle.getString("avatar.main.mainavatar.tabnetworktext")); // NOI18N
        tabNetworkJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabNetworkJLabelMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(tabNetworkJLabel, gridBagConstraints);

        quitJButton.setText(bundle.getString("avatar.main.mainavatar.quitbuttontext")); // NOI18N
        quitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitJButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        add(quitJButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void quitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitJButtonActionPerformed
        getContext().newActionRunner("/main/quit").run();
    }//GEN-LAST:event_quitJButtonActionPerformed

    /**
     * Select a tab.
     * 
     * @param tab
     *            A <code>Tab</code>.
     * @param select
     *            A <code>JPanel</code>.
     * @param unselectArray
     *            An optional <code>JPanel[]</code>.
     */
    private void select(final Tab tab, final JPanel select,
            final JPanel... unselectArray) {
        if (this.tab == tab) {
            return;
        } else {
            this.tab = tab;
            select.setVisible(true);
            add(select, newSelectConstraints());

            for (JPanel unselect : unselectArray) {
                unselect.setVisible(false);
                remove(unselect);
            }
        }
    }

    private Object newSelectConstraints() {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 1.0F;
        constraints.weighty = 1.0F;
        return constraints;
    }

    private void tabApplicationJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabApplicationJLabelMousePressed
        getContext().newActionRunner("/application/selecttab").run();
    }//GEN-LAST:event_tabApplicationJLabelMousePressed

    private void tabNetworkJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabNetworkJLabelMousePressed
        getContext().newActionRunner("/network/selecttab").run();
    }//GEN-LAST:event_tabNetworkJLabelMousePressed

    /** <b>Title:</b>Main Avatar Tab<br> */
    private enum Tab { APPLICATION, NETWORK }
}
